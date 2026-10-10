package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SubjectRecord
import com.example.ui.theme.*

@Composable
fun AttendanceTrendDiagram(
    subjects: List<SubjectRecord>,
    modifier: Modifier = Modifier,
    onSubjectClick: (SubjectRecord) -> Unit = {}
) {
    val colors = DeekshiiTheme.colors
    var selectedSubjectId by remember { mutableStateOf<String?>(null) }
    val selectedSubject = subjects.find { it.id == selectedSubjectId }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("attendance_trend_diagram_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Attendance Visualizer",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Text(
                        text = "Comparative semester trend vs 75% cutoff",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textSecondary
                    )
                }

                // Legend chip
                Surface(
                    color = colors.mintContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(colors.mintPrimary, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "≥ 75% Safe",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.mintOnContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Custom Canvas Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 24.dp, top = 16.dp, start = 8.dp, end = 8.dp)
                ) {
                    val width = size.width
                    val height = size.height
                    val barCount = subjects.size
                    if (barCount == 0) return@Canvas

                    val availableWidth = width
                    val barSpacing = availableWidth / (barCount * 2f + 1f)
                    val barWidth = barSpacing * 1.3f

                    // Draw 75% Reference Threshold Line
                    val cutoffY = height * (1f - 0.75f)
                    drawLine(
                        color = colors.pinkPrimary.copy(alpha = 0.85f),
                        start = Offset(0f, cutoffY),
                        end = Offset(width, cutoffY),
                        strokeWidth = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
                    )

                    // Draw 50% faint grid line
                    val halfY = height * 0.5f
                    drawLine(
                        color = colors.outline.copy(alpha = 0.5f),
                        start = Offset(0f, halfY),
                        end = Offset(width, halfY),
                        strokeWidth = 1.dp.toPx()
                    )

                    // Draw Bars for each subject
                    subjects.forEachIndexed { index, sub ->
                        val pct = (sub.attendancePercentage / 100f).coerceIn(0f, 1f)
                        val barHeight = height * pct
                        val x = barSpacing + (index * (barWidth + barSpacing))
                        val y = height - barHeight

                        val isSafe = sub.attendancePercentage >= 75f
                        val isSelected = sub.id == selectedSubjectId

                        val barBrush = if (isSafe) {
                            Brush.verticalGradient(
                                colors = listOf(
                                    colors.mintPrimary,
                                    colors.mintPrimary.copy(alpha = 0.75f)
                                )
                            )
                        } else {
                            Brush.verticalGradient(
                                colors = listOf(
                                    colors.pinkPrimary,
                                    colors.pinkPrimary.copy(alpha = 0.75f)
                                )
                            )
                        }

                        // Bar
                        drawRoundRect(
                            brush = barBrush,
                            topLeft = Offset(x, y),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                        )

                        if (isSelected) {
                            drawRoundRect(
                                color = colors.lavenderPrimary,
                                topLeft = Offset(x - 2.dp.toPx(), y - 2.dp.toPx()),
                                size = Size(barWidth + 4.dp.toPx(), barHeight + 4.dp.toPx()),
                                cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx()),
                                style = Stroke(width = 2.dp.toPx())
                            )
                        }
                    }
                }
            }

            // Labels row under the chart
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                subjects.forEach { sub ->
                    val isSafe = sub.attendancePercentage >= 75f
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable {
                                selectedSubjectId = if (selectedSubjectId == sub.id) null else sub.id
                                onSubjectClick(sub)
                            }
                            .padding(horizontal = 2.dp)
                    ) {
                        Text(
                            text = "${sub.attendancePercentage.toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isSafe) colors.mintPrimary else colors.pinkPrimary
                        )
                        Text(
                            text = sub.code.takeLast(4),
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.textSecondary,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Selected Subject Details Toast/Card
            selectedSubject?.let { sub ->
                Surface(
                    color = if (sub.isSafeAttendance) colors.mintContainer else colors.pinkContainer,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${sub.name} (${sub.code})",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (sub.isSafeAttendance) colors.mintOnContainer else colors.pinkOnContainer
                            )
                            Text(
                                text = "${sub.attendedClasses} attended of ${sub.totalClasses} classes • ${sub.bunkStatusText}",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.textSecondary
                            )
                        }
                    }
                }
            } ?: run {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Dashed line denotes 75% university cutoff threshold. Tap any bar for details.",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun MarksGrowthDiagram(
    subjects: List<SubjectRecord>,
    modifier: Modifier = Modifier
) {
    val colors = DeekshiiTheme.colors

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("marks_growth_diagram_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Marks & Internal Trends",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Text(
                        text = "Mid-1 vs Mid-2 score progression (out of 30)",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textSecondary
                    )
                }

                // Legend
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(colors.lavenderPrimary, CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Mid-1", style = MaterialTheme.typography.labelSmall, color = colors.textSecondary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.size(8.dp).background(colors.skyPrimary, CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Mid-2", style = MaterialTheme.typography.labelSmall, color = colors.textSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Multi-bar comparative chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 20.dp, top = 10.dp, start = 8.dp, end = 8.dp)
                ) {
                    val width = size.width
                    val height = size.height
                    val count = subjects.size
                    if (count == 0) return@Canvas

                    val groupWidth = width / count
                    val barWidth = groupWidth * 0.32f
                    val maxMarks = 30f

                    // Grid lines (10, 20, 30 marks)
                    for (i in 1..3) {
                        val markLevelY = height * (1f - (i * 10f / maxMarks))
                        drawLine(
                            color = colors.outline.copy(alpha = 0.4f),
                            start = Offset(0f, markLevelY),
                            end = Offset(width, markLevelY),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    subjects.forEachIndexed { index, sub ->
                        val groupStartX = index * groupWidth + (groupWidth * 0.15f)

                        // Mid-1 bar
                        val mid1Height = height * (sub.mid1Marks.toFloat() / maxMarks).coerceIn(0f, 1f)
                        val mid1Y = height - mid1Height
                        drawRoundRect(
                            color = colors.lavenderPrimary.copy(alpha = 0.85f),
                            topLeft = Offset(groupStartX, mid1Y),
                            size = Size(barWidth, mid1Height),
                            cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                        )

                        // Mid-2 bar
                        val mid2Height = height * (sub.mid2Marks.toFloat() / maxMarks).coerceIn(0f, 1f)
                        val mid2Y = height - mid2Height
                        drawRoundRect(
                            color = colors.skyPrimary.copy(alpha = 0.9f),
                            topLeft = Offset(groupStartX + barWidth + 3.dp.toPx(), mid2Y),
                            size = Size(barWidth, mid2Height),
                            cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                        )
                    }
                }
            }

            // Subject label row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                subjects.forEach { sub ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = sub.code.takeLast(4),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.textPrimary,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "${sub.mid1Marks.toInt()} → ${sub.mid2Marks.toInt()}",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.textMuted,
                            fontSize = 9.5.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                color = colors.lavenderContainer,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📈 Growth Insight: Consistent upward trend in Mid-2 marks (+1.8 marks average gain across subjects).",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.lavenderOnContainer
                    )
                }
            }
        }
    }
}

