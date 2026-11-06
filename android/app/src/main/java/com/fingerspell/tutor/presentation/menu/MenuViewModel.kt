package com.fingerspell.tutor.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fingerspell.tutor.core.config.GameConfig
import com.fingerspell.tutor.domain.model.Handedness
import com.fingerspell.tutor.domain.repository.ContentRepository
import com.fingerspell.tutor.domain.repository.ProgressRepository
import com.fingerspell.tutor.domain.repository.SettingsRepository
import com.fingerspell.tutor.domain.usecase.GetDueReviewsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val content: ContentRepository,
    private val progress: ProgressRepository,
    private val settings: SettingsRepository,
    private val getDueReviews: GetDueReviewsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val learner = progress.snapshot()
            val prefs = settings.load()
            val today = progress.today()
            val lesson = content.lessons()
                .filter { it.unit == 1 }
                .firstOrNull { lessonItem ->
                    lessonItem.letters.any { (learner.boxes[it] ?: 0) < GameConfig.KNOWN_BOX }
                }
                ?: content.lessons().first()
            val heroLetter = lesson.letters.firstOrNull() ?: "A"
            _state.update {
                it.copy(
                    ready = true,
                    knownCount = learner.knownCount(GameConfig.KNOWN_BOX),
                    accuracy = learner.accuracy,
                    bestStreak = learner.bestStreak,
                    dueCount = getDueReviews(learner, today).size,
                    lessonNumber = lesson.id,
                    lessonLetters = lesson.letters,
                    heroLetter = heroLetter,
                    heroSpec = content.handshape(heroLetter).spec,
                    skinToneIndex = prefs.skinToneIndex,
                    mirrored = prefs.handedness == Handedness.LEFT
                )
            }
        }
    }
}
