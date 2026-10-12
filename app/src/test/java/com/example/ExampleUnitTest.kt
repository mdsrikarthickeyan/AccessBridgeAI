package com.example

import com.example.data.SampleDataProvider
import com.example.data.local.BarrierReportEntity
import com.example.model.AccessibilityPreferences
import com.example.model.AppLanguage
import com.example.model.BarrierCategory
import com.example.model.BarrierReport
import com.example.model.BarrierSeverity
import com.example.model.BarrierStatus
import com.example.model.NoticeCategory
import com.example.service.GeminiVisionService
import com.example.service.NoticeExplainerService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun noticeExplainer_generatesPlainSummary() {
        val sample = "Elevator undergoing maintenance from 15th October. Buggy assistance at Gate 3."
        val result = NoticeExplainerService.explainNoticeText(sample, NoticeCategory.TRANSIT)

        assertNotNull(result.id)
        assertTrue(result.plainSummary.isNotEmpty())
        assertTrue(result.keyPoints.isNotEmpty())
        assertTrue(result.actionRequired.isNotEmpty())
    }

    @Test
    fun preferences_defaultValuesAreValid() {
        val prefs = AccessibilityPreferences()
        assertEquals(false, prefs.highContrast)
        assertEquals(1.0f, prefs.fontScale, 0.01f)
        assertEquals(AppLanguage.ENGLISH, prefs.preferredLanguage)
    }

    @Test
    fun communicationCard_providesLanguageTranslations() {
        val waterCard = SampleDataProvider.sampleCommCards.first { it.id == "comm-water-01" }
        assertEquals("I need drinking water, please.", waterCard.getTextForLanguage(AppLanguage.ENGLISH))
        assertTrue(waterCard.getTextForLanguage(AppLanguage.HINDI).contains("पानी"))
        assertTrue(waterCard.getTextForLanguage(AppLanguage.TAMIL).contains("தண்ணீர்"))
    }

    @Test
    fun barrierReport_entityConversionPreservesFields() {
        val report = BarrierReport(
            id = "test-001",
            title = "Broken Ramp",
            category = BarrierCategory.BROKEN_RAMP,
            locationName = "Platform 1",
            city = "Delhi",
            specificLocationDetails = "Loose ramp tile",
            severity = BarrierSeverity.HIGH,
            status = BarrierStatus.UNDER_REVIEW,
            dateReported = "11 Oct 2026",
            statusNotes = "Inspection pending",
            resolutionEstimate = "2 days"
        )

        val entity = BarrierReportEntity.fromModel(report)
        val converted = entity.toModel()

        assertEquals(report.id, converted.id)
        assertEquals(report.title, converted.title)
        assertEquals(report.category, converted.category)
        assertEquals(report.status, converted.status)
        assertEquals(report.severity, converted.severity)
    }

    @Test
    fun geminiVision_answersDocumentQuestionsLocallyWhenUnconfigured() = runBlocking {
        val docText = "Counter 4A opens from 9:00 AM to 2:00 PM for wheelchair tokens. Helpline: 011-26588500."
        val summary = "Tokens available at Counter 4A."
        val question = "What is the contact number?"

        val result = GeminiVisionService.answerFollowUpQuestion(docText, summary, question)
        assertTrue(result.isSuccess)
        val answer = result.getOrThrow()
        assertTrue(answer.contains("011-26588500"))
    }

    @Test
    fun geminiVision_handlesMissingInformationFactually() = runBlocking {
        val docText = "Library reading hall is open on second floor."
        val summary = "Library is on second floor."
        val question = "What is the phone number?"

        val result = GeminiVisionService.answerFollowUpQuestion(docText, summary, question)
        assertTrue(result.isSuccess)
        val answer = result.getOrThrow()
        assertTrue(answer.contains("No phone number") || answer.contains("does not specify"))
    }

    @Test
    fun accessiblePlace_verificationStatusAndAttributes_areAccurate() {
        val majestic = SampleDataProvider.samplePlaces.first { it.id == "place-majestic-01" }
        assertEquals(com.example.model.VerificationStatus.VERIFIED, majestic.verificationStatus)
        assertTrue(majestic.wheelchairRamp)
        assertTrue(majestic.stepFreeEntrance)
        assertTrue(majestic.tactilePaving)
        assertTrue(majestic.audioAssistance)
        assertTrue(majestic.audioAnnouncements)
        assertEquals("080-22945100", majestic.assistanceDeskPhone)

        val outdatedPlace = SampleDataProvider.samplePlaces.first { it.id == "place-delhi-publib-07" }
        assertEquals(com.example.model.VerificationStatus.OUTDATED, outdatedPlace.verificationStatus)
        assertEquals(false, outdatedPlace.liftAvailable)

        val uncheckedPlace = SampleDataProvider.samplePlaces.first { it.id == "place-bengaluru-collectorate-08" }
        assertEquals(com.example.model.VerificationStatus.NOT_YET_CHECKED, uncheckedPlace.verificationStatus)
    }

    @Test
    fun accessiblePlace_matchScoreCalculatesCorrectly() {
        val aiims = SampleDataProvider.samplePlaces.first { it.id == "place-aiims-02" }
        val score = aiims.matchScoreForPreferences(
            needsRamp = true,
            needsTactile = true,
            needsLift = true,
            needsToilet = true,
            needsBraille = true,
            needsAudio = true,
            needsParking = true
        )
        // High score because AIIMS OPD is fully audited and equipped
        assertTrue(score >= 15)
    }
}
