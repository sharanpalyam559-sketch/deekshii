package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyTask
import com.example.data.model.ExamDeadline
import com.example.data.model.ScheduleItem
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    scheduleList: List<ScheduleItem>,
    selectedDay: String,
    onSelectDay: (String) -> Unit,
    activeSubTab: Int = 0,
    onSubTabChange: (Int) -> Unit = {},
    dailyTasks: List<DailyTask>,
    onToggleTask: (String) -> Unit,
    onAddTask: (String, String, String, String) -> Unit,
    onDeleteTask: (String) -> Unit,
    examDeadlines: List<ExamDeadline>,
    onAddExam: (String, String, String, Int, String) -> Unit
) {
    val colors = DeekshiiTheme.colors
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var showAddExamDialog by remember { mutableStateOf(false) }

    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
    val filteredSchedule = remember(scheduleList, selectedDay) {
        scheduleList.filter { it.dayOfWeek.equals(selectedDay, ignoreCase = true) }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (activeSubTab == 0) showAddTaskDialog = true else showAddExamDialog = true
                },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                text = { Text(if (activeSubTab == 0) "New Task" else "Add Exam") },
                containerColor = colors.lavenderPrimary,
                contentColor = if (colors.isDark) Color(0xFF1B1429) else Color.White,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.testTag("schedule_fab")
            )
        },
        containerColor = colors.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("schedule_screen"),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Screen Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Schedule & Deadlines",
                        style = MaterialTheme.typography.displaySmall,
                        color = colors.lavenderPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Keep your B.Tech classes, tasks & exam countdown in harmony",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textSecondary
                    )
                }
            }

            // Tab Selector (Timetable & Tasks vs Exam Deadlines)
            item {
                PrimaryTabRow(
                    selectedTabIndex = activeSubTab,
                    containerColor = Color.Transparent,
                    contentColor = colors.lavenderPrimary,
                    indicator = {
                        TabRowDefaults.PrimaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(activeSubTab),
                            color = colors.lavenderPrimary,
                            height = 3.dp
                        )
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Tab(
                        selected = activeSubTab == 0,
                        onClick = { onSubTabChange(0) },
                        text = {
                            Text(
                                "Daily Work & Timetable",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = if (activeSubTab == 0) FontWeight.Bold else FontWeight.Normal,
                                color = if (activeSubTab == 0) colors.lavenderPrimary else colors.textMuted
                            )
                        }
                    )
                    Tab(
                        selected = activeSubTab == 1,
                        onClick = { onSubTabChange(1) },
                        text = {
                            Text(
                                "Exam Deadlines (${examDeadlines.size})",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = if (activeSubTab == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (activeSubTab == 1) colors.lavenderPrimary else colors.textMuted
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (activeSubTab == 0) {
                // Day selector pills
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(daysOfWeek) { day ->
                            val isSelected = day == selectedDay
                            FilterChip(
                                selected = isSelected,
                                onClick = { onSelectDay(day) },
                                label = { Text(day) },
                                shape = RoundedCornerShape(16.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = colors.lavenderPrimary,
                                    selectedLabelColor = if (colors.isDark) Color(0xFF1B1429) else Color.White,
                                    containerColor = colors.surface,
                                    labelColor = colors.textSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = if (isSelected) colors.lavenderPrimary else colors.outline
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Section: Timetable for selected day
                item {
                    Text(
                        text = "$selectedDay Lectures & Labs",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                    )
                }

                if (filteredSchedule.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = colors.surface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, colors.outline)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Weekend, contentDescription = null, tint = colors.mintPrimary, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("No scheduled lectures for $selectedDay", style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary)
                                Text("Time for self-paced project work or relaxation 🌸", style = MaterialTheme.typography.bodySmall, color = colors.textMuted)
                            }
                        }
                    }
                } else {
                    items(filteredSchedule) { item ->
                        TimetableItemCard(item = item)
                    }
                }

                // Section: Daily Tasks & Assignments
                item {
                    Spacer(modifier = Modifier.height(18.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Tasks, Labs & Submissions",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                            Text(
                                text = "${dailyTasks.count { !it.isCompleted }} pending • ${dailyTasks.count { it.isCompleted }} completed",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.textSecondary
                            )
                        }

                        IconButton(onClick = { showAddTaskDialog = true }) {
                            Icon(Icons.Default.AddCircle, contentDescription = "Add Task", tint = colors.lavenderPrimary)
                        }
                    }
                }

                items(dailyTasks) { task ->
                    TaskItemCard(
                        task = task,
                        onToggle = { onToggleTask(task.id) },
                        onDelete = { onDeleteTask(task.id) }
                    )
                }

            } else {
                // EXAM DEADLINES TAB
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.pinkContainer),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.pinkContainer)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(colors.surface),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.HourglassTop, contentDescription = null, tint = colors.pinkPrimary)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Semester Mid-2 & Final Vivas Approaching",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.pinkOnContainer
                                )
                                Text(
                                    text = "Maintain syllabus pace and avoid eleventh-hour stress.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.textSecondary
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                items(examDeadlines) { exam ->
                    ExamDeadlineCard(exam = exam)
                }
            }
        }
    }

    // Add Task Dialog
    if (showAddTaskDialog) {
        var title by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Assignment") }
        var dueDate by remember { mutableStateOf("Tomorrow, 5:00 PM") }
        var priority by remember { mutableStateOf("High") }

        AlertDialog(
            onDismissRequest = { showAddTaskDialog = false },
            title = { Text("Add Academic Task", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colors.textPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Task Title / Lab Experiment") },
                        placeholder = { Text("e.g. Finish Networks Socket Program") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category (Assignment, Lab Record, Revision)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = dueDate,
                        onValueChange = { dueDate = it },
                        label = { Text("Due Date / Time") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            onAddTask(title, category, dueDate, priority)
                            showAddTaskDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colors.lavenderPrimary)
                ) {
                    Text("Add Task")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddTaskDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }

    // Add Exam Dialog
    if (showAddExamDialog) {
        var examTitle by remember { mutableStateOf("") }
        var subjectCode by remember { mutableStateOf("CS601PC") }
        var date by remember { mutableStateOf("Oct 25, 2026") }
        var daysLeft by remember { mutableStateOf("14") }
        var type by remember { mutableStateOf("Mid-Term II") }

        AlertDialog(
            onDismissRequest = { showAddExamDialog = false },
            title = { Text("Add Exam Deadline", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colors.textPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = examTitle,
                        onValueChange = { examTitle = it },
                        label = { Text("Exam Name") },
                        placeholder = { Text("e.g. Compiler Design Mid-2") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = subjectCode,
                        onValueChange = { subjectCode = it },
                        label = { Text("Subject Code") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text("Date") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = daysLeft,
                        onValueChange = { daysLeft = it },
                        label = { Text("Days Left (Number)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (examTitle.isNotBlank()) {
                            onAddExam(examTitle, subjectCode, date, daysLeft.toIntOrNull() ?: 7, type)
                            showAddExamDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colors.lavenderPrimary)
                ) {
                    Text("Save Exam")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddExamDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
fun TimetableItemCard(item: ScheduleItem) {
    val colors = DeekshiiTheme.colors

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.outline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = if (item.isLab) colors.mintContainer else colors.lavenderContainer,
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = item.timeSlot.split(" - ").first(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (item.isLab) colors.mintOnContainer else colors.lavenderOnContainer
                    )
                    Text(
                        text = if (item.isLab) "LAB" else "THEORY",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (item.isLab) colors.mintPrimary else colors.lavenderPrimary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.subjectName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = colors.textMuted, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = item.roomNo, style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(Icons.Default.Person, contentDescription = null, tint = colors.textMuted, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = item.faculty, style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
                }
            }
        }
    }
}

@Composable
fun TaskItemCard(
    task: DailyTask,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val colors = DeekshiiTheme.colors

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.outline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = colors.lavenderPrimary,
                    uncheckedColor = colors.outline
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    color = if (task.isCompleted) colors.textMuted else colors.textPrimary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = when (task.category) {
                            "Assignment" -> colors.skyContainer
                            "Lab Record" -> colors.mintContainer
                            "Mini Project" -> colors.peachContainer
                            else -> colors.lavenderContainer
                        },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = task.category,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            color = colors.textPrimary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Due: ${task.dueDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "Delete Task", tint = colors.textMuted, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun ExamDeadlineCard(exam: ExamDeadline) {
    val colors = DeekshiiTheme.colors

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = exam.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Text(
                        text = "${exam.subjectCode} • ${exam.examType} • ${exam.hallNumber}",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textSecondary
                    )
                }

                Surface(
                    color = if (exam.daysRemaining <= 5) colors.pinkContainer else colors.lavenderContainer,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${exam.daysRemaining}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (exam.daysRemaining <= 5) colors.pinkPrimary else colors.lavenderPrimary
                        )
                        Text(
                            text = "days left",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.textSecondary,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Syllabus Progress Meter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Syllabus Revised: ${exam.syllabusProgress}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.textSecondary
                )
                Text(
                    text = exam.examDate,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.lavenderPrimary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { exam.syllabusProgress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = if (exam.syllabusProgress >= 75) colors.mintPrimary else colors.peachPrimary,
                trackColor = colors.surfaceVariant,
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
        }
    }
}
