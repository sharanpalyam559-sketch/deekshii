package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DeekshiiRepository
import com.example.data.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AppScreen {
    DASHBOARD,
    SCHEDULE,
    ACADEMICS,
    FORUM,
    PRODUCTIVITY,
    PORTFOLIO
}

class DeekshiiViewModel(
    private val repository: DeekshiiRepository = DeekshiiRepository()
) : ViewModel() {

    val profile: StateFlow<StudentProfile> = repository.profile
    val subjects: StateFlow<List<SubjectRecord>> = repository.subjects
    val scheduleList: StateFlow<List<ScheduleItem>> = repository.scheduleList
    val dailyTasks: StateFlow<List<DailyTask>> = repository.dailyTasks
    val examDeadlines: StateFlow<List<ExamDeadline>> = repository.examDeadlines
    val forumPosts: StateFlow<List<ForumPost>> = repository.forumPosts
    val motivationalAlerts: StateFlow<List<MotivationalAlert>> = repository.motivationalAlerts
    val productivityTips: StateFlow<List<ProductivityTip>> = repository.productivityTips

    // Navigation & Intro states
    private val _currentScreen = MutableStateFlow(AppScreen.DASHBOARD)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _showButterflyIntro = MutableStateFlow(true)
    val showButterflyIntro: StateFlow<Boolean> = _showButterflyIntro.asStateFlow()

    private val _selectedScheduleDay = MutableStateFlow("Monday")
    val selectedScheduleDay: StateFlow<String> = _selectedScheduleDay.asStateFlow()

    private val _selectedAcademicsTab = MutableStateFlow(0) // 0: Subjects & Marks, 1: Diagrams & Visual Trends
    val selectedAcademicsTab: StateFlow<Int> = _selectedAcademicsTab.asStateFlow()

    // Pomodoro Timer State
    private val _pomodoroTotalSeconds = MutableStateFlow(25 * 60)
    val pomodoroTotalSeconds: StateFlow<Int> = _pomodoroTotalSeconds.asStateFlow()

    private val _pomodoroSecondsRemaining = MutableStateFlow(25 * 60)
    val pomodoroSecondsRemaining: StateFlow<Int> = _pomodoroSecondsRemaining.asStateFlow()

    private val _isPomodoroRunning = MutableStateFlow(false)
    val isPomodoroRunning: StateFlow<Boolean> = _isPomodoroRunning.asStateFlow()

    private val _pomodoroSessionsCompleted = MutableStateFlow(2)
    val pomodoroSessionsCompleted: StateFlow<Int> = _pomodoroSessionsCompleted.asStateFlow()

    private var pomodoroJob: Job? = null

    fun dismissButterflyIntro() {
        _showButterflyIntro.value = false
    }

    fun replayButterflyIntro() {
        _showButterflyIntro.value = true
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun setScheduleDay(day: String) {
        _selectedScheduleDay.value = day
    }

    fun setAcademicsTab(tabIndex: Int) {
        _selectedAcademicsTab.value = tabIndex
    }

    // Pomodoro logic
    fun togglePomodoro() {
        if (_isPomodoroRunning.value) {
            pausePomodoro()
        } else {
            startPomodoro()
        }
    }

    private fun startPomodoro() {
        _isPomodoroRunning.value = true
        pomodoroJob?.cancel()
        pomodoroJob = viewModelScope.launch {
            while (_pomodoroSecondsRemaining.value > 0 && _isPomodoroRunning.value) {
                delay(1000L)
                _pomodoroSecondsRemaining.value -= 1
            }
            if (_pomodoroSecondsRemaining.value <= 0) {
                _isPomodoroRunning.value = false
                _pomodoroSessionsCompleted.value += 1
                _pomodoroSecondsRemaining.value = _pomodoroTotalSeconds.value
            }
        }
    }

    fun pausePomodoro() {
        _isPomodoroRunning.value = false
        pomodoroJob?.cancel()
    }

    fun resetPomodoro(minutes: Int = 25) {
        pausePomodoro()
        _pomodoroTotalSeconds.value = minutes * 60
        _pomodoroSecondsRemaining.value = minutes * 60
    }

    // Repository delegates
    fun markAttendance(subjectId: String, isPresent: Boolean) {
        repository.recordAttendance(subjectId, isPresent)
    }

    fun updateSubjectMarks(subjectId: String, mid1: Double, mid2: Double, internal: Double, target: Double) {
        repository.updateMarks(subjectId, mid1, mid2, internal, target)
    }

    fun addNewSubject(name: String, code: String, faculty: String, credits: Int, attended: Int, total: Int) {
        repository.addSubject(name, code, faculty, credits, attended, total)
    }

    fun toggleTask(taskId: String) {
        repository.toggleTask(taskId)
    }

    fun addTask(title: String, category: String, dueDate: String, priority: String) {
        repository.addTask(title, category, dueDate, priority)
    }

    fun deleteTask(taskId: String) {
        repository.deleteTask(taskId)
    }

    fun addExam(title: String, code: String, date: String, daysLeft: Int, type: String) {
        repository.addExamDeadline(title, code, date, daysLeft, type)
    }

    fun toggleForumLike(postId: String) {
        repository.toggleLikeForumPost(postId)
    }

    fun addReplyToPost(postId: String, text: String) {
        val author = profile.value.name + " (You)"
        repository.addForumReply(postId, author, text)
    }

    fun createForumPost(title: String, category: String, content: String, resourceName: String?, resourceLink: String?) {
        repository.createForumPost(title, category, content, resourceName, resourceLink)
    }

    fun updateStudentProfile(name: String, rollNo: String, branch: String, college: String, cgpa: Double, targetCgpa: Double, bio: String) {
        repository.updateProfile(name, rollNo, branch, college, cgpa, targetCgpa, bio)
    }
}
