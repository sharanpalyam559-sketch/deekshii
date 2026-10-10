package com.example.data.model

data class StudentProfile(
    val name: String = "Deekshitha",
    val title: String = "B.Tech Computer Science & Engineering",
    val semester: String = "Semester VI (3rd Year)",
    val college: String = "Engineering College of Technology",
    val rollNo: String = "21B91A0501",
    val cgpa: Double = 8.92,
    val targetCgpa: Double = 9.20,
    val currentSgpa: Double = 9.05,
    val portfolioBio: String = "Created by Deekshitha for my personal student portfolio app. Passionate about software architecture, calm design, and engineering excellence.",
    val technicalSkills: List<String> = listOf("Kotlin & Jetpack Compose", "Java", "Python", "Machine Learning", "Data Structures", "Database Management", "Computer Networks", "Git & CI/CD")
)

data class SubjectRecord(
    val id: String,
    val code: String,
    val name: String,
    val faculty: String,
    val credits: Int,
    val attendedClasses: Int,
    val totalClasses: Int,
    val mid1Marks: Double, // out of 30
    val mid2Marks: Double, // out of 30
    val internalMarks: Double, // out of 40
    val targetMarks: Double, // out of 100
    val syllabusProgress: Float, // 0.0 to 1.0
    val accentColorHex: Long
) {
    val attendancePercentage: Float
        get() = if (totalClasses > 0) (attendedClasses.toFloat() / totalClasses.toFloat()) * 100f else 100f

    val isSafeAttendance: Boolean
        get() = attendancePercentage >= 75f

    // Bunk calculator: how many classes can miss or need to attend to maintain 75%
    val bunkStatusText: String
        get() {
            if (totalClasses == 0) return "No classes yet"
            val currentPct = attendancePercentage
            return if (currentPct >= 75f) {
                // Calculate how many future classes can miss while staying >= 75%
                // (attended) / (total + x) >= 0.75  =>  x <= (attended / 0.75) - total
                val canMiss = ((attendedClasses / 0.75) - totalClasses).toInt().coerceAtLeast(0)
                if (canMiss > 0) "✓ You can miss $canMiss classes safely" else "On threshold: Don't miss next class"
            } else {
                // Calculate how many consecutive classes must attend to reach 75%
                // (attended + y) / (total + y) >= 0.75 => y >= (0.75 * total - attended) / 0.25
                val needAttend = Math.ceil(((0.75 * totalClasses) - attendedClasses) / 0.25).toInt().coerceAtLeast(1)
                "⚠️ Attend next $needAttend classes to reach 75%"
            }
        }
}

data class ScheduleItem(
    val id: String,
    val dayOfWeek: String, // "Monday", "Tuesday", etc.
    val timeSlot: String,
    val subjectName: String,
    val roomNo: String,
    val faculty: String,
    val isLab: Boolean = false,
    val isToday: Boolean = false
)

data class DailyTask(
    val id: String,
    val title: String,
    val category: String, // "Assignment", "Lab Record", "Mini Project", "Revision", "Self-Care"
    val dueDate: String,
    val priority: String, // "High", "Medium", "Low"
    val isCompleted: Boolean = false
)

data class ExamDeadline(
    val id: String,
    val title: String,
    val subjectCode: String,
    val examDate: String,
    val daysRemaining: Int,
    val examType: String, // "Mid-Term II", "Semester End Exam", "Lab External", "Viva Voce"
    val syllabusProgress: Int, // 0-100%
    val hallNumber: String = "Hall 204"
)

data class ForumPost(
    val id: String,
    val authorName: String,
    val authorBadge: String, // "Classmate", "Class Representative", "Deekshitha (You)", "Study Buddy"
    val timeAgo: String,
    val category: String, // "Notes & PPTs", "Lab Solutions", "Study Groups", "Viva Prep", "Project Collab"
    val title: String,
    val content: String,
    val resourceName: String? = null,
    val resourceLink: String? = null,
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val replies: List<ForumReply> = emptyList()
)

data class ForumReply(
    val id: String,
    val authorName: String,
    val timeAgo: String,
    val content: String
)

data class ProductivityTip(
    val id: String,
    val title: String,
    val category: String,
    val tip: String,
    val iconTag: String
)

data class MotivationalAlert(
    val id: String,
    val quote: String,
    val context: String,
    val author: String,
    val category: String // "Academic Calm", "Engineering Grit", "Self-Care", "Focus"
)
