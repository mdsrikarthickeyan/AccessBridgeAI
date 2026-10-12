package com.example.data

import com.example.model.AccessiblePlace
import com.example.model.BarrierCategory
import com.example.model.BarrierReport
import com.example.model.BarrierSeverity
import com.example.model.BarrierStatus
import com.example.model.CommCategory
import com.example.model.CommunicationCard
import com.example.model.NoticeCategory
import com.example.model.NoticeItem
import com.example.model.PlaceType
import com.example.model.VerificationStatus

object SampleDataProvider {

    val sampleNotices = listOf(
        NoticeItem(
            id = "notice-dmrc-01",
            title = "DMRC Metro Wheelchair Shuttle & Lift Modernization",
            category = NoticeCategory.TRANSIT,
            originalText = """
                DELHI METRO RAIL CORPORATION (DMRC)
                PUBLIC NOTICE NO. 2026/DMRC/ACC-09
                Subject: Upgradation of Elevators and Free Battery Buggy Assistance at Rajiv Chowk Station.
                All passengers requiring wheelchair assistance, crutches, or senior citizens with restricted mobility are notified that Platform 1 & 2 elevators will undergo scheduled sensor recalibration between 15th October and 20th October 2026.
                During this period, dedicated electric battery buggies with trained station attendants will operate continuously between Gate 3 and Gate 7 concourses free of charge.
                Passengers may contact the Station Control Room directly via intercom or by dialling 011-23417910 or contacting security at AFC gates.
                Wheelchair users can also request escorted interchange to Yellow / Blue Lines.
            """.trimIndent(),
            plainSummary = "Rajiv Chowk Metro station is upgrading its elevators from 15 to 20 October. During this time, free electric buggies and helper staff are available at Gate 3 and 7 to take wheelchair users and senior citizens to their platforms.",
            keyPoints = listOf(
                "Elevators will undergo maintenance from 15th to 20th October",
                "Free battery buggies with helper attendants are running between Gate 3 and Gate 7",
                "Escorts are available to help you transfer between Blue and Yellow lines",
                "Call Station Control at 011-23417910 or ask security at any entry gate"
            ),
            actionRequired = "If you use a wheelchair or need walking assistance, enter through Gate 3 or Gate 7 and ask the attendant for buggy transfer.",
            deadline = "15 Oct - 20 Oct 2026",
            emergencyContact = "011-23417910 (Station Control)",
            isVerified = true,
            dateAdded = "10 Oct 2026"
        ),
        NoticeItem(
            id = "notice-aiims-02",
            title = "AIIMS OPD Accessibility & Braille Report Guidelines",
            category = NoticeCategory.HOSPITAL,
            originalText = """
                ALL INDIA INSTITUTE OF MEDICAL SCIENCES (AIIMS)
                OFFICE OF THE MEDICAL SUPERINTENDENT - CIRCULAR 114
                Subject: Dedicated Accessibility Desk and Assistive Reading Facilities at Rajkumari Amrit Kaur OPD.
                Patients with visual impairment, hearing disability, or locomotor challenges can now access fast-track token collection at Counter 4A (Ground Floor, Main Atrium).
                Prescriptions and discharge summaries can be printed in Braille or emailed in accessible text format upon request at Counter 4A.
                Sign language interpreters are stationed in Room 12 between 9:00 AM and 2:00 PM on all weekdays.
                Wheelchairs and stretchers are available at Gate 2 entrance with no security deposit required.
            """.trimIndent(),
            plainSummary = "AIIMS New Delhi has set up Counter 4A on the ground floor for patients with disabilities. You get fast-track tokens, Braille prescription printouts, free wheelchairs at Gate 2, and sign language interpreters in Room 12.",
            keyPoints = listOf(
                "Fast-track token collection at Counter 4A on the ground floor",
                "Braille prints and accessible digital prescriptions available upon request",
                "Sign language interpreters present in Room 12 (9 AM to 2 PM, Mon-Fri)",
                "Free wheelchairs at Gate 2 with zero deposit"
            ),
            actionRequired = "Head directly to Counter 4A in the Main Atrium. Bring your disability card or photo ID for immediate fast-track assistance.",
            deadline = "Ongoing service",
            emergencyContact = "011-26588500 (OPD Helpdesk)",
            isVerified = true,
            dateAdded = "08 Oct 2026"
        ),
        NoticeItem(
            id = "notice-univ-03",
            title = "University Examination Concession & Scribe Policy",
            category = NoticeCategory.COLLEGE,
            originalText = """
                CONTROLLER OF EXAMINATIONS - NOTIFICATION 88/2026
                Subject: Compensatory Time and Scribe Allocation for Semester Exams.
                Pursuant to RPwD Act 2016 norms, students with permanent or temporary disabilities (including visual impairment, cerebral palsy, orthopaedic limitations, and specific learning disabilities like dyslexia) are granted 20 minutes compensatory time per hour of examination.
                Candidates requiring an approved scribe must submit Form Annexure-C along with valid medical certificate to Student Welfare Section by 25th October 2026.
                Accessible screen reader laptops with NVDA software will be arranged in Lab Block 3.
            """.trimIndent(),
            plainSummary = "Students with disabilities or dyslexia receive 20 minutes extra time per hour during semester exams. If you need a scribe (writer) or a computer with NVDA screen reader, submit Form Annexure-C before 25 October.",
            keyPoints = listOf(
                "20 minutes extra time per examination hour",
                "Free scribe assistance available with prior registration",
                "Screen reader computers with NVDA available in Lab Block 3",
                "Deadline to submit scribe request form: 25 October 2026"
            ),
            actionRequired = "Submit Form Annexure-C with your medical or disability certificate to the Student Welfare Section before 25 October 2026.",
            deadline = "25 October 2026",
            emergencyContact = "011-27667000 (Student Welfare)",
            isVerified = true,
            dateAdded = "05 Oct 2026"
        ),
        NoticeItem(
            id = "notice-railway-04",
            title = "Indian Railways Battery Buggy & Low-Height Berth Booking",
            category = NoticeCategory.TRANSIT,
            originalText = """
                MINISTRY OF RAILWAYS - COMMERCIAL CIRCULAR NO. 42
                Subject: Yatri Suvidha Wheelchair and Battery Operated Car booking at Category A1 Stations.
                Divyangjan passengers holding concession certificates can book lower berths under Divyang quota through PRS counters or online portal.
                To arrange free battery car pickup from station porch to coach door at New Delhi, Howrah, CSMT Mumbai, and Chennai Central, call Railway Helpline 139 at least 2 hours before train departure.
                Tactile pathways lead directly from Concourse Entry to Platforms 1 through 16.
            """.trimIndent(),
            plainSummary = "Passengers with disabilities can book lower berths under the reserved quota. You can also book a free battery cart to drive you directly from the station gate to your train coach by calling 139 two hours before your journey.",
            keyPoints = listOf(
                "Reserved lower berth quota available for passengers with disabilities",
                "Free battery car pickup from station porch directly to coach door",
                "Call 139 at least 2 hours in advance to book battery car",
                "Tactile guiding tiles installed along the entire main concourse"
            ),
            actionRequired = "Dial 139 two hours before your train leaves, or visit the Divyangjan Help Booth at Platform 1.",
            deadline = "Call 2 hours before journey",
            emergencyContact = "139 (Rail Madad Helpline)",
            isVerified = true,
            dateAdded = "01 Oct 2026"
        )
    )

