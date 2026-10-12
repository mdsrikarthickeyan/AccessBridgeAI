package com.example.model

enum class AppLanguage(val code: String, val displayName: String, val localName: String) {
    ENGLISH("en", "English", "English"),
    HINDI("hi", "Hindi", "हिन्दी"),
    TAMIL("ta", "Tamil", "தமிழ்"),
    TELUGU("te", "Telugu", "తెలుగు"),
    KANNADA("kn", "Kannada", "ಕನ್ನಡ"),
    BENGALI("bn", "Bengali", "বাংলা"),
    MARATHI("mr", "Marathi", "मराठी")
}

data class AccessibilityPreferences(
    val highContrast: Boolean = false,
    val fontScale: Float = 1.0f, // 1.0f, 1.15f, 1.3f, 1.5f
    val dyslexiaSpacing: Boolean = false,
    val reducedMotion: Boolean = false,
    val preferredLanguage: AppLanguage = AppLanguage.ENGLISH,
    val speechRate: Float = 1.0f, // 0.75f, 1.0f, 1.25f
    val hapticFeedback: Boolean = true,
    val highContrastFlashcards: Boolean = true
)
