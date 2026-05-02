package com.ivaniuk.algolearnsimple.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.content.edit

@HiltViewModel
class TutorialViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val prefs = context.getSharedPreferences("tutorial", Context.MODE_PRIVATE)

    fun isTutorialSeen(): Boolean {
        return prefs.getBoolean("tutorial_seen", false)
    }

    fun setTutorialSeen() {
        prefs.edit { putBoolean("tutorial_seen", true) }
    }
}