    val samplePlaces = listOf(
        AccessiblePlace(
            id = "place-majestic-01",
            name = "Kempegowda Majestic Interchange Metro",
            type = PlaceType.METRO,
            city = "Bengaluru",
            address = "Gubbi Thotadappa Rd, Majestic, Bengaluru 560023",
            latitude = 12.9767,
            longitude = 77.5713,
            verificationStatus = VerificationStatus.VERIFIED,
            lastVerifiedDate = "15 Aug 2026",
            informationSource = "BMRCL Accessibility Audit & Joint Inspection",
            openingHours = "05:00 AM - 11:30 PM",
            entranceDetails = "Step-free ramp entry at Gate A & Gate F with automated wide fare gates (900mm width)",
            stepFreeEntrance = true,
            wheelchairRamp = true,
            liftAvailable = true,
            accessibleToilet = true,
            wheelchairAccessibleRoute = true,
            tactilePaving = true,
            brailleSignage = true,
            audioAssistance = true,
            visualAnnouncements = true,
            accessibleParking = true,
            signLanguageAssistance = true,
            otherFacilities = listOf("Motorized wheelchair assistance", "Low-height ticket counter 3", "Tactile station map at concourse"),
            assistanceDeskPhone = "080-22945100",
            description = "Major transit hub connecting Green and Purple metro lines, KSRTC bus stand, and City Railway Station. Full step-free access across all 4 underground and elevated concourse levels.",
            accessibilityNotes = "Continuous yellow tactile paving from all bus entry gates. Dual elevators at Platform 1 & 2 with braille button panels and multilingual audible chime. Dedicated accessible restroom on Concourse Level 1.",
            nearestTransit = "Direct connection to City Railway Station (SBC)"
        ),
        AccessiblePlace(
            id = "place-aiims-02",
            name = "AIIMS Main Medical Complex & OPD",
            type = PlaceType.HOSPITAL,
            city = "Delhi",
            address = "Sri Aurobindo Marg, Ansari Nagar, New Delhi 110029",
            latitude = 28.5672,
            longitude = 77.2100,
            verificationStatus = VerificationStatus.VERIFIED,
            lastVerifiedDate = "10 Sep 2026",
            informationSource = "Ministry of Health Accessibility Cell & AIIMS Engineering Dept",
            openingHours = "24 Hours (OPD: 08:30 AM - 01:00 PM)",
            entranceDetails = "Covered gentle ramp with handrails on both sides at Gate 2 and Gate 4",
            stepFreeEntrance = true,
            wheelchairRamp = true,
            liftAvailable = true,
            accessibleToilet = true,
            wheelchairAccessibleRoute = true,
            tactilePaving = true,
            brailleSignage = true,
            audioAssistance = true,
            visualAnnouncements = true,
            accessibleParking = true,
            signLanguageAssistance = true,
            otherFacilities = listOf("Free motorized buggies between OPD blocks", "Braille signage on all elevator panels", "UDID priority counter"),
            assistanceDeskPhone = "011-26588500",
            description = "Premier government hospital equipped with step-free ramps, wide automated sliding doors, priority counters, and assistive sign interpreters in the main outpatient building.",
            accessibilityNotes = "Wheelchair station with motorized chairs located at Gate 2. Low-height reception desks at Counter 4A. Tactile directional indicators leading to pharmacy and blood collection.",
            nearestTransit = "AIIMS Metro Station (Yellow Line) Exit 2 (Elevator equipped)"
        ),
        AccessiblePlace(
            id = "place-anna-library-03",
            name = "Anna Centenary Library",
            type = PlaceType.LIBRARY,
            city = "Chennai",
            address = "Gandhi Mandapam Rd, Surya Nagar, Kotturpuram, Chennai 600085",
            latitude = 13.0125,
            longitude = 80.2376,
            verificationStatus = VerificationStatus.VERIFIED,
            lastVerifiedDate = "01 Jul 2026",
            informationSource = "Dept. of Public Libraries Audit",
            openingHours = "09:00 AM - 08:00 PM",
            entranceDetails = "Smooth wide porch ramp with dual height handrails and tactile entry warning tiles",
            stepFreeEntrance = true,
            wheelchairRamp = true,
            liftAvailable = true,
            accessibleToilet = true,
            wheelchairAccessibleRoute = true,
            tactilePaving = true,
            brailleSignage = true,
            audioAssistance = true,
            visualAnnouncements = true,
            accessibleParking = true,
            signLanguageAssistance = false,
            otherFacilities = listOf("Over 3,500 Braille volumes", "50 DAISY digital talking book computers", "Screen magnifier reading stations"),
            assistanceDeskPhone = "044-22201011",
            description = "State-of-the-art public library with an entire dedicated Braille and Talking Books wing on the Ground Floor.",
            accessibilityNotes = "Smooth gentle ramps at main porch. Over 3,000 Braille volumes and 50 DAISY digital audio reading workstations. Spacious accessible washrooms on all 8 reading floors.",
            nearestTransit = "Kotturpuram MRTS Station (700m) / Bus stop at gate"
        ),
        AccessiblePlace(
            id = "place-csmt-04",
            name = "Chhatrapati Shivaji Maharaj Terminus (CSMT)",
            type = PlaceType.METRO,
            city = "Mumbai",
            address = "Fort, Mumbai, Maharashtra 400001",
            latitude = 18.9400,
            longitude = 72.8353,
            verificationStatus = VerificationStatus.REPORTED_BY_VISITOR,
            lastVerifiedDate = "22 Aug 2026",
            informationSource = "Recent passenger report with uploaded entry photos",
            openingHours = "24 Hours Open",
            entranceDetails = "Level entry from suburban booking concourse; ramps installed at main entrance",
            stepFreeEntrance = true,
            wheelchairRamp = true,
            liftAvailable = true,
            accessibleToilet = true,
            wheelchairAccessibleRoute = true,
            tactilePaving = true,
            brailleSignage = false,
            audioAssistance = true,
            visualAnnouncements = true,
            accessibleParking = false,
            signLanguageAssistance = false,
            otherFacilities = listOf("Free battery-operated transit cart", "Priority booking window at suburban hall"),
            assistanceDeskPhone = "022-22620173",
            description = "Historic railway terminus with upgraded step-free access paths, motorized passenger carts, and accessible booking counters at Platform 1.",
            accessibilityNotes = "Level boarding for suburban local trains on Platforms 1 through 7. Free wheelchair service managed by Coolie Helpdesk near suburban ticket booking hall.",
            nearestTransit = "CSMT Metro Line 3 Underground Station (Upcoming fully accessible)"
        ),
        AccessiblePlace(
            id = "place-iitb-05",
            name = "IIT Bombay Central Library & Student Hub",
            type = PlaceType.COLLEGE,
            city = "Mumbai",
            address = "Powai, Mumbai, Maharashtra 400076",
            latitude = 19.1334,
            longitude = 72.9133,
            verificationStatus = VerificationStatus.VERIFIED,
            lastVerifiedDate = "18 Sep 2026",
            informationSource = "IIT Bombay Equal Opportunity Cell (EOC)",
            openingHours = "08:00 AM - 11:00 PM (Exam weeks: 24h)",
            entranceDetails = "Automated sliding glass entrance door with ramp and tactile indicators",
            stepFreeEntrance = true,
            wheelchairRamp = true,
            liftAvailable = true,
            accessibleToilet = true,
            wheelchairAccessibleRoute = true,
            tactilePaving = true,
            brailleSignage = true,
            audioAssistance = false,
            visualAnnouncements = true,
            accessibleParking = true,
            signLanguageAssistance = true,
            otherFacilities = listOf("JAWS screen-reader enabled terminals", "Accessible battery shuttles across campus"),
            assistanceDeskPhone = "022-25768920",
            description = "Fully barrier-free academic hub with wheelchair accessible elevators, screen reader software on student terminals, and dedicated accessible shuttle vans.",
            accessibilityNotes = "Continuous ramps linking lecture theatre halls. Accessible restrooms in all departmental wings. Dedicated Assistive Technology Cell in the Central Library.",
            nearestTransit = "Kanjurmarg Railway Station (3 km) / Auto rickshaw stand at main gate"
        ),
        AccessiblePlace(
            id = "place-hyd-collectorate-06",
            name = "Hyderabad District Collectorate & Citizen Centre",
            type = PlaceType.GOVERNMENT,
            city = "Hyderabad",
            address = "Chirag Ali Lane, Abids, Hyderabad, Telangana 500001",
            latitude = 17.3916,
            longitude = 78.4735,
            verificationStatus = VerificationStatus.REPORTED_BY_VISITOR,
            lastVerifiedDate = "05 Sep 2026",
            informationSource = "Community visitor submission during UDID camp",
            openingHours = "10:00 AM - 05:00 PM (Mon-Sat, Closed 2nd Sat)",
            entranceDetails = "Ramp at the rear entrance near parking; main front steps have steep angle",
            stepFreeEntrance = true,
            wheelchairRamp = true,
            liftAvailable = false,
            accessibleToilet = true,
            wheelchairAccessibleRoute = true,
            tactilePaving = false,
            brailleSignage = false,
            audioAssistance = true,
            visualAnnouncements = false,
            accessibleParking = true,
            signLanguageAssistance = false,
            otherFacilities = listOf("UDID card application desk on Ground Floor", "Wheelchair available in Room 102"),
            assistanceDeskPhone = "040-23202833",
            description = "Citizen public services center for pension verification, disability identity cards (UDID), and revenue certificates with ground floor priority counters.",
            accessibilityNotes = "Ramp at the rear entrance near parking. Counter 1 is reserved exclusively for senior citizens and persons with disabilities. Wheelchair on standby in Room 102.",
            nearestTransit = "Gandhi Bhavan Metro Station (Red Line) - 800m"
        ),
        AccessiblePlace(
            id = "place-delhi-publib-07",
            name = "Delhi Public Library Central Branch",
            type = PlaceType.LIBRARY,
            city = "Delhi",
            address = "Opposite Old Delhi Railway Station, Chandni Chowk, Delhi 110006",
            latitude = 28.6582,
            longitude = 77.2289,
            verificationStatus = VerificationStatus.OUTDATED,
            lastVerifiedDate = "14 Mar 2025",
            informationSource = "Past municipal survey (Over 12 months old)",
            openingHours = "08:30 AM - 07:30 PM",
            entranceDetails = "Side ramp exists; main entrance has 4 stone steps",
            stepFreeEntrance = true,
            wheelchairRamp = true,
            liftAvailable = false,
            accessibleToilet = false,
            wheelchairAccessibleRoute = false,
            tactilePaving = false,
            brailleSignage = true,
            audioAssistance = false,
            visualAnnouncements = false,
            accessibleParking = false,
            signLanguageAssistance = false,
            otherFacilities = listOf("Braille reading section on Ground Floor"),
            assistanceDeskPhone = "011-23951112",
            description = "Historic municipal library with dedicated Braille books and quiet study corners.",
            accessibilityNotes = "Ground floor has Braille materials. Elevator to upper floors is currently undergoing maintenance. Visitors recommend calling ahead.",
            nearestTransit = "Chandni Chowk Metro Station (Yellow Line) - 300m"
        ),
        AccessiblePlace(
            id = "place-bengaluru-collectorate-08",
            name = "Bengaluru Urban Deputy Commissioner Office",
            type = PlaceType.GOVERNMENT,
            city = "Bengaluru",
            address = "K.G. Road, Revenue Complex, Bengaluru, Karnataka 560009",
            latitude = 12.9738,
            longitude = 77.5831,
            verificationStatus = VerificationStatus.NOT_YET_CHECKED,
            lastVerifiedDate = "Newly listed",
            informationSource = "Public directory listing; independent audit scheduled",
            openingHours = "10:00 AM - 05:30 PM (Working days)",
            entranceDetails = "Main road entrance with vehicle drop-off porch",
            stepFreeEntrance = true,
            wheelchairRamp = true,
            liftAvailable = true,
            accessibleToilet = true,
            wheelchairAccessibleRoute = false,
            tactilePaving = false,
            brailleSignage = false,
            audioAssistance = false,
            visualAnnouncements = false,
            accessibleParking = true,
            signLanguageAssistance = false,
            otherFacilities = listOf("Ground floor citizen service counters"),
            assistanceDeskPhone = "080-22211292",
            description = "District administrative headquarters providing citizen certificates and electoral services.",
            accessibilityNotes = "Ground floor ramps verified by public photos; upper floors and restroom facilities not yet formally audited.",
            nearestTransit = "Sir M. Visveshwaraya Metro Station - 450m"
        )
    )

