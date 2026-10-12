package com.example.model

enum class BarrierCategory(val label: String) {
    BROKEN_RAMP("Broken or Steep Ramp"),
    BLOCKED_TACTILE("Blocked Tactile Paving"),
    ELEVATOR_OUT("Out of Order Elevator / Lift"),
    INACCESSIBLE_ENTRANCE("Inaccessible Steps / Narrow Gate"),
    MISSING_SIGN_ASSIST("No Sign Language Support"),
    ACCESSIBLE_TOILET_LOCKED("Accessible Toilet Locked / Damaged"),
    SENSORY_HAZARD("Sensory / Loud Alarm Hazard")
}

enum class BarrierSeverity(val label: String, val levelOrder: Int) {
    LOW("Minor Inconvenience", 1),
    MEDIUM("Noticeable Obstacle", 2),
    HIGH("Blocks Access Completely", 3),
    URGENT("Immediate Safety Risk", 4)
}

enum class BarrierStatus(val label: String, val description: String) {
    SUBMITTED("Submitted", "Report received and queued for verification"),
    UNDER_REVIEW("Under Review", "Facility administration contacted"),
    IN_PROGRESS("In Progress", "Repair or accommodation scheduled"),
    RESOLVED("Resolved", "Barrier removed and facility verified accessible")
}

data class BarrierReport(
    val id: String,
    val title: String,
    val category: BarrierCategory,
    val locationName: String,
    val city: String,
    val specificLocationDetails: String,
    val severity: BarrierSeverity,
    val status: BarrierStatus,
    val dateReported: String,
    val statusNotes: String,
    val resolutionEstimate: String? = null
)
