package com.fingerspell.tutor.presentation.lessonpath

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fingerspell.tutor.core.config.GameConfig
import com.fingerspell.tutor.domain.model.Handedness
import com.fingerspell.tutor.domain.repository.ContentRepository
import com.fingerspell.tutor.domain.repository.ProgressRepository
import com.fingerspell.tutor.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LessonPathViewModel(
    private val content: ContentRepository,
    private val progress: ProgressRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LessonPathUiState())
    val state: StateFlow<LessonPathUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val learner = progress.snapshot()
            val prefs = settings.load()
            val topics = content.topics()
            var currentMarked = false
            val rows = content.lessons().map { lesson ->
                if (lesson.unit == 1) {
                    val complete = lesson.letters.all {
                        (learner.boxes[it] ?: 0) >= GameConfig.KNOWN_BOX
                    }
                    val current = !complete && !currentMarked
                    if (current) currentMarked = true
                    LessonRow(
                        id = lesson.id,
                        unit = 1,
                        letters = lesson.letters,
                        complete = complete,
                        current = current,
                        spec = content.handshape(lesson.letters.first()).spec
                    )
                } else {
                    val topic = topics[(lesson.id - 10).coerceIn(0, topics.lastIndex)]
                    LessonRow(
                        id = lesson.id,
                        unit = 2,
                        topicLabelRes = topic.labelRes,
                        spec = content.signs().first { it.topicId == topic.id }.spec
                    )
                }
            }
            _state.update {
                it.copy(
                    rows = rows,
                    knownCount = learner.knownCount(GameConfig.KNOWN_BOX),
                    skinToneIndex = prefs.skinToneIndex,
                    mirrored = prefs.handedness == Handedness.LEFT
                )
            }
        }
    }
}