    val sampleCommCards = listOf(
        // Quick Accessibility Phrases requested by users
        CommunicationCard(
            id = "comm-speak-slowly-00",
            category = CommCategory.DAILY_NEEDS,
            englishText = "Please speak slowly.",
            hindiText = "कृपया धीरे बोलें।",
            tamilText = "தயவுசெய்து மெதுவாக பேசுங்கள்.",
            teluguText = "దయచేసి నెమ్మదిగా మాట్లాడండి.",
            kannadaText = "ದಯವಿಟ್ಟು ನಿಧಾನವಾಗಿ ಮಾತನಾಡಿ.",
            iconName = "hearing",
            phoneticGuide = "Kripya dheere bolein"
        ),
        CommunicationCard(
            id = "comm-write-down-00",
            category = CommCategory.DAILY_NEEDS,
            englishText = "Please write that down.",
            hindiText = "कृपया इसे लिखकर दें।",
            tamilText = "தயவுசெய்து அதை எழுதிக் காட்டுங்கள்.",
            teluguText = "దయచేసి దాన్ని రాసి చూపించండి.",
            kannadaText = "ದಯವಿಟ್ಟು ಅದನ್ನು ಬರೆದು ತೋರಿಸಿ.",
            iconName = "edit",
            phoneticGuide = "Kripya likhkar dein"
        ),
        CommunicationCard(
            id = "comm-step-free-00",
            category = CommCategory.TRANSIT,
            englishText = "I need step-free access.",
            hindiText = "मुझे बिना सीढ़ियों वाला (रैंप या लिफ्ट) रास्ता चाहिए।",
            tamilText = "எனக்கு படிகள் இல்லாத பாதை தேவை.",
            teluguText = "నాకు మెట్లు లేని దారి కావాలి.",
            kannadaText = "ನನಗೆ ಮೆಟ್ಟಿಲುಗಳಿಲ್ಲದ ಮಾರ್ಗ ಬೇಕು.",
            iconName = "accessible",
            phoneticGuide = "Bina seedhiyon wala rasta chahiye"
        ),
        CommunicationCard(
            id = "comm-show-where-00",
            category = CommCategory.DIRECTIONS,
            englishText = "Could you show me where to go?",
            hindiText = "क्या आप मुझे रास्ता दिखा सकते हैं?",
            tamilText = "எங்கே செல்ல வேண்டும் என்று எனக்கு காட்ட முடியுமா?",
            teluguText = "నేను ఎక్కడికి వెళ్ళాలో చూపించగలరా?",
            kannadaText = "ನಾನು ಎಲ್ಲಿಗೆ ಹೋಗಬೇಕೆಂದು ತೋರಿಸಬಹುದೇ?",
            iconName = "navigation",
            phoneticGuide = "Rasta dikha sakte hain?"
        ),
        CommunicationCard(
            id = "comm-give-moment-00",
            category = CommCategory.DAILY_NEEDS,
            englishText = "Please give me a moment.",
            hindiText = "कृपया मुझे एक पल का समय दें।",
            tamilText = "தயவுசெய்து எனக்கு ஒரு நிமிடம் அவகாசம் கொடுங்கள்.",
            teluguText = "దయచేసి నాకు కాస్త సమయం ఇవ్వండి.",
            kannadaText = "ದಯವಿಟ್ಟು ನನಗೆ ಸ್ವಲ್ಪ ಸಮಯ ಕೊಡಿ.",
            iconName = "time",
            phoneticGuide = "Mujhe thoda samay dein"
        ),
        CommunicationCard(
            id = "comm-assistance-00",
            category = CommCategory.EMERGENCY,
            englishText = "I need assistance.",
            hindiText = "मुझे सहायता चाहिए।",
            tamilText = "எனக்கு உதவி தேவை.",
            teluguText = "నాకు సహాయం కావాలి.",
            kannadaText = "ನನಗೆ ಸಹಾಯ ಬೇಕು.",
            iconName = "alert",
            phoneticGuide = "Mujhe sahayata chahiye"
        ),
        // Daily Needs
        CommunicationCard(
            id = "comm-water-01",
            category = CommCategory.DAILY_NEEDS,
            englishText = "I need drinking water, please.",
            hindiText = "मुझे पीने का पानी चाहिए, कृपया।",
            tamilText = "எனக்கு குடிக்க தண்ணீர் வேண்டும், தயவுசெய்து.",
            teluguText = "నాకు తాగడానికి నీరు కావాలి, దయచేసి.",
            kannadaText = "ನನಗೆ ಕುಡಿಯಲು ನೀರು ಬೇಕು, ದಯವಿಟ್ಟು.",
            iconName = "water",
            phoneticGuide = "Mujhe peene ka paani chahiye"
        ),
        CommunicationCard(
            id = "comm-restroom-02",
            category = CommCategory.DAILY_NEEDS,
            englishText = "Where is the accessible restroom / toilet?",
            hindiText = "सुलभ शौचालय (टॉयलेट) कहाँ है?",
            tamilText = "கழிப்பறை எங்கே உள்ளது?",
            teluguText = "వాష్ రూమ్ ఎక్కడ ఉంది?",
            kannadaText = "ಶೌಚಾಲಯ ಎಲ್ಲಿದೆ?",
            iconName = "wc",
            phoneticGuide = "Toilet kahan hai?"
        ),
        CommunicationCard(
            id = "comm-charge-03",
            category = CommCategory.DAILY_NEEDS,
            englishText = "Can I charge my motorized wheelchair or phone here?",
            hindiText = "क्या मैं अपनी व्हीलचेयर या फोन यहाँ चार्ज कर सकता हूँ?",
            tamilText = "நான் இங்கு சார்ஜ் செய்யலாமா?",
            teluguText = "నేను ఇక్కడ ఛార్జ్ చేసుకోవచ్చా?",
            kannadaText = "ನಾನು ಇಲ್ಲಿ ಚಾರ್ಜ್ ಮಾಡಬಹುದೇ?",
            iconName = "battery",
            phoneticGuide = "Yahan charging point hai?"
        ),
        // Transit
        CommunicationCard(
            id = "comm-bus-stop-04",
            category = CommCategory.TRANSIT,
            englishText = "Please tell me when the next bus or train arrives.",
            hindiText = "कृपया बताएं अगली बस या ट्रेन कब आएगी।",
            tamilText = "அடுத்த பேருந்து எப்போது வரும் என்று சொல்லுங்கள்.",
            teluguText = "తదుపరి బస్సు ఎప్పుడు వస్తుందో చెప్పండి.",
            kannadaText = "ಮುಂದಿನ ಬಸ್ ಯಾವಾಗ ಬರುತ್ತದೆ ಹೇಳಿ.",
            iconName = "bus",
            phoneticGuide = "Agli bus kab aayegi?"
        ),
        CommunicationCard(
            id = "comm-ramp-request-05",
            category = CommCategory.TRANSIT,
            englishText = "Please lower the wheelchair ramp or open the wide gate.",
            hindiText = "कृपया व्हीलचेयर रैंप खोलें या चौड़ा गेट खोलें।",
            tamilText = "தயவுசெய்து சக்கர நாற்காலி சாய்வுதளத்தை திறக்கவும்.",
            teluguText = "దయచేసి వీల్ చైర్ ర్యాంప్ తెరవండి.",
            kannadaText = "ದಯವಿಟ್ಟು ವೀಲ್‌ಚೇರ್ ರ‍್ಯಾಂಪ್ ತೆರೆಯಿರಿ.",
            iconName = "accessible",
            phoneticGuide = "Kripya ramp khol dein"
        ),
        CommunicationCard(
            id = "comm-ticket-06",
            category = CommCategory.TRANSIT,
            englishText = "I need one ticket with disability concession.",
            hindiText = "मुझे दिव्यांगजन रियायत वाला एक टिकट चाहिए।",
            tamilText = "மாற்றுத்திறனாளி சலுகை கொண்ட ஒரு டிக்கெட் வேண்டும்.",
            teluguText = "నాకు ఒక రాయితీ టికెట్ కావాలి.",
            kannadaText = "ನನಗೆ ಒಂದು ರಿಯಾಯಿತಿ ಟಿಕೆಟ್ ಬೇಕು.",
            iconName = "ticket",
            phoneticGuide = "Ek concession ticket chahiye"
        ),
        // Health
        CommunicationCard(
            id = "comm-medicine-07",
            category = CommCategory.HEALTH,
            englishText = "I need my regular medicine. Here is my prescription.",
            hindiText = "मुझे मेरी दवा चाहिए। यह मेरा पर्चा है।",
            tamilText = "எனக்கு எனது மருந்து வேண்டும். இது எனது மருந்து சீட்டு.",
            teluguText = "నాకు నా మందులు కావాలి. ఇది నా చీటీ.",
            kannadaText = "ನನಗೆ ನನ್ನ ಔಷಧ ಬೇಕು. ಇದು ನನ್ನ ಚೀಟಿ.",
            iconName = "medical",
            phoneticGuide = "Mujhe dawa chahiye"
        ),
        CommunicationCard(
            id = "comm-pain-08",
            category = CommCategory.HEALTH,
            englishText = "I am in pain or feeling dizzy. Please help me sit down.",
            hindiText = "मुझे दर्द या चक्कर आ रहा है। कृपया मुझे बैठने में मदद करें।",
            tamilText = "எனக்கு தலைச்சுற்றலாக உள்ளது. தயவுசெய்து அமர உதவுங்கள்.",
            teluguText = "నాకు కళ్లు తిరుగుతున్నాయి. దయచేసి కూర్చోవడానికి సహాయం చేయండి.",
            kannadaText = "ನನಗೆ ತಲೆತಿರುಗುತ್ತಿದೆ. ದಯವಿಟ್ಟು ಕುಳಿತುಕೊಳ್ಳಲು ಸಹಾಯ ಮಾಡಿ.",
            iconName = "heart",
            phoneticGuide = "Mujhe baithne mein madad karein"
        ),
        CommunicationCard(
            id = "comm-write-09",
            category = CommCategory.HEALTH,
            englishText = "I am deaf or hard of hearing. Please write down what you are saying.",
            hindiText = "मैं सुन नहीं सकता/सकती। कृपया अपनी बात लिखकर बताएं।",
            tamilText = "எனக்கு காது கேளாது. தயவுசெய்து எழுதி காட்டுங்கள்.",
            teluguText = "నేను వినలేను. దయచేసి రాసి చూపించండి.",
            kannadaText = "ನನಗೆ ಕೇಳಿಸುವುದಿಲ್ಲ. ದಯವಿಟ್ಟು ಬರೆದು ತೋರಿಸಿ.",
            iconName = "hearing",
            phoneticGuide = "Kripya likhkar batayein"
        ),
        // Emergency
        CommunicationCard(
            id = "comm-emergency-10",
            category = CommCategory.EMERGENCY,
            englishText = "I need urgent help! Please call my emergency contact.",
            hindiText = "मुझे तुरंत मदद चाहिए! कृपया मेरे आपातकालीन नंबर पर कॉल करें।",
            tamilText = "எனக்கு அவசர உதவி தேவை! எனது குடும்பத்திற்கு அழைக்கவும்.",
            teluguText = "నాకు అత్యవసర సహాయం కావాలి! దయచేసి కాల్ చేయండి.",
            kannadaText = "ನನಗೆ ತುರ್ತು ಸಹಾಯ ಬೇಕು! ದಯವಿಟ್ಟು ಕರೆ ಮಾಡಿ.",
            iconName = "alert",
            phoneticGuide = "Mujhe turant madad chahiye!"
        ),
        CommunicationCard(
            id = "comm-lost-11",
            category = CommCategory.EMERGENCY,
            englishText = "I am visually impaired and lost. Please guide me to the exit.",
            hindiText = "मुझे दिखाई नहीं देता और मैं रास्ता भटक गया/गई हूँ। कृपया निकास द्वार तक ले चलें।",
            tamilText = "எனக்கு பார்வை குறைபாடு உள்ளது. தயவுசெய்து வெளியேறும் வழியைக் காட்டுங்கள்.",
            teluguText = "నాకు కనిపించదు. దయచేసి నన్ను బయటకు తీసుకెళ్లండి.",
            kannadaText = "ನನಗೆ ಕಣ್ಣು ಕಾಣಿಸುವುದಿಲ್ಲ. ದಯವಿಟ್ಟು ಹೊರಗೆ ಹೋಗುವ ದಾರಿ ತೋರಿಸಿ.",
            iconName = "navigation",
            phoneticGuide = "Exit tak le chalein"
        )
    )