@Composable
fun AcademicProgressRadialCard(
    cgpa: Double,
    targetCgpa: Double,
    sgpa: Double,
    modifier: Modifier = Modifier
) {
    val colors = DeekshiiTheme.colors

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("academic_radial_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.outline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Radial Progress Ring
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeW = 10.dp.toPx()
                    val radius = (size.minDimension - strokeW) / 2f
                    val center = Offset(size.width / 2f, size.height / 2f)

                    // Background track
                    drawCircle(
                        color = colors.lavenderLight,
                        radius = radius,
                        center = center,
                        style = Stroke(width = strokeW)
                    )

                    // Foreground CGPA progress arc (out of 10)
                    val sweepAngle = ((cgpa / 10.0) * 360.0).toFloat()
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(colors.lavenderPrimary, colors.pinkPrimary, colors.lavenderPrimary)
                        ),
                        startAngle = -90f,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = strokeW, cap = androidx.compose.ui.graphics.StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "%.2f".format(cgpa),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.lavenderPrimary
                    )
                    Text(
                        text = "CGPA",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Right side Metrics & Goals
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Semester SGPA",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.textSecondary
                        )
                        Text(
                            text = "%.2f".format(sgpa),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.mintPrimary
                        )
                    }

                    Column {
                        Text(
                            text = "Target CGPA",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.textSecondary
                        )
                        Text(
                            text = "%.2f".format(targetCgpa),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.pinkPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = { (cgpa / targetCgpa).toFloat().coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = colors.lavenderPrimary,
                    trackColor = colors.lavenderLight,
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "96.9% of target achieved • Top 5% in CSE cohort",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.textMuted
                )
            }
        }
    }
}
