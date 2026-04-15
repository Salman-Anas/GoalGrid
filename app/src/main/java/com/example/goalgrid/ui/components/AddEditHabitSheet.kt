package com.example.goalgrid.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.goalgrid.data.Habit
import com.example.goalgrid.ui.theme.CyanNeon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditHabitSheet(
    habit: Habit? = null,
    onDismiss: () -> Unit,
    onSave: (Habit) -> Unit
) {
    var name by remember { mutableStateOf(habit?.name ?: "") }
    var dailyTarget by remember { mutableStateOf(habit?.dailyTarget?.toFloat() ?: 5f) }
    var priority by remember { mutableStateOf(habit?.priority?.toFloat() ?: 50f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .navigationBarsPadding()
    ) {
        Text(
            text = if (habit == null) "New Habit" else "Edit Habit",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Habit Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Daily Target: ${dailyTarget.toInt()}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
        Slider(
            value = dailyTarget,
            onValueChange = { dailyTarget = it },
            valueRange = 1f..50f,
            steps = 49,
            colors = SliderDefaults.colors(thumbColor = CyanNeon, activeTrackColor = CyanNeon)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Priority: ${priority.toInt()}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
        Slider(
            value = priority,
            onValueChange = { priority = it },
            valueRange = 1f..100f,
            colors = SliderDefaults.colors(thumbColor = CyanNeon, activeTrackColor = CyanNeon)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val newHabit = habit?.copy(
                    name = name,
                    dailyTarget = dailyTarget.toInt(),
                    priority = priority.toInt()
                ) ?: Habit(
                    name = name,
                    dailyTarget = dailyTarget.toInt(),
                    priority = priority.toInt()
                )
                onSave(newHabit)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = Color.Black)
        ) {
            Text("Save Habit")
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
