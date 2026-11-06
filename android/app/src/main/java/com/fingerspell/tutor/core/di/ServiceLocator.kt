package com.fingerspell.tutor.core.di

import android.content.Context
import com.fingerspell.tutor.data.local.TutorPreferences
import com.fingerspell.tutor.data.repository.ContentRepositoryImpl
import com.fingerspell.tutor.data.repository.ProgressRepositoryImpl
import com.fingerspell.tutor.data.repository.SettingsRepositoryImpl

object ServiceLocator {

    private var viewModelFactory: AppViewModelFactory? = null

    fun init(context: Context) {
        if (viewModelFactory != null) return
        val prefs = TutorPreferences(context.applicationContext)
        viewModelFactory = AppViewModelFactory(
            content = ContentRepositoryImpl(),
            progress = ProgressRepositoryImpl(prefs),
            settings = SettingsRepositoryImpl(prefs)
        )
    }

    val factory: AppViewModelFactory
        get() = requireNotNull(viewModelFactory)
}
