package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MotivationalAlert
import com.example.data.model.ProductivityTip
import com.example.ui.theme.*

@Composable
fun ProductivityScreen(
    pomodoroTotalSeconds: Int,
    pomodoroSecondsRemaining: Int,
    isPomodoroRunning: Boolean,
    sessionsCompleted: Int,
    onTogglePomodoro: () -> Unit,
    onResetPomodoro: (Int) -> Unit,
    motivationalAlerts: List<MotivationalAlert>,
    productivityTips: List<ProductivityTip>
) {
    val colors = DeekshiiTheme.colors

    val progress = if (pomodoroTotalSeconds > 0) {
        (pomodoroTotalSeconds - pomodoroSecondsRemaining).toFloat() / pomodoroTotalSeconds.toFloat()
    } else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(500),
        label = "pomodoro_progress"
    )

    val minutes = pomodoroSecondsRemaining / 60
    val seconds = pomodoroSecondsRemaining % 60
    val formattedTime = "%02d:%02d".format(minutes, seconds)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .testTag("productivity_screen"),
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
                    text = "Productivity & Calm Hub",
                    style = MaterialTheme.typography.displaySmall,
                    color = colors.lavenderPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Mindful focus timer, motivational alerts & academic balance tips",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.textSecondary
                )
            }
        }

        // Pomodoro Focus Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("pomodoro_card"),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.outline)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Calm Focus Timer",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                            Text(
                                text = "Deep focus without academic overwhelm",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.textSecondary
                            )
                        }

                        Surface(
                            color = colors.lavenderContainer,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "★ $sessionsCompleted Sessions",
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.lavenderPrimary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Circular Progress Canvas
                    Box(
                        modifier = Modifier.size(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeWidth = 14.dp.toPx()
                            val radius = (size.minDimension - strokeWidth) / 2f
                            val center = Offset(size.width / 2f, size.height / 2f)

                            // Background Track
                            drawCircle(
                                color = colors.lavenderLight,
                                radius = radius,
                                center = center,
                                style = Stroke(width = strokeWidth)
                            )

                            // Animated Foreground Arc
                            val sweepAngle = animatedProgress * 360f
                            drawArc(
                                brush = Brush.sweepGradient(
                                    listOf(colors.lavenderPrimary, colors.pinkPrimary, colors.mintPrimary, colors.lavenderPrimary)
                                ),
                                startAngle = -90f,
                                sweepAngle = sweepAngle,
                                useCenter = false,
                                topLeft = Offset(center.x - radius, center.y - radius),
                                size = Size(radius * 2, radius * 2),
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = formattedTime,
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.lavenderPrimary
                            )
                            Text(
                                text = if (isPomodoroRunning) "Mindful Study 🌸" else "Ready to Focus",
                                style = MaterialTheme.typography.labelMedium,
                                color = colors.textSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Controls: Play/Pause, Reset
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilledTonalButton(
                            onClick = { onResetPomodoro(pomodoroTotalSeconds / 60) },
                            shape = CircleShape,
                            colors = ButtonDefaults.filledTonalButtonColors(containerColor = colors.lavenderLight)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Reset Timer", tint = colors.lavenderPrimary)
                        }

                        Button(
                            onClick = onTogglePomodoro,
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isPomodoroRunning) colors.pinkPrimary else colors.lavenderPrimary,
                                contentColor = if (colors.isDark) Color(0xFF1B1429) else Color.White
                            ),
                            modifier = Modifier
                                .height(50.dp)
                                .padding(horizontal = 12.dp)
                                .testTag("pomodoro_toggle_button")
                        ) {
                            Icon(
                                imageVector = if (isPomodoroRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPomodoroRunning) "Pause" else "Start"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isPomodoroRunning) "Pause Session" else "Start Focusing",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quick duration chips
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SuggestionChip(
                            onClick = { onResetPomodoro(25) },
                            label = { Text("25m Standard") },
                            shape = RoundedCornerShape(12.dp)
                        )
                        SuggestionChip(
                            onClick = { onResetPomodoro(50) },
                            label = { Text("50m Deep Work") },
                            shape = RoundedCornerShape(12.dp)
                        )
                        SuggestionChip(
                            onClick = { onResetPomodoro(10) },
                            label = { Text("10m Rest") },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }

        // Section: Personalized Motivational Alerts
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 8.dp)
            ) {
                Text(
                    text = "Personalized Motivational Alerts",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                Text(
                    text = "Affirmations crafted for women in engineering & academic resilience",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary
                )
            }
        }

        items(motivationalAlerts) { alert ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.outline)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(colors.pinkContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Spa,
                            contentDescription = null,
                            tint = colors.pinkPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = alert.context,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = colors.lavenderPrimary
                            )
                            Surface(
                                color = colors.lavenderContainer,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = alert.category,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 9.sp,
                                    color = colors.lavenderOnContainer,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "“${alert.quote}”",
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic,
                            color = colors.textPrimary,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "— ${alert.author}",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.textMuted
                        )
                    }
                }
            }
        }

        // Section: Curated B.Tech Productivity Tips
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 8.dp)
            ) {
                Text(
                    text = "B.Tech Engineering Productivity Strategies",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                Text(
                    text = "High-impact study techniques tested for tech courses",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary
                )
            }
        }

        items(productivityTips) { tip ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(colors.mintContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = colors.mintPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = tip.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                        }

                        Surface(
                            color = colors.mintContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = tip.category,
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.mintOnContainer,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = tip.tip,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}
