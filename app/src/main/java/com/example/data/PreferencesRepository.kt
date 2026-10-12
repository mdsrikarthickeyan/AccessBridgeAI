package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.AccessibilityPreferences
import com.example.model.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreferencesRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("accessbridge_prefs", Context.MODE_PRIVATE)

    private val _preferences = MutableStateFlow(loadPreferences())
    val preferences: StateFlow<AccessibilityPreferences> = _preferences.asStateFlow()

    private fun loadPreferences(): AccessibilityPreferences {
        val highContrast = prefs.getBoolean("high_contrast", false)
        val fontScale = prefs.getFloat("font_scale", 1.0f)
        val dyslexiaSpacing = prefs.getBoolean("dyslexia_spacing", false)
        val reducedMotion = prefs.getBoolean("reduced_motion", false)
        val langCode = prefs.getString("preferred_lang", AppLanguage.ENGLISH.code) ?: AppLanguage.ENGLISH.code
        val lang = AppLanguage.entries.find { it.code == langCode } ?: AppLanguage.ENGLISH
        val speechRate = prefs.getFloat("speech_rate", 1.0f)
        val haptic = prefs.getBoolean("haptic", true)
        val flashcards = prefs.getBoolean("flashcards_hc", true)

        return AccessibilityPreferences(
            highContrast = highContrast,
            fontScale = fontScale,
            dyslexiaSpacing = dyslexiaSpacing,
            reducedMotion = reducedMotion,
            preferredLanguage = lang,
            speechRate = speechRate,
            hapticFeedback = haptic,
            highContrastFlashcards = flashcards
        )
    }

    fun updateHighContrast(enabled: Boolean) {
        prefs.edit().putBoolean("high_contrast", enabled).apply()
        _preferences.value = _preferences.value.copy(highContrast = enabled)
    }

    fun updateFontScale(scale: Float) {
        val bounded = scale.coerceIn(1.0f, 1.6f)
        prefs.edit().putFloat("font_scale", bounded).apply()
        _preferences.value = _preferences.value.copy(fontScale = bounded)
    }

    fun updateDyslexiaSpacing(enabled: Boolean) {
        prefs.edit().putBoolean("dyslexia_spacing", enabled).apply()
        _preferences.value = _preferences.value.copy(dyslexiaSpacing = enabled)
    }

    fun updateReducedMotion(enabled: Boolean) {
        prefs.edit().putBoolean("reduced_motion", enabled).apply()
        _preferences.value = _preferences.value.copy(reducedMotion = enabled)
    }

    fun updatePreferredLanguage(language: AppLanguage) {
        prefs.edit().putString("preferred_lang", language.code).apply()
        _preferences.value = _preferences.value.copy(preferredLanguage = language)
    }

    fun updateSpeechRate(rate: Float) {
        val bounded = rate.coerceIn(0.5f, 2.0f)
        prefs.edit().putFloat("speech_rate", bounded).apply()
        _preferences.value = _preferences.value.copy(speechRate = bounded)
    }

    fun updateHapticFeedback(enabled: Boolean) {
        prefs.edit().putBoolean("haptic", enabled).apply()
        _preferences.value = _preferences.value.copy(hapticFeedback = enabled)
    }

    fun resetToDefaults() {
        prefs.edit().clear().apply()
        _preferences.value = AccessibilityPreferences()
    }
}
