package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

data class ButterflyParticle(
    val initialX: Float,
    val initialY: Float,
    val speedY: Float,
    val swayAmplitude: Float,
    val swaySpeed: Float,
    val size: Float,
    val wingColor: Color,
    val wingColor2: Color,
    val phaseOffset: Float
)

@Composable
fun ButterflyOpeningScreen(
    studentName: String = "Deekshitha",
    onTransitionComplete: () -> Unit
) {
    // Auto transition after a serene 3.5 seconds
    LaunchedEffect(Unit) {
        delay(3600L)
        onTransitionComplete()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "butterfly_motion")

    // Wing flapping animation (cycles quickly like a fluttering butterfly)
    val flapProgress by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(220, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flap"
    )

    // Gentle vertical and horizontal floating time
    val floatTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "floatTime"
    )

    // Pulsing aura for serene atmosphere
    val auraScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aura"
    )

    // Entrance fade for text elements
    var showText by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(400L)
        showText = true
    }

    // Static list of butterflies with unique motion patterns
    val butterflies = remember {
        listOf(
            ButterflyParticle(0.25f, 0.65f, 0.08f, 35f, 1.8f, 26f, Color(0xFFE1BEE7), Color(0xFFF8BBD0), 0.0f),
            ButterflyParticle(0.70f, 0.55f, 0.07f, 40f, 2.1f, 32f, Color(0xFFFCE4EC), Color(0xFFD1C4E9), 1.2f),
            ButterflyParticle(0.45f, 0.35f, 0.06f, 50f, 1.4f, 38f, Color(0xFFE8F5E9), Color(0xFFB3E5FC), 2.5f),
            ButterflyParticle(0.18f, 0.25f, 0.09f, 30f, 2.3f, 22f, Color(0xFFFFF3E0), Color(0xFFFFCDD2), 3.8f),
            ButterflyParticle(0.82f, 0.20f, 0.05f, 45f, 1.6f, 28f, Color(0xFFC5CAE9), Color(0xFFF3E5F5), 4.4f),
            ButterflyParticle(0.55f, 0.78f, 0.075f, 38f, 1.9f, 24f, Color(0xFFE0F2F1), Color(0xFFF8BBD0), 5.1f)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF6F9), // Soft blush cloud
                        Color(0xFFF5EEFA), // Gentle lavender mist
                        Color(0xFFEFF7F3), // Pale mint dawn
                        Color(0xFFFAF7FD)  // Soothing pearl
                    )
                )
            )
            .testTag("butterfly_opening_screen")
    ) {
        // Floating Butterfly Canvas with fluttering wings and magical particles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Gentle background radial pastel glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x35E1BEE7), Color(0x15F8BBD0), Color.Transparent),
                    center = Offset(width * 0.5f, height * 0.45f),
                    radius = (width * 0.65f) * auraScale
                ),
                center = Offset(width * 0.5f, height * 0.45f),
                radius = (width * 0.65f) * auraScale
            )

            // Sparkle particles
            for (i in 0..18) {
                val sparkX = ((width * 0.1f) + (i * 47f * 3.3f) % (width * 0.8f) + sin(floatTime * 0.8f + i) * 15f).toFloat()
                val sparkY = ((height * 0.85f - (floatTime * 40f + i * 55f) % (height * 0.75f))).toFloat()
                val sparkAlpha = ((sin(floatTime * 2f + i) + 1f) / 2f) * 0.6f
                drawCircle(
                    color = if (i % 2 == 0) Color(0xFF917BBF).copy(alpha = sparkAlpha) else Color(0xFFF398AE).copy(alpha = sparkAlpha),
                    radius = if (i % 3 == 0) 3.5f else 2f,
                    center = Offset(sparkX, sparkY)
                )
            }

            // Draw each fluttering butterfly
            butterflies.forEach { b ->
                val time = floatTime + b.phaseOffset
                // Upward floating motion with looping wrap-around
                val yProgress = (b.initialY * height - (time * 28f * (b.speedY + 0.05f))) % height
                val currentY = if (yProgress < -50f) height + 50f else yProgress
                val currentX = (b.initialX * width) + sin(time * b.swaySpeed * 0.3f) * b.swayAmplitude
                val flightAngle = cos(time * b.swaySpeed * 0.3f) * 18f - 15f // slight tilt in flight direction

                // Wing span compressed/expanded by sinusoidal flap
                val wingScale = (sin((time * 8f) + b.phaseOffset * 3f)).coerceIn(-1f, 1f)

                drawButterfly(
                    center = Offset(currentX, currentY),
                    size = b.size,
                    wingScale = wingScale,
                    rotationDegrees = flightAngle,
                    wingColorTop = b.wingColor,
                    wingColorBottom = b.wingColor2
                )
            }
        }

        // Elegant Text Overlay & Welcome Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 40.dp)
                .navigationBarsPadding()
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top serene badge
            Surface(
                color = PastelLavenderLight.copy(alpha = 0.75f),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, PastelLavenderPrimary.copy(alpha = 0.3f)),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Calm sparkles",
                        tint = PastelLavenderPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Calm Academic Sanctuary",
                        style = MaterialTheme.typography.labelMedium,
                        color = PastelLavenderOnContainer,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Center Welcome Card with elegant soft fonts
            AnimatedVisibility(
                visible = showText,
                enter = fadeIn(tween(800)) + scaleIn(tween(800), initialScale = 0.9f)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.88f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEADDF5))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Welcome to",
                            style = MaterialTheme.typography.titleMedium,
                            color = PastelTextSecondary,
                            letterSpacing = 1.2.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Deekshii",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontFamily = PlayfairFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 42.sp,
                                color = PastelLavenderPrimary
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Student Name in soft, elegant font
                        Text(
                            text = studentName,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontFamily = PlayfairFontFamily,
                                fontStyle = FontStyle.Italic,
                                fontSize = 26.sp,
                                color = PastelBlushPink
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Delicate divider
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(2.dp)
                                .clip(RoundedCornerShape(1.dp))
                                .background(PastelSurfaceOutline)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Dedicated portfolio tag requested by user
                        Text(
                            text = "Created by deekshitha for my personal student portfolio app",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.5.sp,
                                lineHeight = 18.sp,
                                color = PastelTextMuted
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            color = PastelPinkContainer.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "B.Tech CSE • Semester VI • Study & Schedule Hub",
                                style = MaterialTheme.typography.labelSmall,
                                color = PastelPinkOnContainer,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Bottom skip/enter action pill button
            Button(
                onClick = onTransitionComplete,
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PastelLavenderPrimary,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(54.dp)
                    .testTag("enter_dashboard_button")
            ) {
                Text(
                    text = "Enter Dashboard",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Proceed to Dashboard",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// Custom vector drawing for an aesthetic flapping butterfly
private fun DrawScope.drawButterfly(
    center: Offset,
    size: Float,
    wingScale: Float,
    rotationDegrees: Float,
    wingColorTop: Color,
    wingColorBottom: Color
) {
    rotate(degrees = rotationDegrees, pivot = center) {
        val wingSpan = (size * 0.9f) * wingScale.coerceIn(-1f, 1f)
        val wingHeight = size * 1.1f

        // Right Upper Wing
        val rightTopWing = Path().apply {
            moveTo(center.x, center.y - (size * 0.1f))
            cubicTo(
                center.x + wingSpan * 0.6f, center.y - wingHeight * 0.9f,
                center.x + wingSpan * 1.2f, center.y - wingHeight * 0.3f,
                center.x, center.y + (size * 0.1f)
            )
            close()
        }
        drawPath(
            path = rightTopWing,
            color = wingColorTop.copy(alpha = 0.88f)
        )

        // Left Upper Wing
        val leftTopWing = Path().apply {
            moveTo(center.x, center.y - (size * 0.1f))
            cubicTo(
                center.x - wingSpan * 0.6f, center.y - wingHeight * 0.9f,
                center.x - wingSpan * 1.2f, center.y - wingHeight * 0.3f,
                center.x, center.y + (size * 0.1f)
            )
            close()
        }
        drawPath(
            path = leftTopWing,
            color = wingColorTop.copy(alpha = 0.88f)
        )

        // Right Lower Wing
        val rightBottomWing = Path().apply {
            moveTo(center.x, center.y)
            cubicTo(
                center.x + wingSpan * 0.9f, center.y + wingHeight * 0.3f,
                center.x + wingSpan * 0.6f, center.y + wingHeight * 0.8f,
                center.x, center.y + (size * 0.4f)
            )
            close()
        }
        drawPath(
            path = rightBottomWing,
            color = wingColorBottom.copy(alpha = 0.82f)
        )

        // Left Lower Wing
        val leftBottomWing = Path().apply {
            moveTo(center.x, center.y)
            cubicTo(
                center.x - wingSpan * 0.9f, center.y + wingHeight * 0.3f,
                center.x - wingSpan * 0.6f, center.y + wingHeight * 0.8f,
                center.x, center.y + (size * 0.4f)
            )
            close()
        }
        drawPath(
            path = leftBottomWing,
            color = wingColorBottom.copy(alpha = 0.82f)
        )

        // Butterfly Body & Antennae
        drawLine(
            color = PastelTextPrimary.copy(alpha = 0.6f),
            start = Offset(center.x, center.y - size * 0.4f),
            end = Offset(center.x, center.y + size * 0.4f),
            strokeWidth = 2.5f
        )

        // Antennae
        drawLine(
            color = PastelTextPrimary.copy(alpha = 0.45f),
            start = Offset(center.x, center.y - size * 0.35f),
            end = Offset(center.x + size * 0.25f, center.y - size * 0.6f),
            strokeWidth = 1.2f
        )
        drawLine(
            color = PastelTextPrimary.copy(alpha = 0.45f),
            start = Offset(center.x, center.y - size * 0.35f),
            end = Offset(center.x - size * 0.25f, center.y - size * 0.6f),
            strokeWidth = 1.2f
        )
    }
}
