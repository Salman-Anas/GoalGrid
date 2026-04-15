package com.example.goalgrid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goalgrid.data.Task
import com.example.goalgrid.ui.theme.CyanNeon
import com.example.goalgrid.ui.theme.DarkBg
import com.example.goalgrid.ui.theme.MagentaNeon
import com.example.goalgrid.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskSheet(
    task: Task? = null,
    onDismiss: () -> Unit,
    onSave: (Task) -> Unit
) {
    var title by remember { mutableStateOf(task?.title ?: "") }
    var notes by remember { mutableStateOf(task?.notes ?: "") }
    var dueTime by remember { mutableStateOf(task?.dueTime ?: "12:00 PM") }
    var isAlarmEnabled by remember { mutableStateOf(task?.isAlarmEnabled ?: false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .padding(horizontal = 24.dp)
            .navigationBarsPadding()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
            
            Text(
                text = if (task == null) "New Task" else "Edit Task",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )

            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        val newTask = task?.copy(
                            title = title,
                            notes = notes,
                            isAlarmEnabled = isAlarmEnabled,
                            dueTime = dueTime
                        ) ?: Task(
                            title = title,
                            notes = notes,
                            dueDate = System.currentTimeMillis(),
                            dueTime = dueTime,
                            isAlarmEnabled = isAlarmEnabled
                        )
                        onSave(newTask)
                    }
                }
            ) {
                Text("Save", color = CyanNeon, fontWeight = FontWeight.Bold)
            }
        }

        // Notepad-style Editor
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // Title Input
            BasicTextField(
                value = title,
                onValueChange = { title = it },
                textStyle = MaterialTheme.typography.headlineMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                cursorBrush = SolidColor(CyanNeon),
                decorationBox = { innerTextField ->
                    if (title.isEmpty()) {
                        Text(
                            "Task Title...",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = Color.White.copy(alpha = 0.3f),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Metadata Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Alarm, contentDescription = null, tint = MagentaNeon, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = dueTime,
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                )
                Spacer(modifier = Modifier.width(24.dp))
                Icon(Icons.Default.EventNote, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Divider(color = Color.White.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(24.dp))

            // Notes Input (The Notepad Part)
            BasicTextField(
                value = notes,
                onValueChange = { notes = it },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White.copy(alpha = 0.8f),
                    lineHeight = 28.sp
                ),
                cursorBrush = SolidColor(CyanNeon),
                decorationBox = { innerTextField ->
                    if (notes.isEmpty()) {
                        Text(
                            "Start writing notes here...",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color.White.copy(alpha = 0.3f)
                            )
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }

        // Bottom Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Alarm, contentDescription = null, tint = if (isAlarmEnabled) CyanNeon else TextSecondary)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Remind me", color = if (isAlarmEnabled) Color.White else TextSecondary)
            }
            Switch(
                checked = isAlarmEnabled,
                onCheckedChange = { isAlarmEnabled = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Black,
                    checkedTrackColor = CyanNeon,
                    uncheckedThumbColor = TextSecondary,
                    uncheckedTrackColor = Color.Transparent
                )
            )
        }
    }
}
