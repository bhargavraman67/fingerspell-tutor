package com.fingerspell.tutor.presentation.app

import androidx.lifecycle.ViewModel
import com.fingerspell.tutor.core.navigation.Screen
import com.fingerspell.tutor.domain.model.DrillMode
import com.fingerspell.tutor.domain.model.DrillResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val backStack = mutableListOf<Screen>()

    var lastDrillMode: DrillMode = DrillMode.Mixed
        private set

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _forward = MutableStateFlow(true)
    val forward: StateFlow<Boolean> = _forward.asStateFlow()

    fun navigate(target: Screen) {
        val current = _screen.value
        if (current != Screen.Splash) backStack.add(current)
        _forward.value = true
        _screen.value = target
    }

    fun startDrill(mode: DrillMode) {
        lastDrillMode = mode
        navigate(Screen.Game(mode))
    }

    fun finishSplash() {
        backStack.clear()
        _forward.value = true
        _screen.value = Screen.Menu
    }

    fun showResult(result: DrillResult) {
        backStack.clear()
        backStack.add(Screen.Menu)
        _forward.value = true
        _screen.value = Screen.GameOver(result)
    }

    fun back(): Boolean {
        if (backStack.isEmpty()) return false
        val previous = backStack.removeAt(backStack.lastIndex)
        _forward.value = false
        _screen.value = previous
        return true
    }

    fun toMenu() {
        backStack.clear()
        _forward.value = false
        _screen.value = Screen.Menu
    }
}
