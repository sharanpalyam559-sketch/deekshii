package com.example.data

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class DeekshiiRepository {

    private val _profile = MutableStateFlow(
        StudentProfile(
            name = "Deekshitha",
            title = "B.Tech Computer Science & Engineering",
            semester = "Semester VI (3rd Year)",
            college = "Vignana Bharathi Institute of Technology",
            rollNo = "21B91A0501",
            cgpa = 8.92,
            targetCgpa = 9.20,
            currentSgpa = 9.05,
            portfolioBio = "Created by Deekshitha for my personal student portfolio app. Aspiring software engineer dedicated to clean architecture, intuitive mobile design, and resilient algorithms 🌸"
        )
    )
    val profile: StateFlow<StudentProfile> = _profile.asStateFlow()

    private val _subjects = MutableStateFlow(
        listOf(
            SubjectRecord(
                id = "sub-1",
                code = "CS601PC",
                name = "Machine Learning",
                faculty = "Dr. K. Sunitha",
                credits = 4,
                attendedClasses = 38,
                totalClasses = 42, // 90.4%
                mid1Marks = 28.5,
                mid2Marks = 29.0,
                internalMarks = 38.5,
                targetMarks = 95.0,
                syllabusProgress = 0.85f,
                accentColorHex = 0xFF917BBF // Pastel Lavender
            ),
            SubjectRecord(
                id = "sub-2",
                code = "CS602PC",
                name = "Computer Networks",
                faculty = "Prof. R. Sharma",
                credits = 4,
                attendedClasses = 35,
                totalClasses = 40, // 87.5%
                mid1Marks = 26.0,
                mid2Marks = 27.5,
                internalMarks = 36.0,
                targetMarks = 90.0,
                syllabusProgress = 0.80f,
                accentColorHex = 0xFF78B5E8 // Pastel Sky
            ),
            SubjectRecord(
                id = "sub-3",
                code = "CS603PC",
                name = "Compiler Design",
                faculty = "Dr. M. Venkat",
                credits = 4,
                attendedClasses = 32,
                totalClasses = 41, // 78.0%
                mid1Marks = 24.5,
                mid2Marks = 26.0,
                internalMarks = 34.0,
                targetMarks = 88.0,
                syllabusProgress = 0.75f,
                accentColorHex = 0xFFF398AE // Pastel Pink
            ),
            SubjectRecord(
                id = "sub-4",
                code = "CS604PE",
                name = "Cloud Computing & DevOps",
                faculty = "Prof. A. Meenakshi",
                credits = 3,
                attendedClasses = 31,
                totalClasses = 34, // 91.1%
                mid1Marks = 29.0,
                mid2Marks = 28.5,
                internalMarks = 39.0,
                targetMarks = 94.0,
                syllabusProgress = 0.90f,
                accentColorHex = 0xFF6FBA8C // Pastel Mint
            ),
            SubjectRecord(
                id = "sub-5",
                code = "CS605PE",
                name = "Software Engineering Principles",
                faculty = "Prof. T. Rajesh",
                credits = 3,
                attendedClasses = 28,
                totalClasses = 38, // 73.7% - Needs attention!
                mid1Marks = 23.0,
                mid2Marks = 25.0,
                internalMarks = 33.0,
                targetMarks = 85.0,
                syllabusProgress = 0.70f,
                accentColorHex = 0xFFF6A97A // Pastel Peach
            ),
            SubjectRecord(
                id = "sub-6",
                code = "CS606PC",
                name = "Machine Learning Lab",
                faculty = "Dr. K. Sunitha & Lab Team",
                credits = 2,
                attendedClasses = 14,
                totalClasses = 14, // 100%
                mid1Marks = 30.0,
                mid2Marks = 30.0,
                internalMarks = 40.0,
                targetMarks = 98.0,
                syllabusProgress = 0.95f,
                accentColorHex = 0xFF917BBF
            ),
            SubjectRecord(
                id = "sub-7",
                code = "CS607PC",
                name = "Computer Networks Lab",
                faculty = "Prof. R. Sharma & Team",
                credits = 2,
                attendedClasses = 13,
                totalClasses = 14, // 92.8%
                mid1Marks = 28.0,
                mid2Marks = 29.0,
                internalMarks = 38.0,
                targetMarks = 95.0,
                syllabusProgress = 0.90f,
                accentColorHex = 0xFF78B5E8
            )
        )
    )
    val subjects: StateFlow<List<SubjectRecord>> = _subjects.asStateFlow()

    private val _scheduleList = MutableStateFlow(
        listOf(
            // Monday
            ScheduleItem("sc-1", "Monday", "09:00 AM - 10:00 AM", "Machine Learning", "Room 304", "Dr. K. Sunitha"),
            ScheduleItem("sc-2", "Monday", "10:00 AM - 11:00 AM", "Computer Networks", "Room 304", "Prof. R. Sharma"),
            ScheduleItem("sc-3", "Monday", "11:15 AM - 12:15 PM", "Compiler Design", "Room 304", "Dr. M. Venkat"),
            ScheduleItem("sc-4", "Monday", "01:00 PM - 03:00 PM", "Machine Learning Lab", "AI/ML Lab 2", "Dr. Sunitha", isLab = true),
            ScheduleItem("sc-5", "Monday", "03:15 PM - 04:15 PM", "Mentor Hour & Seminar", "Seminar Hall A", "CSE Faculty"),

            // Tuesday
            ScheduleItem("sc-6", "Tuesday", "09:00 AM - 10:00 AM", "Cloud Computing & DevOps", "Room 304", "Prof. A. Meenakshi"),
            ScheduleItem("sc-7", "Tuesday", "10:00 AM - 11:00 AM", "Compiler Design", "Room 304", "Dr. M. Venkat"),
            ScheduleItem("sc-8", "Tuesday", "11:15 AM - 12:15 PM", "Software Engineering", "Room 304", "Prof. T. Rajesh"),
            ScheduleItem("sc-9", "Tuesday", "01:00 PM - 03:00 PM", "Networks Lab", "Systems Lab 1", "Prof. R. Sharma", isLab = true),

            // Wednesday
            ScheduleItem("sc-10", "Wednesday", "09:00 AM - 10:00 AM", "Computer Networks", "Room 304", "Prof. R. Sharma"),
            ScheduleItem("sc-11", "Wednesday", "10:00 AM - 11:00 AM", "Machine Learning", "Room 304", "Dr. K. Sunitha"),
            ScheduleItem("sc-12", "Wednesday", "11:15 AM - 12:15 PM", "Cloud Computing", "Room 304", "Prof. A. Meenakshi"),
            ScheduleItem("sc-13", "Wednesday", "01:00 PM - 02:30 PM", "B.Tech Mini-Project Lab", "Project Lab 3", "Project Guides", isLab = true),

            // Thursday
            ScheduleItem("sc-14", "Thursday", "09:00 AM - 10:00 AM", "Software Engineering", "Room 304", "Prof. T. Rajesh"),
            ScheduleItem("sc-15", "Thursday", "10:00 AM - 11:00 AM", "Compiler Design", "Room 304", "Dr. M. Venkat"),
            ScheduleItem("sc-16", "Thursday", "11:15 AM - 12:15 PM", "Machine Learning", "Room 304", "Dr. K. Sunitha"),
            ScheduleItem("sc-17", "Thursday", "01:00 PM - 03:00 PM", "Technical Aptitude & Soft Skills", "Auditorium", "Placement Cell"),

            // Friday
            ScheduleItem("sc-18", "Friday", "09:00 AM - 10:00 AM", "Computer Networks", "Room 304", "Prof. R. Sharma"),
            ScheduleItem("sc-19", "Friday", "10:00 AM - 11:00 AM", "Cloud Computing & DevOps", "Room 304", "Prof. A. Meenakshi"),
            ScheduleItem("sc-20", "Friday", "11:15 AM - 12:15 PM", "Software Engineering", "Room 304", "Prof. T. Rajesh"),
            ScheduleItem("sc-21", "Friday", "01:00 PM - 03:00 PM", "Coding Practice & Peer Study", "Innovation Lab", "Deekshitha & Peers")
        )
    )
    val scheduleList: StateFlow<List<ScheduleItem>> = _scheduleList.asStateFlow()

    private val _dailyTasks = MutableStateFlow(
        listOf(
            DailyTask("tk-1", "Complete ML Lab Experiment 7: SVM Classification on Breast Cancer dataset", "Lab Record", "Today, 5:00 PM", "High", isCompleted = false),
            DailyTask("tk-2", "Submit Computer Networks Assignment 3 (Subnetting & RIP Protocol)", "Assignment", "Tomorrow, 11:59 PM", "High", isCompleted = true),
            DailyTask("tk-3", "Revise Compiler Design Bottom-Up Parsing (LR & LALR parsers)", "Revision", "Oct 12", "Medium", isCompleted = false),
            DailyTask("tk-4", "Prepare mini-project PPT for Phase-2 Evaluation review", "Mini Project", "Oct 14", "High", isCompleted = false),
            DailyTask("tk-5", "20-minute mindful walk & hydration break between study blocks", "Self-Care", "Daily", "Low", isCompleted = true),
            DailyTask("tk-6", "Check shared forum for Sneha's Cloud Computing unit summary notes", "Revision", "Oct 13", "Medium", isCompleted = false)
        )
    )
    val dailyTasks: StateFlow<List<DailyTask>> = _dailyTasks.asStateFlow()

    private val _examDeadlines = MutableStateFlow(
        listOf(
            ExamDeadline("ex-1", "Machine Learning Mid-Term II", "CS601PC", "Oct 15, 2026", 5, "Mid-Term II", 85, "Hall 302"),
            ExamDeadline("ex-2", "Computer Networks Mid-Term II", "CS602PC", "Oct 17, 2026", 7, "Mid-Term II", 75, "Hall 302"),
            ExamDeadline("ex-3", "Compiler Design Internal Assessment", "CS603PC", "Oct 20, 2026", 10, "Internal Assessment", 68, "Hall 304"),
            ExamDeadline("ex-4", "ML Lab External Viva Voce", "CS606PC", "Nov 02, 2026", 23, "Lab External", 90, "AI/ML Lab 2"),
            ExamDeadline("ex-5", "Semester End Examination (SEE Theory)", "All B.Tech Subjects", "Nov 16, 2026", 37, "Semester End Exam", 60, "Central Exam Block")
        )
    )
    val examDeadlines: StateFlow<List<ExamDeadline>> = _examDeadlines.asStateFlow()

    private val _forumPosts = MutableStateFlow(
        listOf(
            ForumPost(
                id = "fp-1",
                authorName = "Ananya Sharma",
                authorBadge = "Classmate",
                timeAgo = "2 hours ago",
                category = "Notes & PPTs",
                title = "Handwritten Notes: ML Support Vector Machines & Kernel Trick ✨",
                content = "Hey everyone! I summarized Dr. Sunitha's lectures on SVMs, soft margin slack variables, and RBF kernel formulations into a neat 8-page color-coded PDF. Hope this helps for the upcoming Mid-2!",
                resourceName = "ML_Unit3_SVM_Notes_DeekshiiBatch.pdf",
                resourceLink = "drive.google.com/deekshii-cse/ml-unit3-svm.pdf",
                likesCount = 14,
                isLiked = true,
                replies = listOf(
                    ForumReply("rp-1", "Deekshitha (You)", "1 hour ago", "Thank you so much Ananya! The dual problem derivation diagram is super clear 🌸"),
                    ForumReply("rp-2", "Priya Verma", "45 mins ago", "Lifesaver! Was struggling with the Lagrange multipliers part.")
                )
            ),
            ForumPost(
                id = "fp-2",
                authorName = "Sneha Reddy",
                authorBadge = "Class Representative",
                timeAgo = "5 hours ago",
                category = "Study Groups",
                title = "Saturday Group Study: Compiler Design LR(0) & LALR Parsing Tables 📚",
                content = "We are hosting an online Google Meet group study session tomorrow at 6:00 PM to solve previous years' semester questions on shift-reduce parsing and grammar ambiguity. Anyone wanting to practice is welcome to join!",
                resourceName = "meet.google.com/vbit-cse-study",
                resourceLink = "meet.google.com/vbit-cse-study",
                likesCount = 19,
                isLiked = false,
                replies = listOf(
                    ForumReply("rp-3", "Keerthi Rao", "3 hours ago", "I will join! I have 4 previous question papers printed.")
                )
            ),
            ForumPost(
                id = "fp-3",
                authorName = "Deekshitha (You)",
                authorBadge = "Author / Portfolio App",
                timeAgo = "Yesterday",
                category = "Lab Solutions",
                title = "Computer Networks Socket Programming (TCP/UDP Chat in Python & C)",
                content = "Shared my clean, commented implementation for Lab Experiment 5 (Multi-client TCP chat server using select/threading). Includes test packets screenshot and viva questions reference.",
                resourceName = "github.com/deekshitha-portfolio/cn-lab-solutions",
                resourceLink = "github.com/deekshitha-portfolio/cn-lab-solutions",
                likesCount = 26,
                isLiked = true,
                replies = listOf(
                    ForumReply("rp-4", "Varun Teja", "Yesterday", "The socket timeout explanation was gold Deekshitha! Thanks!")
                )
            ),
            ForumPost(
                id = "fp-4",
                authorName = "Pooja Hegde",
                authorBadge = "Study Buddy",
                timeAgo = "2 days ago",
                category = "Viva Prep",
                title = "Top 25 Viva Questions for Machine Learning Lab Exam 🎯",
                content = "Compiled a list of questions faculty usually ask during external viva: Bias vs Variance tradeoff, why ReLU over Sigmoid, precision vs recall curves, and cross-validation types.",
                resourceName = "ML_Viva_Quick_CheatSheet.pdf",
                resourceLink = "drive.google.com/cse-deekshii/ml-viva-cheatsheet.pdf",
                likesCount = 31,
                isLiked = false,
                replies = emptyList()
            )
        )
    )
    val forumPosts: StateFlow<List<ForumPost>> = _forumPosts.asStateFlow()

    private val _motivationalAlerts = MutableStateFlow(
        listOf(
            MotivationalAlert(
                id = "ma-1",
                quote = "Every complex algorithm was once a series of simple questions. Trust your consistency, Deekshitha.",
                context = "Daily Spark for Women in Engineering",
                author = "Grace Hopper Philosophy",
                category = "Engineering Grit"
            ),
            MotivationalAlert(
                id = "ma-2",
                quote = "Academic success isn't about pulling all-nighters; it's about 45 minutes of quiet, peaceful focus every day.",
                context = "Mindful Study Tip",
                author = "Calm Productivity",
                category = "Academic Calm"
            ),
            MotivationalAlert(
                id = "ma-3",
                quote = "Drink a glass of water, gently stretch your shoulders, and give yourself credit for how far you've come this semester.",
                context = "Wellness Check-in",
                author = "Deekshii Care Bot",
                category = "Self-Care"
            ),
            MotivationalAlert(
                id = "ma-4",
                quote = "You are not just preparing for exams; you are building the problem-solving mindset of a future tech leader.",
                context = "Empowerment Alert",
                author = "Women in STEM",
                category = "Focus"
            )
        )
    )
    val motivationalAlerts: StateFlow<List<MotivationalAlert>> = _motivationalAlerts.asStateFlow()

    private val _productivityTips = MutableStateFlow(
        listOf(
            ProductivityTip(
                id = "pt-1",
                title = "The 50/10 Engineering Pomodoro",
                category = "Time Management",
                tip = "Code or solve theory derivations for 50 uninterrupted minutes, followed by a strict 10-minute away-from-screen break. Restores cognitive clarity for complex algorithms.",
                iconTag = "timer"
            ),
            ProductivityTip(
                id = "pt-2",
                title = "Feynman Technique for Tough Subjects",
                category = "Active Learning",
                tip = "For concepts like LR Parsing or TCP 3-way handshakes, explain them out loud as if teaching a first-year junior. Gaps in your explanation reveal exactly where to revise.",
                iconTag = "lightbulb"
            ),
            ProductivityTip(
                id = "pt-3",
                title = "Proactive Attendance Buffer Rule",
                category = "Semester Balance",
                tip = "Aim for 85% attendance early in the semester. This leaves you a 10% safety cushion for unexpected events, hackathons, or project sprints without dropping near 75%.",
                iconTag = "check_circle"
            ),
            ProductivityTip(
                id = "pt-4",
                title = "Digital Lab Records Habit",
                category = "Lab Productivity",
                tip = "Push lab code to a private GitHub repo immediately after finishing the lab class. Formatting observations on the same day prevents the dreaded end-of-sem record rush.",
                iconTag = "code"
            )
        )
    )
    val productivityTips: StateFlow<List<ProductivityTip>> = _productivityTips.asStateFlow()

    // Actions
    fun recordAttendance(subjectId: String, isPresent: Boolean) {
        _subjects.value = _subjects.value.map { subject ->
            if (subject.id == subjectId) {
                val newAttended = if (isPresent) subject.attendedClasses + 1 else subject.attendedClasses
                val newTotal = subject.totalClasses + 1
                subject.copy(attendedClasses = newAttended, totalClasses = newTotal)
            } else subject
        }
    }

    fun updateMarks(subjectId: String, mid1: Double, mid2: Double, internal: Double, target: Double) {
        _subjects.value = _subjects.value.map { subject ->
            if (subject.id == subjectId) {
                subject.copy(
                    mid1Marks = mid1,
                    mid2Marks = mid2,
                    internalMarks = internal,
                    targetMarks = target
                )
            } else subject
        }
    }

    fun addSubject(name: String, code: String, faculty: String, credits: Int, attended: Int, total: Int) {
        val newSub = SubjectRecord(
            id = "sub-${UUID.randomUUID().toString().take(6)}",
            code = code,
            name = name,
            faculty = faculty,
            credits = credits,
            attendedClasses = attended,
            totalClasses = total,
            mid1Marks = 25.0,
            mid2Marks = 26.0,
            internalMarks = 35.0,
            targetMarks = 90.0,
            syllabusProgress = 0.70f,
            accentColorHex = 0xFF917BBF
        )
        _subjects.value = _subjects.value + newSub
    }

    fun toggleTask(taskId: String) {
        _dailyTasks.value = _dailyTasks.value.map { task ->
            if (task.id == taskId) task.copy(isCompleted = !task.isCompleted) else task
        }
    }

    fun addTask(title: String, category: String, dueDate: String, priority: String) {
        val newTask = DailyTask(
            id = "tk-${UUID.randomUUID().toString().take(6)}",
            title = title,
            category = category,
            dueDate = dueDate,
            priority = priority,
            isCompleted = false
        )
        _dailyTasks.value = listOf(newTask) + _dailyTasks.value
    }

    fun deleteTask(taskId: String) {
        _dailyTasks.value = _dailyTasks.value.filter { it.id != taskId }
    }

    fun addExamDeadline(title: String, subjectCode: String, date: String, daysLeft: Int, type: String) {
        val newExam = ExamDeadline(
            id = "ex-${UUID.randomUUID().toString().take(6)}",
            title = title,
            subjectCode = subjectCode,
            examDate = date,
            daysRemaining = daysLeft,
            examType = type,
            syllabusProgress = 50
        )
        _examDeadlines.value = (_examDeadlines.value + newExam).sortedBy { it.daysRemaining }
    }

    fun toggleLikeForumPost(postId: String) {
        _forumPosts.value = _forumPosts.value.map { post ->
            if (post.id == postId) {
                val newLiked = !post.isLiked
                val newLikes = if (newLiked) post.likesCount + 1 else post.likesCount - 1
                post.copy(isLiked = newLiked, likesCount = newLikes)
            } else post
        }
    }

    fun addForumReply(postId: String, authorName: String, replyText: String) {
        _forumPosts.value = _forumPosts.value.map { post ->
            if (post.id == postId) {
                val newReply = ForumReply(
                    id = "rp-${UUID.randomUUID().toString().take(6)}",
                    authorName = authorName,
                    timeAgo = "Just now",
                    content = replyText
                )
                post.copy(replies = post.replies + newReply)
            } else post
        }
    }

    fun createForumPost(title: String, category: String, content: String, resourceName: String?, resourceLink: String?) {
        val newPost = ForumPost(
            id = "fp-${UUID.randomUUID().toString().take(6)}",
            authorName = "Deekshitha (You)",
            authorBadge = "Deekshitha / CSE-6",
            timeAgo = "Just now",
            category = category,
            title = title,
            content = content,
            resourceName = resourceName.takeIf { !it.isNullOrBlank() },
            resourceLink = resourceLink.takeIf { !it.isNullOrBlank() },
            likesCount = 1,
            isLiked = true,
            replies = emptyList()
        )
        _forumPosts.value = listOf(newPost) + _forumPosts.value
    }

    fun updateProfile(name: String, rollNo: String, branch: String, college: String, cgpa: Double, targetCgpa: Double, bio: String) {
        _profile.value = _profile.value.copy(
            name = name,
            rollNo = rollNo,
            title = branch,
            college = college,
            cgpa = cgpa,
            targetCgpa = targetCgpa,
            portfolioBio = bio
        )
    }
}
