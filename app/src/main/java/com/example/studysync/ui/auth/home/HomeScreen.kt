package com.example.studysync.ui.auth.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.ui.Alignment
import com.example.studysync.domain.model.User


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("StudySync") },
                actions = {
                    IconButton(onClick = { viewModel.toggleEditProfile() }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                    IconButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // Welcome Section
                    item {
                        WelcomeSection(userName = state.user?.displayName ?: "")
                    }

                    // Profile Section (when editing)
                    if (state.isEditingProfile) {
                        item {
                            ProfileSection(
                                user = state.user,
                                onSave = { updatedUser ->
                                    viewModel.updateUserProfile(updatedUser)
                                }
                            )
                        }
                    }

                    // Study Stats Section
                    item {
                        StudyStatsSection(stats = state.studyStats)
                    }

                    // Courses Section
                    item {
                        CoursesHeader()
                    }
                    item {
                        CoursesList(courses = state.courses)
                    }

                    // Tasks Section
                    item {
                        TasksHeader()
                    }
                    items(state.upcomingTasks) { task ->
                        TaskItem(
                            task = task,
                            onCheckedChange = { isCompleted ->
                                viewModel.onTaskCheckedChange(task.id, isCompleted)
                            }
                        )
                    }
                }

                // Error Snackbar
                if (state.error != null) {
                    ErrorSnackbar(
                        error = state.error!!,
                        onDismiss = { viewModel.clearError() }
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileSection(
    user: User?,
    onSave: (User) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            user?.let { currentUser ->
                OutlinedTextField(
                    value = currentUser.displayName,
                    onValueChange = { newName ->
                        onSave(currentUser.copy(displayName = newName))
                    },
                    label = { Text("Display Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = currentUser.email,
                    onValueChange = { },
                    label = { Text("Email") },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                )

                // Add more fields as needed
            }
        }
    }
}

@Composable
fun CoursesHeader() {
    Text(
        text = "Your Courses",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun CoursesList(courses: List<Course>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(courses) { course ->
            CourseCard(course = course)
        }
    }
}

@Composable
fun TasksHeader() {
    Text(
        text = "Upcoming Tasks",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun ErrorSnackbar(
    error: String,
    onDismiss: () -> Unit
) {
    Snackbar(
        modifier = Modifier
            .padding(16.dp),
        action = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
    ) {
        Text(error)
    }
}

@Composable
fun WelcomeSection(userName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Welcome back,",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = userName,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
fun StudyStatsSection(stats: StudyStats) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Study Statistics",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    icon = Icons.Default.Timer,
                    value = "${stats.totalStudyTime / 60}h ${stats.totalStudyTime % 60}m",
                    label = "Study Time"
                )
                StatItem(
                    icon = Icons.Default.CheckCircle,
                    value = "${stats.completedTasks}",
                    label = "Completed"
                )
                StatItem(
                    icon = Icons.Default.Whatshot,
                    value = "${stats.streakDays}",
                    label = "Day Streak"
                )
            }
        }
    }
}

@Composable
fun StatItem(
    icon: ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = label)
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseCard(course: Course) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(120.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = course.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = course.progress,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "${(course.progress * 100).toInt()}% Complete",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onCheckedChange
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Due: ${task.dueDate}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            PriorityIndicator(priority = task.priority)
        }
    }
}

@Composable
fun PriorityIndicator(priority: TaskPriority) {
    val color = when (priority) {
        TaskPriority.HIGH -> MaterialTheme.colorScheme.error
        TaskPriority.MEDIUM -> MaterialTheme.colorScheme.tertiary
        TaskPriority.LOW -> MaterialTheme.colorScheme.primary
    }

    Icon(
        imageVector = Icons.Default.Flag,
        contentDescription = "Priority ${priority.name}",
        tint = color
    )
}