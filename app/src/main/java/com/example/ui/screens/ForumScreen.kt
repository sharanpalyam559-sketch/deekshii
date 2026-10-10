package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ForumPost
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumScreen(
    posts: List<ForumPost>,
    onToggleLike: (String) -> Unit,
    onAddReply: (String, String) -> Unit,
    onCreatePost: (String, String, String, String?, String?) -> Unit
) {
    val colors = DeekshiiTheme.colors
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("All") }
    var showCreatePostDialog by remember { mutableStateOf(false) }

    val categories = listOf("All", "Notes & PPTs", "Lab Solutions", "Study Groups", "Viva Prep", "Project Collab")

    val filteredPosts = remember(posts, selectedCategory) {
        if (selectedCategory == "All") posts else posts.filter { it.category == selectedCategory }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCreatePostDialog = true },
                icon = { Icon(Icons.Default.PostAdd, contentDescription = "Share Resource") },
                text = { Text("Share / Discuss") },
                containerColor = colors.lavenderPrimary,
                contentColor = if (colors.isDark) Color(0xFF1B1429) else Color.White,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.testTag("create_post_fab")
            )
        },
        containerColor = colors.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("forum_screen"),
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
                        text = "Peer Study & Resource Forum",
                        style = MaterialTheme.typography.displaySmall,
                        color = colors.lavenderPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Collaborative study sessions, Drive notes & lab solutions with classmates",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textSecondary
                    )
                }
            }

            // Categories Filter Bar
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        val isSelected = cat == selectedCategory
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat) },
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

            // Active Classmate Forum Posts
            items(filteredPosts) { post ->
                ForumPostCard(
                    post = post,
                    onToggleLike = { onToggleLike(post.id) },
                    onAddReply = { text -> onAddReply(post.id, text) },
                    onOpenResource = { link ->
                        val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as? android.content.ClipboardManager
                        val clip = android.content.ClipData.newPlainText("Resource Link", link)
                        clipboard?.setPrimaryClip(clip)
                        Toast.makeText(context, "Copied resource link to clipboard: $link", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    // Create Post Dialog
    if (showCreatePostDialog) {
        var title by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Notes & PPTs") }
        var content by remember { mutableStateOf("") }
        var resourceName by remember { mutableStateOf("") }
        var resourceLink by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showCreatePostDialog = false },
            title = { Text("Share Resource or Start Study Session", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Post Title") },
                        placeholder = { Text("e.g. Unit 4 Compiler Design Cheat Sheet") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category (Notes, Lab Solutions, Study Groups)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Discussion Details / Description") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4,
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = resourceName,
                        onValueChange = { resourceName = it },
                        label = { Text("Resource Name (Optional)") },
                        placeholder = { Text("e.g. CD_Unit4_Handwritten_Notes.pdf") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = resourceLink,
                        onValueChange = { resourceLink = it },
                        label = { Text("Drive / GitHub / Meet Link (Optional)") },
                        placeholder = { Text("e.g. drive.google.com/...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank() && content.isNotBlank()) {
                            onCreatePost(title, category, content, resourceName, resourceLink)
                            showCreatePostDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.lavenderPrimary,
                        contentColor = if (colors.isDark) Color(0xFF1B1429) else Color.White
                    )
                ) {
                    Text("Post to Forum")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreatePostDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
fun ForumPostCard(
    post: ForumPost,
    onToggleLike: () -> Unit,
    onAddReply: (String) -> Unit,
    onOpenResource: (String) -> Unit
) {
    val colors = DeekshiiTheme.colors
    var showReplies by remember { mutableStateOf(false) }
    var replyInput by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("forum_post_${post.id}"),
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
            // Author row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(colors.lavenderLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = post.authorName.take(1),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.lavenderPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = post.authorName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = colors.pinkContainer,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = post.authorBadge,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 9.sp,
                                    color = colors.pinkOnContainer,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = post.timeAgo,
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.textMuted
                        )
                    }
                }

                Surface(
                    color = colors.lavenderContainer,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = post.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.lavenderOnContainer,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title & Content
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary,
                lineHeight = 20.sp
            )

            // Resource banner (if attached)
            post.resourceName?.let { resName ->
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = colors.skyContainer,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenResource(post.resourceLink ?: resName) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AttachFile, contentDescription = null, tint = colors.skyPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = resName,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.skyOnContainer
                                )
                                Text(
                                    text = post.resourceLink ?: "Shared peer document",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colors.textSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }
                        Icon(Icons.Default.Download, contentDescription = "Download Resource", tint = colors.skyPrimary, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Footer Actions: Like, Comment count, Share
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Like button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable(onClick = onToggleLike)
                    ) {
                        Icon(
                            imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (post.isLiked) colors.pinkPrimary else colors.textMuted,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${post.likesCount}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (post.isLiked) colors.pinkPrimary else colors.textSecondary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Replies button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { showReplies = !showReplies }
                    ) {
                        Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Replies", tint = colors.textMuted, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${post.replies.size} replies",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.textSecondary
                        )
                    }
                }

                Text(
                    text = if (showReplies) "Hide discussion ▲" else "View replies ▼",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.lavenderPrimary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { showReplies = !showReplies }
                )
            }

            // Expanded Replies Section
            AnimatedVisibility(visible = showReplies) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                ) {
                    HorizontalDivider(color = colors.outline.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(10.dp))

                    post.replies.forEach { reply ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(colors.pinkContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = reply.authorName.take(1),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.pinkPrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = reply.authorName, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                                    Text(text = reply.timeAgo, style = MaterialTheme.typography.labelSmall, color = colors.textMuted, fontSize = 9.sp)
                                }
                                Text(text = reply.content, style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick reply input
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = replyInput,
                            onValueChange = { replyInput = it },
                            placeholder = { Text("Write a supportive reply or answer...", style = MaterialTheme.typography.bodySmall) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                if (replyInput.isNotBlank()) {
                                    onAddReply(replyInput)
                                    replyInput = ""
                                }
                            }
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Send Reply", tint = colors.lavenderPrimary)
                        }
                    }
                }
            }
        }
    }
}
