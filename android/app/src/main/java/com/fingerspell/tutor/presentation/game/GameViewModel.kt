package com.fingerspell.tutor.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fingerspell.tutor.core.config.GameConfig
import com.fingerspell.tutor.domain.model.DrillAnswer
import com.fingerspell.tutor.domain.model.DrillMode
import com.fingerspell.tutor.domain.model.DrillQuestion
import com.fingerspell.tutor.domain.model.DrillResult
import com.fingerspell.tutor.domain.model.Handedness
import com.fingerspell.tutor.domain.repository.ContentRepository
import com.fingerspell.tutor.domain.repository.ProgressRepository
import com.fingerspell.tutor.domain.repository.SettingsRepository
import com.fingerspell.tutor.domain.usecase.BuildDrillUseCase
import com.fingerspell.tutor.domain.usecase.ScoreDrillUseCase
import com.fingerspell.tutor.domain.usecase.UpdateReviewScheduleUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val content: ContentRepository,
    private val progress: ProgressRepository,
    private val settings: SettingsRepository,
    private val buildDrill: BuildDrillUseCase,
    private val scoreDrill: ScoreDrillUseCase,
    private val updateSchedule: UpdateReviewScheduleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _results = Channel<DrillResult>(Channel.BUFFERED)
    val results: Flow<DrillResult> = _results.receiveAsFlow()

    private var questions: List<DrillQuestion> = emptyList()
    private val answers = mutableListOf<DrillAnswer>()
    private var advanceJob: Job? = null

    fun startDrill(mode: DrillMode) {
        advanceJob?.cancel()
        answers.clear()
        viewModelScope.launch {
            val learner = progress.snapshot()
            val prefs = settings.load()
            questions = buildDrill(mode, content.handshapes(), learner, progress.today())
            _state.value = GameUiState(
                phase = GamePhase.IDLE,
                index = 0,
                total = questions.size,
                target = questions[0].target,
                options = questions[0].options,
                spec = content.handshape(questions[0].target).spec,
                segments = List(questions.size) { SegmentState.PENDING },
                skinToneIndex = prefs.skinToneIndex,
                mirrored = prefs.handedness == Handedness.LEFT,
                haptics = prefs.haptics
            )
        }
    }

    fun answer(option: String) {
        val current = _state.value
        if (current.phase != GamePhase.IDLE) return
        val isCorrect = option == current.target
        answers.add(DrillAnswer(current.target, option))
        val segments = current.segments.toMutableList()
        if (current.index < segments.size) {
            segments[current.index] = if (isCorrect) SegmentState.CORRECT else SegmentState.WRONG
        }
        _state.update {
            it.copy(
                phase = GamePhase.ANSWERED,
                picked = option,
                segments = segments,
                correctCount = it.correctCount + if (isCorrect) 1 else 0,
                streak = if (isCorrect) it.streak + 1 else 0,
                answered = it.answered + 1,
                feedbackTick = it.feedbackTick + 1,
                lastCorrect = isCorrect
            )
        }
        advanceJob = viewModelScope.launch {
            delay(GameConfig.FEEDBACK_HOLD_MS)
            val next = current.index + 1
            if (next >= questions.size) finish() else show(next)
        }
    }

    fun stop() {
        advanceJob?.cancel()
    }

    private fun show(next: Int) {
        val question = questions[next]
        _state.update {
            it.copy(
                phase = GamePhase.IDLE,
                index = next,
                target = question.target,
                options = question.options,
                picked = "",
                spec = content.handshape(question.target).spec
            )
        }
    }

    private suspend fun finish() {
        val before = progress.snapshot()
        val today = progress.today()
        val after = updateSchedule(before, answers.toList(), today)
        progress.save(after)
        val result = scoreDrill(
            answers = answers.toList(),
            before = before,
            after = after,
            scheduledTomorrow = updateSchedule.scheduledFor(after, today + 1)
        )
        _state.update { it.copy(phase = GamePhase.FINISHED) }
        _results.send(result)
    }
}
