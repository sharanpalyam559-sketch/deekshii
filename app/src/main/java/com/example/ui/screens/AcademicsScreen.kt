package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudentProfile
import com.example.data.model.SubjectRecord
import com.example.ui.components.AcademicProgressRadialCard
import com.example.ui.components.AttendanceTrendDiagram
import com.example.ui.components.MarksGrowthDiagram
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcademicsScreen(
    profile: StudentProfile,
    subjects: List<SubjectRecord>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onMarkAttendance: (String, Boolean) -> Unit,
    onUpdateMarks: (String, Double, Double, Double, Double) -> Unit,
    onAddSubject: (String, String, String, Int, Int, Int) -> Unit
) {
    var showAddSubjectDialog by remember { mutableStateOf(false) }
    var editingSubject by remember { mutableStateOf<SubjectRecord?>(null) }

    // Summary calculations
    val totalAttended = subjects.sumOf { it.attendedClasses }
    val totalConducted = subjects.sumOf { it.totalClasses }
    val overallPct = if (totalConducted > 0) (totalAttended.toDouble() / totalConducted.toDouble()) * 100.0 else 100.0

    Scaffold(
        floatingActionButton = {
            if (selectedTab == 0) {
                ExtendedFloatingActionButton(
                    onClick = { showAddSubjectDialog = true },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add Subject") },
                    text = { Text("Add Subject") },
                    containerColor = PastelLavenderPrimary,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("add_subject_fab")
                )
            }
        },
        containerColor = PastelBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("academics_screen"),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = "Academics & Analytics",
                        style = MaterialTheme.typography.displaySmall,
                        color = PastelLavenderPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Attendance cutoff tracker, marks progression & semester trends",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PastelTextSecondary
                    )
                }
            }

            // Radial CGPA Card
            item {
                AcademicProgressRadialCard(
                    cgpa = profile.cgpa,
                    targetCgpa = profile.targetCgpa,
                    sgpa = profile.currentSgpa,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Tabs Selector: Overview & Subjects vs Diagrams & Visual Trends
            item {
                PrimaryTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = PastelLavenderPrimary,
                    indicator = {
                        TabRowDefaults.PrimaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(selectedTab),
                            color = PastelLavenderPrimary,
                            height = 3.dp
                        )
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { onTabSelected(0) },
                        text = {
                            Text(
                                "Subjects & Attendance (${subjects.size})",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { onTabSelected(1) },
                        text = {
                            Text(
                                "Diagrams & Academic Trends",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            if (selectedTab == 0) {
                // SUBJECTS LIST TAB
                item {
                    // Overall Attendance Bar
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (overallPct >= 75f) PastelMintContainer else PastelPinkContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Overall Semester Attendance",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (overallPct >= 75f) PastelMintOnContainer else PastelPinkOnContainer
                                )
                                Text(
                                    text = "$totalAttended attended of $totalConducted total classes",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = PastelTextSecondary
                                )
                            }

                            Text(
                                text = "%.1f%%".format(overallPct),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (overallPct >= 75f) PastelSageMint else PastelBlushPink
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                items(subjects) { subject ->
                    SubjectDetailCard(
                        subject = subject,
                        onMarkPresent = { onMarkAttendance(subject.id, true) },
                        onMarkAbsent = { onMarkAttendance(subject.id, false) },
                        onEditMarks = { editingSubject = subject }
                    )
                }

            } else {
                // DIAGRAMS & VISUAL TRENDS TAB
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // 1. Attendance Diagram vs 75% Cutoff
                        AttendanceTrendDiagram(
                            subjects = subjects
                        )

                        // 2. Marks Growth Diagram (Mid-1 vs Mid-2 progression)
                        MarksGrowthDiagram(
                            subjects = subjects
                        )

                        // 3. Subject-wise marks summary table
                        SubjectMarksSummaryCard(subjects = subjects)
                    }
                }
            }
        }
    }

    // Add Subject Dialog
    if (showAddSubjectDialog) {
        var name by remember { mutableStateOf("") }
        var code by remember { mutableStateOf("") }
        var faculty by remember { mutableStateOf("") }
        var credits by remember { mutableStateOf("3") }
        var attended by remember { mutableStateOf("30") }
        var total by remember { mutableStateOf("35") }

        AlertDialog(
            onDismissRequest = { showAddSubjectDialog = false },
            title = { Text("Add Subject & Attendance Record", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Subject Name") },
                        placeholder = { Text("e.g. Distributed Systems") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it },
                        label = { Text("Subject Code") },
                        placeholder = { Text("e.g. CS608PE") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = faculty,
                        onValueChange = { faculty = it },
                        label = { Text("Faculty / Professor") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = attended,
                            onValueChange = { attended = it },
                            label = { Text("Attended") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp)
                        )
                        OutlinedTextField(
                            value = total,
                            onValueChange = { total = it },
                            label = { Text("Total Classes") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            onAddSubject(
                                name,
                                code.ifBlank { "CS600" },
                                faculty.ifBlank { "CSE Faculty" },
                                credits.toIntOrNull() ?: 3,
                                attended.toIntOrNull() ?: 0,
                                total.toIntOrNull() ?: 1
                            )
                            showAddSubjectDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PastelLavenderPrimary)
                ) {
                    Text("Add Subject")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddSubjectDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }

    // Edit Marks Dialog
    editingSubject?.let { sub ->
        var mid1 by remember { mutableStateOf(sub.mid1Marks.toString()) }
        var mid2 by remember { mutableStateOf(sub.mid2Marks.toString()) }
        var internal by remember { mutableStateOf(sub.internalMarks.toString()) }
        var target by remember { mutableStateOf(sub.targetMarks.toString()) }

        AlertDialog(
            onDismissRequest = { editingSubject = null },
            title = {
                Text(
                    text = "Update Marks: ${sub.name}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = mid1,
                        onValueChange = { mid1 = it },
                        label = { Text("Mid-Term 1 Marks (out of 30)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = mid2,
                        onValueChange = { mid2 = it },
                        label = { Text("Mid-Term 2 Marks (out of 30)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = internal,
                        onValueChange = { internal = it },
                        label = { Text("Total Internal Score (out of 40)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = target,
                        onValueChange = { target = it },
                        label = { Text("Target Semester Score (out of 100)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateMarks(
                            sub.id,
                            mid1.toDoubleOrNull() ?: sub.mid1Marks,
                            mid2.toDoubleOrNull() ?: sub.mid2Marks,
                            internal.toDoubleOrNull() ?: sub.internalMarks,
                            target.toDoubleOrNull() ?: sub.targetMarks
                        )
                        editingSubject = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PastelLavenderPrimary)
                ) {
                    Text("Save Marks")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingSubject = null }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
fun SubjectDetailCard(
    subject: SubjectRecord,
    onMarkPresent: () -> Unit,
    onMarkAbsent: () -> Unit,
    onEditMarks: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("subject_card_${subject.code.lowercase()}"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = PastelSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, PastelSurfaceOutline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Code, Name, Faculty
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = PastelLavenderContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = subject.code,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = PastelLavenderPrimary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${subject.credits} Credits",
                            style = MaterialTheme.typography.labelSmall,
                            color = PastelTextMuted
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subject.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PastelTextPrimary
                    )
                    Text(
                        text = subject.faculty,
                        style = MaterialTheme.typography.bodySmall,
                        color = PastelTextSecondary
                    )
                }

                // Attendance Circle Badge
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "%.1f%%".format(subject.attendancePercentage),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (subject.isSafeAttendance) PastelSageMint else PastelBlushPink
                    )
                    Text(
                        text = "${subject.attendedClasses}/${subject.totalClasses} classes",
                        style = MaterialTheme.typography.labelSmall,
                        color = PastelTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bunk Calculator / Safe status Banner
            Surface(
                color = if (subject.isSafeAttendance) PastelMintContainer else PastelPinkContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = subject.bunkStatusText,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (subject.isSafeAttendance) PastelMintOnContainer else PastelPinkOnContainer,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Marks Breakdown Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PastelBackground, RoundedCornerShape(14.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MarksChip(label = "Mid-1", score = "${subject.mid1Marks.toInt()}/30")
                MarksChip(label = "Mid-2", score = "${subject.mid2Marks.toInt()}/30")
                MarksChip(label = "Internal", score = "${subject.internalMarks.toInt()}/40")
                MarksChip(label = "Target", score = "${subject.targetMarks.toInt()}/100")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons: 1-Tap Attendance & Edit Marks
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalButton(
                        onClick = onMarkPresent,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = PastelMintContainer,
                            contentColor = PastelMintOnContainer
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Present", style = MaterialTheme.typography.labelMedium)
                    }

                    FilledTonalButton(
                        onClick = onMarkAbsent,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = PastelPinkContainer,
                            contentColor = PastelPinkOnContainer
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Absent", style = MaterialTheme.typography.labelMedium)
                    }
                }

                TextButton(
                    onClick = onEditMarks,
                    colors = ButtonDefaults.textButtonColors(contentColor = PastelLavenderPrimary)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Marks", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable
fun MarksChip(label: String, score: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = PastelTextMuted, fontSize = 10.sp)
        Text(text = score, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = PastelTextPrimary)
    }
}

@Composable
fun SubjectMarksSummaryCard(subjects: List<SubjectRecord>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = PastelSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, PastelSurfaceOutline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Subject Performance Matrix",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PastelTextPrimary
            )
            Text(
                text = "Detailed score card across all Semester VI courses",
                style = MaterialTheme.typography.bodySmall,
                color = PastelTextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            subjects.forEach { sub ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = sub.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = PastelTextPrimary
                        )
                        Text(
                            text = "${sub.code} • ${sub.credits} Credits • Internal: ${sub.internalMarks.toInt()}/40",
                            style = MaterialTheme.typography.bodySmall,
                            color = PastelTextSecondary
                        )
                    }

                    Surface(
                        color = if (sub.mid2Marks >= 27) PastelMintContainer else PastelSkyContainer,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = if (sub.mid2Marks >= 27) "Grade: A+" else "Grade: A",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (sub.mid2Marks >= 27) PastelMintOnContainer else PastelSkyOnContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                if (sub != subjects.last()) {
                    HorizontalDivider(color = PastelSurfaceOutline.copy(alpha = 0.5f))
                }
            }
        }
    }
}
