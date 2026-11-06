package com.fingerspell.tutor.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fingerspell.tutor.core.config.GameConfig
import com.fingerspell.tutor.core.di.ServiceLocator
import com.fingerspell.tutor.core.navigation.Screen
import com.fingerspell.tutor.domain.model.DrillMode
import com.fingerspell.tutor.presentation.culture.CultureNotesRoute
import com.fingerspell.tutor.presentation.game.GameRoute
import com.fingerspell.tutor.presentation.gameover.GameOverRoute
import com.fingerspell.tutor.presentation.glossary.GlossaryRoute
import com.fingerspell.tutor.presentation.handshape.HandshapeCardRoute
import com.fingerspell.tutor.presentation.lessonpath.LessonPathRoute
import com.fingerspell.tutor.presentation.menu.MenuRoute
import com.fingerspell.tutor.presentation.mirror.PracticeMirrorRoute
import com.fingerspell.tutor.presentation.settings.SettingsRoute
import com.fingerspell.tutor.presentation.signs.EverydaySignsRoute
import com.fingerspell.tutor.presentation.splash.SplashRoute
import com.fingerspell.tutor.presentation.wordreader.WordReaderRoute

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.factory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val forward by appViewModel.forward.collectAsStateWithLifecycle()

    BackHandler(enabled = screen != Screen.Menu && screen != Screen.Splash) {
        appViewModel.back()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                val duration = GameConfig.SCREEN_TRANSITION_MS
                if (initialState == Screen.Splash) {
                    (fadeIn(tween(duration)) + slideInVertically(tween(duration)) { it / 12 })
                        .togetherWith(fadeOut(tween(duration)))
                } else if (forward) {
                    (slideInHorizontally(tween(duration)) { it / 4 } + fadeIn(tween(duration)))
                        .togetherWith(
                            slideOutHorizontally(tween(duration)) { -it / 6 } + fadeOut(tween(duration))
                        )
                } else {
                    (slideInHorizontally(tween(duration)) { -it / 4 } + fadeIn(tween(duration)))
                        .togetherWith(
                            slideOutHorizontally(tween(duration)) { it / 6 } + fadeOut(tween(duration))
                        )
                }
            },
            label = "screenHost"
        ) { current ->
            Box(modifier = Modifier.fillMaxSize()) {
                AppDestination(current, appViewModel)
            }
        }
    }
}

@Composable
private fun AppDestination(screen: Screen, appViewModel: AppViewModel) {
    when (screen) {
        Screen.Splash -> SplashRoute(onFinished = appViewModel::finishSplash)
        Screen.Menu -> MenuRoute(
            onPlay = { letters -> appViewModel.startDrill(DrillMode.LessonSet(letters)) },
            onReview = { appViewModel.startDrill(DrillMode.Review) },
            onDestination = { index, letter ->
                when (index) {
                    0 -> appViewModel.navigate(Screen.LessonPath)
                    1 -> appViewModel.navigate(Screen.WordReader)
                    2 -> appViewModel.navigate(Screen.PracticeMirror(letter))
                    3 -> appViewModel.navigate(Screen.EverydaySigns)
                    4 -> appViewModel.navigate(Screen.Glossary)
                    5 -> appViewModel.navigate(Screen.CultureNotes)
                    else -> appViewModel.navigate(Screen.Settings)
                }
            }
        )
        Screen.LessonPath -> LessonPathRoute(
            onBack = { appViewModel.back() },
            onHome = appViewModel::toMenu,
            onOpenLetter = { appViewModel.navigate(Screen.HandshapeCard(it)) },
            onDrill = { appViewModel.startDrill(DrillMode.LessonSet(it)) },
            onOpenSigns = { appViewModel.navigate(Screen.EverydaySigns) }
        )
        is Screen.HandshapeCard -> HandshapeCardRoute(
            letter = screen.letter,
            onBack = { appViewModel.back() },
            onHome = appViewModel::toMenu,
            onMirror = { appViewModel.navigate(Screen.PracticeMirror(it)) },
            onDrill = { appViewModel.startDrill(DrillMode.LessonSet(it)) }
        )
        is Screen.Game -> GameRoute(
            mode = screen.mode,
            onFinished = appViewModel::showResult,
            onQuit = appViewModel::toMenu
        )
        is Screen.GameOver -> GameOverRoute(
            result = screen.result,
            onPlayAgain = { appViewModel.startDrill(appViewModel.lastDrillMode) },
            onMenu = appViewModel::toMenu,
            onOpenLetter = { appViewModel.navigate(Screen.HandshapeCard(it)) }
        )
        Screen.WordReader -> WordReaderRoute(
            onBack = { appViewModel.back() },
            onHome = appViewModel::toMenu
        )
        is Screen.PracticeMirror -> PracticeMirrorRoute(
            letter = screen.letter,
            onBack = { appViewModel.back() },
            onHome = appViewModel::toMenu
        )
        Screen.EverydaySigns -> EverydaySignsRoute(
            onBack = { appViewModel.back() },
            onHome = appViewModel::toMenu
        )
        Screen.Glossary -> GlossaryRoute(
            onBack = { appViewModel.back() },
            onHome = appViewModel::toMenu,
            onOpenLetter = { appViewModel.navigate(Screen.HandshapeCard(it)) }
        )
        Screen.CultureNotes -> CultureNotesRoute(
            onBack = { appViewModel.back() },
            onHome = appViewModel::toMenu
        )
        Screen.Settings -> SettingsRoute(
            onBack = { appViewModel.back() },
            onHome = appViewModel::toMenu
        )
    }
}
