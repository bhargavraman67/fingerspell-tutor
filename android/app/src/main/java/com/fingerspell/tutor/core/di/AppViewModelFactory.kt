package com.fingerspell.tutor.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.fingerspell.tutor.domain.repository.ContentRepository
import com.fingerspell.tutor.domain.repository.ProgressRepository
import com.fingerspell.tutor.domain.repository.SettingsRepository
import com.fingerspell.tutor.domain.usecase.BuildDrillUseCase
import com.fingerspell.tutor.domain.usecase.GetDueReviewsUseCase
import com.fingerspell.tutor.domain.usecase.PromoteLetterUseCase
import com.fingerspell.tutor.domain.usecase.ScoreDrillUseCase
import com.fingerspell.tutor.domain.usecase.UpdateReviewScheduleUseCase
import com.fingerspell.tutor.presentation.app.AppViewModel
import com.fingerspell.tutor.presentation.culture.CultureNotesViewModel
import com.fingerspell.tutor.presentation.game.GameViewModel
import com.fingerspell.tutor.presentation.gameover.GameOverViewModel
import com.fingerspell.tutor.presentation.glossary.GlossaryViewModel
import com.fingerspell.tutor.presentation.handshape.HandshapeCardViewModel
import com.fingerspell.tutor.presentation.lessonpath.LessonPathViewModel
import com.fingerspell.tutor.presentation.menu.MenuViewModel
import com.fingerspell.tutor.presentation.mirror.PracticeMirrorViewModel
import com.fingerspell.tutor.presentation.settings.SettingsViewModel
import com.fingerspell.tutor.presentation.signs.EverydaySignsViewModel
import com.fingerspell.tutor.presentation.splash.SplashViewModel
import com.fingerspell.tutor.presentation.wordreader.WordReaderViewModel

class AppViewModelFactory(
    private val content: ContentRepository,
    private val progress: ProgressRepository,
    private val settings: SettingsRepository
) : ViewModelProvider.Factory {

    private val buildDrill = BuildDrillUseCase()
    private val scoreDrill = ScoreDrillUseCase()
    private val updateSchedule = UpdateReviewScheduleUseCase()
    private val getDue = GetDueReviewsUseCase()
    private val promote = PromoteLetterUseCase()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) ->
                AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) ->
                SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(content, progress, settings, getDue)
            modelClass.isAssignableFrom(LessonPathViewModel::class.java) ->
                LessonPathViewModel(content, progress, settings)
            modelClass.isAssignableFrom(HandshapeCardViewModel::class.java) ->
                HandshapeCardViewModel(content, settings)
            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(content, progress, settings, buildDrill, scoreDrill, updateSchedule)
            modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
                GameOverViewModel(content, settings)
            modelClass.isAssignableFrom(WordReaderViewModel::class.java) ->
                WordReaderViewModel(content, settings)
            modelClass.isAssignableFrom(PracticeMirrorViewModel::class.java) ->
                PracticeMirrorViewModel(content, settings, progress, promote)
            modelClass.isAssignableFrom(EverydaySignsViewModel::class.java) ->
                EverydaySignsViewModel(content, settings)
            modelClass.isAssignableFrom(GlossaryViewModel::class.java) ->
                GlossaryViewModel(content, progress, settings)
            modelClass.isAssignableFrom(CultureNotesViewModel::class.java) ->
                CultureNotesViewModel()
            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(settings, progress)
            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