    val sampleReports = listOf(
        BarrierReport(
            id = "rep-001",
            title = "Broken Wheelchair Ramp at Connaught Place Metro Gate 6",
            category = BarrierCategory.BROKEN_RAMP,
            locationName = "Rajiv Chowk Metro Station",
            city = "Delhi",
            specificLocationDetails = "Gate 6 sidewalk ramp has broken concrete edge and loose tiles blocking wheels",
            severity = BarrierSeverity.HIGH,
            status = BarrierStatus.IN_PROGRESS,
            dateReported = "09 Oct 2026",
            statusNotes = "DMRC Maintenance Unit inspected site on 10 Oct. Repair crew scheduled for resurfacing by 14 Oct.",
            resolutionEstimate = "Expected resolution: 14 Oct 2026"
        ),
        BarrierReport(
            id = "rep-002",
            title = "Tactile Paving Obstructed by Vendor Carts",
            category = BarrierCategory.BLOCKED_TACTILE,
            locationName = "Majestic KSRTC Bus Terminal",
            city = "Bengaluru",
            specificLocationDetails = "Platform 3 main yellow walkway completely blocked by commercial kiosks",
            severity = BarrierSeverity.MEDIUM,
            status = BarrierStatus.UNDER_REVIEW,
            dateReported = "07 Oct 2026",
            statusNotes = "Notice forwarded to KSRTC Terminal Manager. Joint clearance inspection pending.",
            resolutionEstimate = "Under review with terminal authorities"
        ),
        BarrierReport(
            id = "rep-003",
            title = "Elevator Display Screen and Voice Chime Restored",
            category = BarrierCategory.ELEVATOR_OUT,
            locationName = "Anna Centenary Library Entrance",
            city = "Chennai",
            specificLocationDetails = "Ground floor lift audio floor announcement was silent",
            severity = BarrierSeverity.LOW,
            status = BarrierStatus.RESOLVED,
            dateReported = "02 Oct 2026",
            statusNotes = "Speaker module replaced and bilingual Tamil/English floor chime tested and verified.",
            resolutionEstimate = "Resolved on 04 Oct 2026"
        )
    )
}
