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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudentProfile
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PortfolioScreen(
    profile: StudentProfile,
    onUpdateProfile: (String, String, String, String, Double, Double, String) -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PastelBackground)
            .testTag("portfolio_screen"),
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
                    text = "Student Portfolio & Profile",
                    style = MaterialTheme.typography.displaySmall,
                    color = PastelLavenderPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Personal academic portfolio created by Deekshitha",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PastelTextSecondary
                )
            }
        }

        // Profile Identity Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = PastelSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, PastelSurfaceOutline)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(PastelLavenderLight)
                            .border(2.dp, PastelLavenderPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = profile.name.take(1),
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontFamily = PlayfairFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = PastelLavenderPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = profile.name,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontFamily = PlayfairFontFamily,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = PastelTextPrimary
                        )
                    )

                    Text(
                        text = profile.title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = PastelLavenderPrimary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "${profile.semester} • Roll: ${profile.rollNo}",
                        style = MaterialTheme.typography.bodySmall,
                        color = PastelTextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = profile.college,
                        style = MaterialTheme.typography.bodySmall,
                        color = PastelTextMuted
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // User attribution banner
                    Surface(
                        color = PastelPinkContainer,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Created by deekshitha for my personal student portfolio app",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontStyle = FontStyle.Italic,
                                color = PastelPinkOnContainer
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = profile.portfolioBio,
                        style = MaterialTheme.typography.bodyMedium,
                        color = PastelTextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = { showEditDialog = true },
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PastelLavenderPrimary)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Edit Profile Details", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }

        // Academic Milestones Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MilestoneStatCard(
                    title = "CGPA",
                    value = "%.2f".format(profile.cgpa),
                    sub = "Target: ${profile.targetCgpa}",
                    color = PastelLavenderPrimary,
                    containerColor = PastelLavenderContainer,
                    modifier = Modifier.weight(1f)
                )
                MilestoneStatCard(
                    title = "SGPA",
                    value = "%.2f".format(profile.currentSgpa),
                    sub = "Sem VI Current",
                    color = PastelSageMint,
                    containerColor = PastelMintContainer,
                    modifier = Modifier.weight(1f)
                )
                MilestoneStatCard(
                    title = "Standing",
                    value = "Top 5%",
                    sub = "In CSE Batch",
                    color = PastelBlushPink,
                    containerColor = PastelPinkContainer,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Technical Skills Cloud
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PastelSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, PastelSurfaceOutline)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Technical Skill Stack",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PastelTextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Wrapping pills
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        profile.technicalSkills.forEach { skill ->
                            Surface(
                                color = PastelLavenderContainer,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = skill,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = PastelLavenderOnContainer,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Featured Student Projects
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 8.dp)
            ) {
                Text(
                    text = "Engineering & Academic Projects",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PastelTextPrimary
                )
            }
        }

        item {
            ProjectShowcaseCard(
                title = "Deekshii - Academic Sanctuary & Schedule Hub",
                tag = "Android & Jetpack Compose",
                description = "Personal student portfolio app designed for female B.Tech students to track daily schedules, exam deadlines, attendance thresholds, marks visual trends, and collaborative peer forums.",
                accentColor = PastelLavenderPrimary
            )
        }

        item {
            ProjectShowcaseCard(
                title = "Breast Cancer Classification with SVMs",
                tag = "Machine Learning & Python",
                description = "Implemented RBF and Polynomial kernel SVM models to classify medical diagnostic records with 96.8% accuracy. Completed as ML Lab Experiment 7.",
                accentColor = PastelSageMint
            )
        }

        item {
            ProjectShowcaseCard(
                title = "Multi-Client TCP Chat Server",
                tag = "Computer Networks & Sockets",
                description = "Engineered a concurrent client-server communication protocol with asynchronous threading and socket timeout handling. Documented for peer sharing in study forum.",
                accentColor = PastelSkyBlue
            )
        }
    }

    // Edit Profile Dialog
    if (showEditDialog) {
        var name by remember { mutableStateOf(profile.name) }
        var rollNo by remember { mutableStateOf(profile.rollNo) }
        var branch by remember { mutableStateOf(profile.title) }
        var college by remember { mutableStateOf(profile.college) }
        var cgpa by remember { mutableStateOf(profile.cgpa.toString()) }
        var targetCgpa by remember { mutableStateOf(profile.targetCgpa.toString()) }
        var bio by remember { mutableStateOf(profile.portfolioBio) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Student Profile", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Student Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = rollNo,
                        onValueChange = { rollNo = it },
                        label = { Text("Roll Number") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = branch,
                        onValueChange = { branch = it },
                        label = { Text("Branch & Specialization") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = cgpa,
                            onValueChange = { cgpa = it },
                            label = { Text("Current CGPA") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = targetCgpa,
                            onValueChange = { targetCgpa = it },
                            label = { Text("Target CGPA") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                    OutlinedTextField(
                        value = bio,
                        onValueChange = { bio = it },
                        label = { Text("Portfolio Bio / Statement") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateProfile(
                            name,
                            rollNo,
                            branch,
                            college,
                            cgpa.toDoubleOrNull() ?: profile.cgpa,
                            targetCgpa.toDoubleOrNull() ?: profile.targetCgpa,
                            bio
                        )
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PastelLavenderPrimary)
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
fun MilestoneStatCard(
    title: String,
    value: String,
    sub: String,
    color: Color,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = PastelTextSecondary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = sub, style = MaterialTheme.typography.labelSmall, color = PastelTextMuted, fontSize = 10.sp)
        }
    }
}

@Composable
fun ProjectShowcaseCard(
    title: String,
    tag: String,
    description: String,
    accentColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = PastelSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, PastelSurfaceOutline)
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
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PastelTextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    color = accentColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = tag,
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = PastelTextSecondary,
                lineHeight = 18.sp
            )
        }
    }
}
