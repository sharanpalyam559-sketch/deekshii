package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ButterflyOpeningScreen
import com.example.ui.screens.*
import com.example.ui.theme.*

data class NavItem(
    val screen: AppScreen,
    val title: String,
    val icon: ImageVector
)

@Composable
fun MainScreen(viewModel: DeekshiiViewModel) {
    val colors = DeekshiiTheme.colors
    val isSystemDark = isSystemInDarkTheme()

    val showButterflyIntro by viewModel.showButterflyIntro.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val subjects by viewModel.subjects.collectAsStateWithLifecycle()
    val scheduleList by viewModel.scheduleList.collectAsStateWithLifecycle()
    val dailyTasks by viewModel.dailyTasks.collectAsStateWithLifecycle()
    val examDeadlines by viewModel.examDeadlines.collectAsStateWithLifecycle()
    val forumPosts by viewModel.forumPosts.collectAsStateWithLifecycle()
    val motivationalAlerts by viewModel.motivationalAlerts.collectAsStateWithLifecycle()
    val productivityTips by viewModel.productivityTips.collectAsStateWithLifecycle()
    val selectedScheduleDay by viewModel.selectedScheduleDay.collectAsStateWithLifecycle()
    val selectedScheduleTab by viewModel.selectedScheduleTab.collectAsStateWithLifecycle()
    val selectedAcademicsTab by viewModel.selectedAcademicsTab.collectAsStateWithLifecycle()

    val pomodoroTotalSeconds by viewModel.pomodoroTotalSeconds.collectAsStateWithLifecycle()
    val pomodoroSecondsRemaining by viewModel.pomodoroSecondsRemaining.collectAsStateWithLifecycle()
    val isPomodoroRunning by viewModel.isPomodoroRunning.collectAsStateWithLifecycle()
    val pomodoroSessionsCompleted by viewModel.pomodoroSessionsCompleted.collectAsStateWithLifecycle()

    // Handle Android system back press
    BackHandler(enabled = currentScreen != AppScreen.DASHBOARD) {
        viewModel.navigateTo(AppScreen.DASHBOARD)
    }

    if (showButterflyIntro) {
        ButterflyOpeningScreen(
            studentName = profile.name,
            onTransitionComplete = {
                viewModel.dismissButterflyIntro()
            }
        )
    } else {
        val navItems = listOf(
            NavItem(AppScreen.DASHBOARD, "Dashboard", Icons.Default.Home),
            NavItem(AppScreen.SCHEDULE, "Schedule", Icons.Default.CalendarToday),
            NavItem(AppScreen.ACADEMICS, "Academics", Icons.Default.BarChart),
            NavItem(AppScreen.FORUM, "Forum", Icons.Default.Forum),
            NavItem(AppScreen.PRODUCTIVITY, "Focus", Icons.Default.SelfImprovement)
        )

        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = colors.surface,
                    tonalElevation = 6.dp,
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .testTag("bottom_nav_bar")
                ) {
                    navItems.forEach { item ->
                        val isSelected = currentScreen == item.screen
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.navigateTo(item.screen) },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = if (isSelected) colors.lavenderPrimary else colors.textMuted
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp,
                                    color = if (isSelected) colors.lavenderPrimary else colors.textMuted
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = colors.lavenderContainer,
                                selectedIconColor = colors.lavenderPrimary,
                                unselectedIconColor = colors.textMuted,
                                selectedTextColor = colors.lavenderPrimary,
                                unselectedTextColor = colors.textMuted
                            ),
                            modifier = Modifier.testTag("nav_item_${item.title.lowercase()}")
                        )
                    }
                }
            },
            containerColor = colors.background
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                when (currentScreen) {
                    AppScreen.DASHBOARD -> {
                        DashboardScreen(
                            profile = profile,
                            subjects = subjects,
                            scheduleList = scheduleList,
                            dailyTasks = dailyTasks,
                            examDeadlines = examDeadlines,
                            motivationalAlerts = motivationalAlerts,
                            onNavigate = { screen -> viewModel.navigateTo(screen) },
                            onNavigateWithTab = { screen, tab -> viewModel.navigateToWithTab(screen, tab) },
                            onReplayButterfly = { viewModel.replayButterflyIntro() },
                            onToggleTheme = { viewModel.toggleTheme(isSystemDark) },
                            onToggleTask = { taskId -> viewModel.toggleTask(taskId) }
                        )
                    }

                    AppScreen.SCHEDULE -> {
                        ScheduleScreen(
                            scheduleList = scheduleList,
                            selectedDay = selectedScheduleDay,
                            onSelectDay = { day -> viewModel.setScheduleDay(day) },
                            activeSubTab = selectedScheduleTab,
                            onSubTabChange = { tab -> viewModel.setScheduleTab(tab) },
                            dailyTasks = dailyTasks,
                            onToggleTask = { taskId -> viewModel.toggleTask(taskId) },
                            onAddTask = { title, cat, due, prio -> viewModel.addTask(title, cat, due, prio) },
                            onDeleteTask = { taskId -> viewModel.deleteTask(taskId) },
                            examDeadlines = examDeadlines,
                            onAddExam = { title, code, date, days, type -> viewModel.addExam(title, code, date, days, type) }
                        )
                    }

                    AppScreen.ACADEMICS -> {
                        AcademicsScreen(
                            profile = profile,
                            subjects = subjects,
                            selectedTab = selectedAcademicsTab,
                            onTabSelected = { tab -> viewModel.setAcademicsTab(tab) },
                            onMarkAttendance = { subId, isPresent -> viewModel.markAttendance(subId, isPresent) },
                            onUpdateMarks = { subId, m1, m2, int, tgt -> viewModel.updateSubjectMarks(subId, m1, m2, int, tgt) },
                            onAddSubject = { name, code, fac, cred, att, tot -> viewModel.addNewSubject(name, code, fac, cred, att, tot) }
                        )
                    }

                    AppScreen.FORUM -> {
                        ForumScreen(
                            posts = forumPosts,
                            onToggleLike = { postId -> viewModel.toggleForumLike(postId) },
                            onAddReply = { postId, text -> viewModel.addReplyToPost(postId, text) },
                            onCreatePost = { title, cat, cont, resName, resLink ->
                                viewModel.createForumPost(title, cat, cont, resName, resLink)
                            }
                        )
                    }

                    AppScreen.PRODUCTIVITY -> {
                        ProductivityScreen(
                            pomodoroTotalSeconds = pomodoroTotalSeconds,
                            pomodoroSecondsRemaining = pomodoroSecondsRemaining,
                            isPomodoroRunning = isPomodoroRunning,
                            sessionsCompleted = pomodoroSessionsCompleted,
                            onTogglePomodoro = { viewModel.togglePomodoro() },
                            onResetPomodoro = { mins -> viewModel.resetPomodoro(mins) },
                            motivationalAlerts = motivationalAlerts,
                            productivityTips = productivityTips
                        )
                    }

                    AppScreen.PORTFOLIO -> {
                        PortfolioScreen(
                            profile = profile,
                            onToggleTheme = { viewModel.toggleTheme(isSystemDark) },
                            onUpdateProfile = { name, roll, branch, col, cgpa, tgt, bio ->
                                viewModel.updateStudentProfile(name, roll, branch, col, cgpa, tgt, bio)
                            }
                        )
                    }
                }
            }
        }
    }
}
