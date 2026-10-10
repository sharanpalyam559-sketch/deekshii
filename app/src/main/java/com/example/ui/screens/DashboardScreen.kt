package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.*
import com.example.ui.AppScreen
import com.example.ui.theme.*

data class DashboardOptionItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val containerColor: Color,
    val iconTint: Color,
    val targetScreen: AppScreen,
    val targetSubTab: Int? = null,
    val badge: String? = null
)

@Composable
fun DashboardScreen(
    profile: StudentProfile,
    subjects: List<SubjectRecord>,
    scheduleList: List<ScheduleItem>,
    dailyTasks: List<DailyTask>,
    examDeadlines: List<ExamDeadline>,
    motivationalAlerts: List<MotivationalAlert>,
    onNavigate: (AppScreen) -> Unit,
    onNavigateWithTab: (AppScreen, Int?) -> Unit = { screen, _ -> onNavigate(screen) },
    onReplayButterfly: () -> Unit,
    onToggleTheme: () -> Unit = {},
    onToggleTask: (String) -> Unit
) {
    val colors = DeekshiiTheme.colors

    // Quick statistics
    val totalAttended = subjects.sumOf { it.attendedClasses }
    val totalClasses = subjects.sumOf { it.totalClasses }
    val overallAttendancePct = if (totalClasses > 0) (totalAttended.toDouble() / totalClasses.toDouble()) * 100.0 else 100.0
    val pendingTasksCount = dailyTasks.count { !it.isCompleted }
    val nearestExam = examDeadlines.minByOrNull { it.daysRemaining }

    var currentMotivationIndex by remember { mutableIntStateOf(0) }
    val currentAlert = motivationalAlerts.getOrNull(currentMotivationIndex % motivationalAlerts.size)

    val todaySchedule = remember(scheduleList) {
        scheduleList.filter { it.dayOfWeek == "Monday" } // Monday or current day
    }

    // All Options Dashboard items
    val dashboardOptions = listOf(
        DashboardOptionItem(
            title = "Daily Work Schedule",
            subtitle = "Timetable & lab sessions",
            icon = Icons.Default.CalendarToday,
            containerColor = colors.lavenderContainer,
            iconTint = colors.lavenderPrimary,
            targetScreen = AppScreen.SCHEDULE,
            targetSubTab = 0,
            badge = "${todaySchedule.size} Classes"
        ),
        DashboardOptionItem(
            title = "Exam Deadlines",
            subtitle = "Mid & sem exam countdown",
            icon = Icons.Default.Alarm,
            containerColor = colors.pinkContainer,
            iconTint = colors.pinkPrimary,
            targetScreen = AppScreen.SCHEDULE,
            targetSubTab = 1,
            badge = "${nearestExam?.daysRemaining ?: 0}d left"
        ),
        DashboardOptionItem(
            title = "Attendance Manager",
            subtitle = "75% Cutoff & bunk status",
            icon = Icons.Default.CheckCircle,
            containerColor = colors.mintContainer,
            iconTint = colors.mintPrimary,
            targetScreen = AppScreen.ACADEMICS,
            targetSubTab = 0,
            badge = "%.1f%%".format(overallAttendancePct)
        ),
        DashboardOptionItem(
            title = "Marks & Progress",
            subtitle = "Mid-1, Mid-2 & Internals",
            icon = Icons.Default.Grade,
            containerColor = colors.peachContainer,
            iconTint = colors.peachPrimary,
            targetScreen = AppScreen.ACADEMICS,
            targetSubTab = 0,
            badge = "${profile.cgpa} CGPA"
        ),
        DashboardOptionItem(
            title = "Diagrams & Trends",
            subtitle = "Visual attendance & marks",
            icon = Icons.Default.BarChart,
            containerColor = colors.skyContainer,
            iconTint = colors.skyPrimary,
            targetScreen = AppScreen.ACADEMICS,
            targetSubTab = 1,
            badge = "Charts"
        ),
        DashboardOptionItem(
            title = "Classmate Study Forum",
            subtitle = "Shared notes, drive & code",
            icon = Icons.Default.Groups,
            containerColor = colors.lavenderContainer,
            iconTint = colors.lavenderPrimary,
            targetScreen = AppScreen.FORUM,
            badge = "4 New"
        ),
        DashboardOptionItem(
            title = "Productivity Hub",
            subtitle = "Pomodoro timer & study tips",
            icon = Icons.Default.Timer,
            containerColor = colors.mintContainer,
            iconTint = colors.mintPrimary,
            targetScreen = AppScreen.PRODUCTIVITY,
            badge = "25m Focus"
        ),
        DashboardOptionItem(
            title = "Student Portfolio",
            subtitle = "Deekshitha's profile & skills",
            icon = Icons.Default.Person,
            containerColor = colors.pinkContainer,
            iconTint = colors.pinkPrimary,
            targetScreen = AppScreen.PORTFOLIO,
            badge = "CSE-6"
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Aesthetic Header with Banner and Soft Welcome
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            ) {
                // Background Hero Illustration
                Image(
                    painter = painterResource(id = R.drawable.img_butterfly_hero),
                    contentDescription = "Calm pastel butterflies banner",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)),
                    contentScale = ContentScale.Crop
                )

                // Soft pastel gradient scrim
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    if (colors.isDark) Color(0xFF13101A).copy(alpha = 0.45f) else Color.White.copy(alpha = 0.45f),
                                    colors.background.copy(alpha = 0.94f)
                                )
                            )
                        )
                )

                // Header Top Controls & Greeting
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Replay Butterfly Animation Button
                            FilledTonalButton(
                                onClick = onReplayButterfly,
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = if (colors.isDark) colors.surface.copy(alpha = 0.88f) else Color.White.copy(alpha = 0.85f),
                                    contentColor = colors.lavenderPrimary
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("replay_butterfly_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FilterVintage,
                                    contentDescription = "Butterfly Animation",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Calm Pause",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            // Theme Toggle Button
                            FilledTonalButton(
                                onClick = onToggleTheme,
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = if (colors.isDark) colors.surface.copy(alpha = 0.88f) else Color.White.copy(alpha = 0.85f),
                                    contentColor = colors.lavenderPrimary
                                ),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("theme_toggle_button")
                            ) {
                                Icon(
                                    imageVector = if (colors.isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                                    contentDescription = if (colors.isDark) "Switch to Light Theme" else "Switch to Dark Theme",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (colors.isDark) "Light" else "Dark",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Profile Avatar
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(colors.lavenderLight)
                                .border(1.5.dp, colors.lavenderPrimary, CircleShape)
                                .clickable { onNavigate(AppScreen.PORTFOLIO) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = profile.name.take(1),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.lavenderPrimary
                            )
                        }
                    }

                    // Welcome to Deekshii & Student Name
                    Column {
                        Text(
                            text = "Welcome to Deekshii",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = QuicksandFontFamily,
                                fontWeight = FontWeight.Medium,
                                color = colors.textSecondary
                            )
                        )
                        Text(
                            text = profile.name,
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontFamily = PlayfairFontFamily,
                                fontStyle = FontStyle.Italic,
                                fontSize = 32.sp,
                                color = colors.lavenderPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        // Dedicated portfolio tag requested by user
                        Text(
                            text = "Created by deekshitha for my personal student portfolio app",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.5.sp,
                                color = colors.textMuted
                            )
                        )
                    }
                }
            }
        }

        // Quick Academic Pulse Metrics Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Attendance Quick Card
                PulseMetricCard(
                    label = "Attendance",
                    value = "%.1f%%".format(overallAttendancePct),
                    subtext = if (overallAttendancePct >= 75f) "Safe (>75%)" else "Warning",
                    color = if (overallAttendancePct >= 75f) colors.mintPrimary else colors.pinkPrimary,
                    bgColor = if (overallAttendancePct >= 75f) colors.mintContainer else colors.pinkContainer,
                    modifier = Modifier.weight(1f)
                ) {
                    onNavigateWithTab(AppScreen.ACADEMICS, 0)
                }

                // CGPA Quick Card
                PulseMetricCard(
                    label = "CGPA",
                    value = "%.2f".format(profile.cgpa),
                    subtext = "Target ${profile.targetCgpa}",
                    color = colors.lavenderPrimary,
                    bgColor = colors.lavenderContainer,
                    modifier = Modifier.weight(1f)
                ) {
                    onNavigateWithTab(AppScreen.ACADEMICS, 1)
                }

                // Pending Tasks Quick Card
                PulseMetricCard(
                    label = "Tasks",
                    value = "$pendingTasksCount due",
                    subtext = "Today's list",
                    color = colors.peachPrimary,
                    bgColor = colors.peachContainer,
                    modifier = Modifier.weight(1f)
                ) {
                    onNavigateWithTab(AppScreen.SCHEDULE, 0)
                }

                // Next Exam Quick Card
                PulseMetricCard(
                    label = "Exam",
                    value = "${nearestExam?.daysRemaining ?: 0}d left",
                    subtext = nearestExam?.subjectCode ?: "Mid-2",
                    color = colors.skyPrimary,
                    bgColor = colors.skyContainer,
                    modifier = Modifier.weight(1f)
                ) {
                    onNavigateWithTab(AppScreen.SCHEDULE, 1)
                }
            }
        }

        // Motivational Alert Banner
        currentAlert?.let { alert ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("motivational_alert_card"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.lavenderContainer),
                    border = androidx.compose.foundation.BorderStroke(1.dp, colors.outline)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(colors.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Motivational spark",
                                tint = colors.pinkPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = alert.context,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colors.lavenderOnContainer,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Tap to refresh ↻",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colors.textMuted,
                                    modifier = Modifier.clickable {
                                        currentMotivationIndex++
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "“${alert.quote}”",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.textPrimary,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                }
            }
        }

        // Section Title: All Options Dashboard
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 8.dp)
            ) {
                Text(
                    text = "All Options Dashboard",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                Text(
                    text = "Seamless one-tap navigation to every student tool",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary
                )
            }
        }

        // All Options 2-Column Grid
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                dashboardOptions.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowItems.forEach { option ->
                            DashboardOptionCard(
                                item = option,
                                modifier = Modifier.weight(1f),
                                onClick = { onNavigateWithTab(option.targetScreen, option.targetSubTab) }
                            )
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // Today's Timetable Glance
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.outline)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(colors.lavenderLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = "Schedule",
                                    tint = colors.lavenderPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Today's Schedule Glance",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.textPrimary
                                )
                                Text(
                                    text = "Monday • ${todaySchedule.size} lectures & labs",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.textSecondary
                                )
                            }
                        }

                        TextButton(
                            onClick = { onNavigate(AppScreen.SCHEDULE) },
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Text("Full Week", style = MaterialTheme.typography.labelMedium)
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    todaySchedule.take(3).forEach { slot ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = if (slot.isLab) colors.mintContainer else colors.lavenderContainer,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = slot.timeSlot.split(" - ").first(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (slot.isLab) colors.mintOnContainer else colors.lavenderOnContainer,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = slot.subjectName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colors.textPrimary
                                )
                                Text(
                                    text = "${slot.roomNo} • ${slot.faculty}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.textSecondary
                                )
                            }

                            if (slot.isLab) {
                                Surface(
                                    color = colors.mintContainer,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "LAB",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = colors.mintOnContainer,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        if (slot != todaySchedule.take(3).last()) {
                            HorizontalDivider(color = colors.outline.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 2.dp))
                        }
                    }
                }
            }
        }

        // Daily Work Tasks Preview Checklist
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.outline)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(colors.pinkContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TaskAlt,
                                    contentDescription = "Tasks",
                                    tint = colors.pinkPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Daily Work & Deadlines",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                        }

                        TextButton(
                            onClick = { onNavigate(AppScreen.SCHEDULE) },
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Text("Manage", style = MaterialTheme.typography.labelMedium)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    dailyTasks.take(3).forEach { task ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onToggleTask(task.id) }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = task.isCompleted,
                                onCheckedChange = { onToggleTask(task.id) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = colors.lavenderPrimary,
                                    uncheckedColor = colors.outline
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = task.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (task.isCompleted) colors.textMuted else colors.textPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${task.category} • ${task.dueDate}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.textSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardOptionCard(
    item: DashboardOptionItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = DeekshiiTheme.colors
    Card(
        modifier = modifier
            .height(115.dp)
            .clickable(onClick = onClick)
            .testTag("dashboard_opt_${item.title.replace(" ", "_").lowercase()}"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(item.containerColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = item.iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }

                item.badge?.let { badgeText ->
                    Surface(
                        color = item.containerColor,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = badgeText,
                            style = MaterialTheme.typography.labelSmall,
                            color = item.iconTint,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Column {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun PulseMetricCard(
    label: String,
    value: String,
    subtext: String,
    color: Color,
    bgColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = DeekshiiTheme.colors
    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .testTag("pulse_metric_${label.lowercase()}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = colors.textSecondary,
                fontSize = 10.5.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = subtext,
                style = MaterialTheme.typography.labelSmall,
                color = colors.textSecondary,
                fontSize = 9.5.sp,
                maxLines = 1
            )
        }
    }
}
