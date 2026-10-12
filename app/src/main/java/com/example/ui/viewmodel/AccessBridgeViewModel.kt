package com.example.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.PreferencesRepository
import com.example.data.SampleDataProvider
import com.example.data.local.AppDatabase
import com.example.data.local.BarrierReportEntity
import com.example.data.local.SavedNoticeEntity
import com.example.model.AccessibilityPreferences
import com.example.model.AccessiblePlace
import com.example.model.AppLanguage
import com.example.model.BarrierCategory
import com.example.model.BarrierReport
import com.example.model.BarrierSeverity
import com.example.model.BarrierStatus
import com.example.model.CommCategory
import com.example.model.CommunicationCard
import com.example.model.DocumentQA
import com.example.model.ExplainedDocument
import com.example.model.NoticeCategory
import com.example.model.NoticeItem
import com.example.model.PlaceType
import com.example.service.CommunicationTranslationService
import com.example.service.DocumentOcrService
import com.example.service.GeminiVisionService
import com.example.service.NoticeExplainerService
import com.example.service.SpeechRecognitionState
import com.example.service.SpeechToTextManager
import com.example.service.TranslationResult
import com.example.service.TtsManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

enum class AppScreen(val label: String) {
    WELCOME("Welcome"),
    HOME("Home"),
    SCAN_EXPLAIN("Scan & Explain"),
    PLACES("Accessible Places"),
    COMMUNICATE("Help Me Communicate"),
    REPORTS("Barrier Reports"),
    PREFERENCES("Accessibility"),
    HELP("Help & Rights")
}

data class RecentlyUsedItem(
    val title: String,
    val subtitle: String,
    val targetScreen: AppScreen,
    val timestamp: String
)

class AccessBridgeViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val barrierReportDao = db.barrierReportDao()
    private val savedNoticeDao = db.savedNoticeDao()
    private val preferencesRepository = PreferencesRepository(application)
    val ttsManager = TtsManager(application)
    val speechToTextManager = SpeechToTextManager(application)
    val translationService = CommunicationTranslationService()

    // Current Screen
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Screen Backstack
    private val screenStack = mutableListOf<AppScreen>()

    // Preferences
    val preferences: StateFlow<AccessibilityPreferences> = preferencesRepository.preferences

    // TTS Speaking State
    val isSpeaking: StateFlow<Boolean> = ttsManager.isSpeaking

    // Recently Used Activities
    private val _recentlyUsed = MutableStateFlow<List<RecentlyUsedItem>>(emptyList())
    val recentlyUsed: StateFlow<List<RecentlyUsedItem>> = _recentlyUsed.asStateFlow()

    // 1. Scan & Explain State
    private val _notices = MutableStateFlow<List<NoticeItem>>(SampleDataProvider.sampleNotices)
    val notices: StateFlow<List<NoticeItem>> = _notices.asStateFlow()

    private val _currentExplainedDoc = MutableStateFlow<ExplainedDocument?>(
        SampleDataProvider.sampleNotices.firstOrNull()?.let { noticeToExplainedDoc(it) }
    )
    val currentExplainedDoc: StateFlow<ExplainedDocument?> = _currentExplainedDoc.asStateFlow()

    private val _isProcessingNotice = MutableStateFlow(false)
    val isProcessingNotice: StateFlow<Boolean> = _isProcessingNotice.asStateFlow()

    private val _processingNoticeStep = MutableStateFlow("Reading document text...")
    val processingNoticeStep: StateFlow<String> = _processingNoticeStep.asStateFlow()

    private val _documentError = MutableStateFlow<String?>(null)
    val documentError: StateFlow<String?> = _documentError.asStateFlow()

    private val _isAnsweringQuestion = MutableStateFlow(false)
    val isAnsweringQuestion: StateFlow<Boolean> = _isAnsweringQuestion.asStateFlow()

    private val _isTranslating = MutableStateFlow(false)
    val isTranslating: StateFlow<Boolean> = _isTranslating.asStateFlow()

    private val _noticeCategoryFilter = MutableStateFlow<NoticeCategory?>(null)
    val noticeCategoryFilter: StateFlow<NoticeCategory?> = _noticeCategoryFilter.asStateFlow()

    private val _showOnlySavedNotices = MutableStateFlow(false)
    val showOnlySavedNotices: StateFlow<Boolean> = _showOnlySavedNotices.asStateFlow()

    // 2. Accessible Places State
    private val _places = MutableStateFlow<List<AccessiblePlace>>(SampleDataProvider.samplePlaces)
    val places: StateFlow<List<AccessiblePlace>> = _places.asStateFlow()

    private val _selectedPlace = MutableStateFlow<AccessiblePlace?>(null)
    val selectedPlace: StateFlow<AccessiblePlace?> = _selectedPlace.asStateFlow()

    private val _placesSearchQuery = MutableStateFlow("")
    val placesSearchQuery: StateFlow<String> = _placesSearchQuery.asStateFlow()

    private val _selectedPlaceType = MutableStateFlow(PlaceType.ALL)
    val selectedPlaceType: StateFlow<PlaceType> = _selectedPlaceType.asStateFlow()

    private val _selectedCity = MutableStateFlow<String?>("All Cities")
    val selectedCity: StateFlow<String?> = _selectedCity.asStateFlow()

    private val _filterWheelchairOnly = MutableStateFlow(false)
    val filterWheelchairOnly: StateFlow<Boolean> = _filterWheelchairOnly.asStateFlow()

    private val _filterTactileOnly = MutableStateFlow(false)
    val filterTactileOnly: StateFlow<Boolean> = _filterTactileOnly.asStateFlow()

    private val _bookmarkedPlaceIds = MutableStateFlow<Set<String>>(emptySet())
    val bookmarkedPlaceIds: StateFlow<Set<String>> = _bookmarkedPlaceIds.asStateFlow()

    // 3. Communicate State
    private val _commCards = MutableStateFlow<List<CommunicationCard>>(SampleDataProvider.sampleCommCards)
    val commCards: StateFlow<List<CommunicationCard>> = _commCards.asStateFlow()

    private val _selectedCommCategory = MutableStateFlow<CommCategory?>(null)
    val selectedCommCategory: StateFlow<CommCategory?> = _selectedCommCategory.asStateFlow()

    private val _commSearchQuery = MutableStateFlow("")
    val commSearchQuery: StateFlow<String> = _commSearchQuery.asStateFlow()

    private val _customCommInput = MutableStateFlow("")
    val customCommInput: StateFlow<String> = _customCommInput.asStateFlow()

    private val _flashcardToShow = MutableStateFlow<CommunicationCard?>(null)
    val flashcardToShow: StateFlow<CommunicationCard?> = _flashcardToShow.asStateFlow()

    // Speech Recognition & Translation State
    val speechRecognitionState: StateFlow<SpeechRecognitionState> = speechToTextManager.state
    val isListeningToSpeech: StateFlow<Boolean> = speechToTextManager.isListening

    private val _recognizedSpeechText = MutableStateFlow("")
    val recognizedSpeechText: StateFlow<String> = _recognizedSpeechText.asStateFlow()

    private val _translatedCommResult = MutableStateFlow<TranslationResult.Success?>(null)
    val translatedCommResult: StateFlow<TranslationResult.Success?> = _translatedCommResult.asStateFlow()

    private val _translationError = MutableStateFlow<String?>(null)
    val translationError: StateFlow<String?> = _translationError.asStateFlow()

    // 4. Barrier Reports State
    private val _barrierReports = MutableStateFlow<List<BarrierReport>>(SampleDataProvider.sampleReports)
    val barrierReports: StateFlow<List<BarrierReport>> = _barrierReports.asStateFlow()

    private val _selectedReportStatusFilter = MutableStateFlow<BarrierStatus?>(null)
    val selectedReportStatusFilter: StateFlow<BarrierStatus?> = _selectedReportStatusFilter.asStateFlow()

    private val _isReportDialogOpen = MutableStateFlow(false)
    val isReportDialogOpen: StateFlow<Boolean> = _isReportDialogOpen.asStateFlow()

    private val _prefilledLocationForReport = MutableStateFlow<Pair<String, String>?>(null)
    val prefilledLocationForReport: StateFlow<Pair<String, String>?> = _prefilledLocationForReport.asStateFlow()

    // User feedback message banner
    private val _feedbackMessage = MutableStateFlow<String?>(null)
    val feedbackMessage: StateFlow<String?> = _feedbackMessage.asStateFlow()

    init {
        // Observe saved reports from local database
        viewModelScope.launch {
            barrierReportDao.getAllReports().collect { entityList ->
                val localReports = entityList.map { it.toModel() }
                val combined = (localReports + SampleDataProvider.sampleReports).distinctBy { it.id }
                _barrierReports.value = combined
            }
        }

        // Observe saved notices
        viewModelScope.launch {
            savedNoticeDao.getAllSavedNotices().collect { entityList ->
                val savedModels = entityList.map { it.toModel() }
                val initial = SampleDataProvider.sampleNotices.map { sample ->
                    val isSaved = savedModels.any { it.id == sample.id }
                    sample.copy(isSaved = isSaved)
                }
                val extras = savedModels.filter { sm -> SampleDataProvider.sampleNotices.none { it.id == sm.id } }
                _notices.value = initial + extras
            }
        }
    }

    private fun noticeToExplainedDoc(notice: NoticeItem): ExplainedDocument {
        return ExplainedDocument(
            id = notice.id,
            title = notice.title,
            category = notice.category,
            originalExtractedText = notice.originalText,
            simpleExplanation = notice.plainSummary,
            datesAndTimes = listOfNotNull(notice.deadline?.let { "Date / Deadline: $it" }),
            directionsAndRooms = listOf("Platform / Main Gate assistance counter"),
            requiredDocuments = listOf("Disability Identity Card / UDID or Photo ID"),
            warnings = listOf("Arrive at least 30 minutes in advance for guided assistance"),
            contactInfo = notice.emergencyContact,
            isAiAnalyzed = true,
            aiProviderNote = "Verified Accessibility Circular",
            isSaved = notice.isSaved,
            dateAdded = notice.dateAdded
        )
    }

    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            screenStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        return if (screenStack.isNotEmpty()) {
            _currentScreen.value = screenStack.removeAt(screenStack.lastIndex)
            true
        } else if (_currentScreen.value != AppScreen.HOME) {
            _currentScreen.value = AppScreen.HOME
            true
        } else {
            false
        }
    }

    private fun addRecentlyUsed(title: String, subtitle: String, screen: AppScreen) {
        val now = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
        val item = RecentlyUsedItem(title, subtitle, screen, now)
        _recentlyUsed.value = (listOf(item) + _recentlyUsed.value.filter { it.title != title }).take(4)
    }

    fun clearRecentlyUsed() {
        _recentlyUsed.value = emptyList()
        _feedbackMessage.value = "Recently used history cleared."
    }

    // --- Scan & Explain Actions ---
    fun selectSampleNotice(notice: NoticeItem) {
        _currentExplainedDoc.value = noticeToExplainedDoc(notice)
        _documentError.value = null
        addRecentlyUsed("Notice: ${notice.title}", notice.category.title, AppScreen.SCAN_EXPLAIN)
    }

    fun setNoticeCategoryFilter(category: NoticeCategory?) {
        _noticeCategoryFilter.value = category
    }

    fun toggleShowOnlySavedNotices() {
        _showOnlySavedNotices.value = !_showOnlySavedNotices.value
    }

    fun processDocument(
        imageUri: Uri?,
        rawText: String?,
        category: NoticeCategory,
        title: String? = null
    ) {
        viewModelScope.launch {
            _isProcessingNotice.value = true
            _documentError.value = null
            _processingNoticeStep.value = "Reading document text..."
            delay(350)
            _processingNoticeStep.value = "Identifying dates, directions & instructions..."
            delay(350)
            _processingNoticeStep.value = "Simplifying into plain words..."

            val result = DocumentOcrService.processDocumentInput(
                context = getApplication(),
                imageUri = imageUri,
                rawTextInput = rawText,
                category = category,
                customTitle = title
            )

            _isProcessingNotice.value = false

            if (result.isSuccess) {
                val doc = result.getOrThrow()
                _currentExplainedDoc.value = doc
                _documentError.value = null
                _feedbackMessage.value = "Document read and explained in simple words."
                addRecentlyUsed("Scanned Notice: ${doc.title}", "Scan & Explain", AppScreen.SCAN_EXPLAIN)
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "We couldn't read that file. Please try again."
                _documentError.value = errorMsg
            }
        }
    }

    fun translateCurrentDocument(targetLanguage: AppLanguage) {
        val doc = _currentExplainedDoc.value ?: return
        if (targetLanguage == AppLanguage.ENGLISH) {
            _currentExplainedDoc.value = doc.copy(
                translatedExplanation = null,
                activeLanguage = AppLanguage.ENGLISH
            )
            return
        }

        viewModelScope.launch {
            _isTranslating.value = true
            val result = GeminiVisionService.translateText(doc.simpleExplanation, targetLanguage)
            _isTranslating.value = false

            if (result.isSuccess) {
                _currentExplainedDoc.value = doc.copy(
                    translatedExplanation = result.getOrThrow(),
                    activeLanguage = targetLanguage
                )
                _feedbackMessage.value = "Translated to ${targetLanguage.displayName}."
            } else {
                _feedbackMessage.value = "Translation service is currently busy. Showing original explanation."
            }
        }
    }

    fun askFollowUpQuestion(question: String) {
        val doc = _currentExplainedDoc.value ?: return
        if (question.isBlank()) return

        viewModelScope.launch {
            _isAnsweringQuestion.value = true
            val result = GeminiVisionService.answerFollowUpQuestion(
                documentOriginalText = doc.originalExtractedText,
                documentSummary = doc.simpleExplanation,
                question = question
            )
            _isAnsweringQuestion.value = false

            val answer = if (result.isSuccess) {
                result.getOrThrow()
            } else {
                "We couldn't process this question right now. Please refer to the key points above."
            }

            val newQa = DocumentQA(
                id = UUID.randomUUID().toString().take(6),
                question = question.trim(),
                answer = answer,
                timestamp = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
            )

            _currentExplainedDoc.value = doc.copy(
                followUpQa = doc.followUpQa + newQa
            )
        }
    }

    fun resetScanToNew() {
        _currentExplainedDoc.value = null
        _documentError.value = null
    }

    fun toggleSaveCurrentDocument() {
        val doc = _currentExplainedDoc.value ?: return
        viewModelScope.launch {
            if (doc.isSaved) {
                val entity = SavedNoticeEntity(
                    id = doc.id,
                    title = doc.title,
                    categoryName = doc.category.name,
                    originalText = doc.originalExtractedText,
                    plainSummary = doc.simpleExplanation,
                    keyPointsJson = doc.datesAndTimes.joinToString("|||"),
                    actionRequired = doc.directionsAndRooms.firstOrNull() ?: "Visit support counter",
                    deadline = doc.datesAndTimes.firstOrNull(),
                    emergencyContact = doc.contactInfo,
                    isVerified = true,
                    dateAdded = doc.dateAdded
                )
                savedNoticeDao.deleteNotice(entity)
                _currentExplainedDoc.value = doc.copy(isSaved = false)
                _feedbackMessage.value = "Notice removed from saved items."
            } else {
                val entity = SavedNoticeEntity(
                    id = doc.id,
                    title = doc.title,
                    categoryName = doc.category.name,
                    originalText = doc.originalExtractedText,
                    plainSummary = doc.simpleExplanation,
                    keyPointsJson = doc.datesAndTimes.joinToString("|||"),
                    actionRequired = doc.directionsAndRooms.firstOrNull() ?: "Visit support counter",
                    deadline = doc.datesAndTimes.firstOrNull(),
                    emergencyContact = doc.contactInfo,
                    isVerified = true,
                    dateAdded = doc.dateAdded
                )
                savedNoticeDao.insertNotice(entity)
                _currentExplainedDoc.value = doc.copy(isSaved = true)
                _feedbackMessage.value = "Notice saved for offline reading."
            }
        }
    }

    // --- Places Actions ---
    fun selectPlace(place: AccessiblePlace?) {
        _selectedPlace.value = place
        if (place != null) {
            addRecentlyUsed("Place: ${place.name}", place.city, AppScreen.PLACES)
        }
    }

    fun setPlacesSearchQuery(query: String) {
        _placesSearchQuery.value = query
    }

    fun setPlaceTypeFilter(type: PlaceType) {
        _selectedPlaceType.value = type
    }

    fun setCityFilter(city: String?) {
        _selectedCity.value = city
    }

    fun toggleWheelchairFilter() {
        _filterWheelchairOnly.value = !_filterWheelchairOnly.value
    }

    fun toggleTactileFilter() {
        _filterTactileOnly.value = !_filterTactileOnly.value
    }

    fun toggleBookmarkPlace(placeId: String) {
        val current = _bookmarkedPlaceIds.value
        _bookmarkedPlaceIds.value = if (current.contains(placeId)) {
            _feedbackMessage.value = "Place removed from bookmarks."
            current - placeId
        } else {
            _feedbackMessage.value = "Place saved to bookmarks."
            current + placeId
        }
    }

    fun reportBarrierAtPlace(place: AccessiblePlace) {
        _selectedPlace.value = null
        _prefilledLocationForReport.value = Pair(place.name, place.city)
        _currentScreen.value = AppScreen.REPORTS
        _isReportDialogOpen.value = true
    }

    // --- Communication Actions ---
    fun selectCommCategory(category: CommCategory?) {
        _selectedCommCategory.value = category
    }

    fun setCommSearchQuery(query: String) {
        _commSearchQuery.value = query
    }

    fun setCustomCommInput(text: String) {
        _customCommInput.value = text
    }

    fun showFlashcard(card: CommunicationCard?) {
        _flashcardToShow.value = card
        if (card != null) {
            addRecentlyUsed("Communication Card", card.englishText.take(25) + "...", AppScreen.COMMUNICATE)
        }
    }

    fun addCustomPhrase(english: String, category: CommCategory) {
        val newCard = CommunicationCard(
            id = "custom-${UUID.randomUUID().toString().take(6)}",
            category = category,
            englishText = english.trim(),
            hindiText = english.trim(),
            tamilText = english.trim(),
            teluguText = english.trim(),
            kannadaText = english.trim(),
            iconName = "chat",
            phoneticGuide = null
        )
        _commCards.value = listOf(newCard) + _commCards.value
        _feedbackMessage.value = "New phrase added to your cards."
    }

    fun updateCommunicationCard(cardId: String, updatedEnglish: String) {
        _commCards.value = _commCards.value.map { card ->
            if (card.id == cardId) {
                card.copy(englishText = updatedEnglish.trim())
            } else {
                card
            }
        }
        _feedbackMessage.value = "Phrase updated."
    }

    // Speech-to-Text Actions
    fun startSpeechToText(language: AppLanguage? = null) {
        val lang = language ?: preferences.value.preferredLanguage
        _translationError.value = null
        speechToTextManager.startListening(lang) { recognizedText ->
            _recognizedSpeechText.value = recognizedText
            addRecentlyUsed("Speech-to-Text", recognizedText.take(25) + "...", AppScreen.COMMUNICATE)
        }
    }

    fun stopSpeechToText() {
        speechToTextManager.stopListening()
    }

    fun clearSpeechToText() {
        speechToTextManager.resetState()
        _recognizedSpeechText.value = ""
    }

    fun setRecognizedSpeechText(text: String) {
        _recognizedSpeechText.value = text
    }

    // Translation Actions
    fun translateMessage(
        text: String,
        sourceLang: AppLanguage = AppLanguage.ENGLISH,
        targetLang: AppLanguage = preferences.value.preferredLanguage
    ) {
        _translationError.value = null
        when (val result = translationService.translate(text, sourceLang, targetLang)) {
            is TranslationResult.Success -> {
                _translatedCommResult.value = result
                addRecentlyUsed("Translated Message", result.translatedText.take(25) + "...", AppScreen.COMMUNICATE)
            }
            is TranslationResult.Error -> {
                _translationError.value = result.message
            }
        }
    }

    fun updateTranslatedText(editedText: String) {
        val current = _translatedCommResult.value ?: return
        _translatedCommResult.value = current.copy(translatedText = editedText)
    }

    fun clearTranslation() {
        _translatedCommResult.value = null
        _translationError.value = null
    }

    fun speakText(text: String, language: AppLanguage? = null) {
        val lang = language ?: preferences.value.preferredLanguage
        ttsManager.speak(text, lang, preferences.value.speechRate)
    }

    fun stopSpeaking() {
        ttsManager.stop()
    }

    fun testSpeech() {
        val lang = preferences.value.preferredLanguage
        val phrase = when (lang) {
            AppLanguage.HINDI -> "नमस्ते, एक्सेसब्रिज ध्वनि परीक्षण सफल है।"
            AppLanguage.TAMIL -> "வணக்கம், குரல் சோதனை வெற்றிகரமாக முடிந்தது."
            AppLanguage.TELUGU -> "నమస్కారం, వాయిస్ పరీక్ష పూర్తయింది."
            AppLanguage.KANNADA -> "ನಮಸ್ಕಾರ, ಧ್ವನಿ ಪರೀಕ್ಷೆ ಯಶಸ್ವಿಯಾಗಿದೆ."
            else -> "Hello, voice reading test completed successfully."
        }
        ttsManager.speak(phrase, lang, preferences.value.speechRate)
    }

    // --- Barrier Reports Actions ---
    fun openReportDialog() {
        _isReportDialogOpen.value = true
    }

    fun closeReportDialog() {
        _isReportDialogOpen.value = false
        _prefilledLocationForReport.value = null
    }

    fun setReportStatusFilter(status: BarrierStatus?) {
        _selectedReportStatusFilter.value = status
    }

    fun submitNewBarrierReport(
        title: String,
        category: BarrierCategory,
        locationName: String,
        city: String,
        details: String,
        severity: BarrierSeverity
    ) {
        viewModelScope.launch {
            val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
            val newReport = BarrierReport(
                id = "rep-${UUID.randomUUID().toString().take(6)}",
                title = title.trim(),
                category = category,
                locationName = locationName.trim(),
                city = city.trim(),
                specificLocationDetails = details.trim(),
                severity = severity,
                status = BarrierStatus.SUBMITTED,
                dateReported = dateStr,
                statusNotes = "Report received and queued for physical review by regional accessibility team.",
                resolutionEstimate = "Initial review within 48 hours"
            )

            barrierReportDao.insertReport(BarrierReportEntity.fromModel(newReport))
            _isReportDialogOpen.value = false
            _prefilledLocationForReport.value = null
            _feedbackMessage.value = "Your report has been submitted for review."
            addRecentlyUsed("Barrier Report: $title", locationName, AppScreen.REPORTS)
        }
    }

    fun deleteBarrierReport(report: BarrierReport) {
        viewModelScope.launch {
            barrierReportDao.deleteReport(BarrierReportEntity.fromModel(report))
            _barrierReports.value = _barrierReports.value.filter { it.id != report.id }
            _feedbackMessage.value = "Report removed."
        }
    }

    // --- Preferences Actions ---
    fun setHighContrast(enabled: Boolean) {
        preferencesRepository.updateHighContrast(enabled)
    }

    fun setFontScale(scale: Float) {
        preferencesRepository.updateFontScale(scale)
    }

    fun setDyslexiaSpacing(enabled: Boolean) {
        preferencesRepository.updateDyslexiaSpacing(enabled)
    }

    fun setReducedMotion(enabled: Boolean) {
        preferencesRepository.updateReducedMotion(enabled)
    }

    fun setPreferredLanguage(language: AppLanguage) {
        preferencesRepository.updatePreferredLanguage(language)
    }

    fun setSpeechRate(rate: Float) {
        preferencesRepository.updateSpeechRate(rate)
    }

    fun setHapticFeedback(enabled: Boolean) {
        preferencesRepository.updateHapticFeedback(enabled)
    }

    fun resetPreferences() {
        preferencesRepository.resetToDefaults()
        _feedbackMessage.value = "Preferences reset to standard settings."
    }

    fun clearFeedbackMessage() {
        _feedbackMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
        speechToTextManager.destroy()
    }
}
