package com.example.goalgrid.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.goalgrid.data.Habit
import com.example.goalgrid.ui.components.GlassCard
import com.example.goalgrid.ui.theme.CyanNeon
import com.example.goalgrid.ui.theme.MagentaNeon
import com.example.goalgrid.ui.theme.PurpleNeon
import com.example.goalgrid.ui.theme.SuccessGreen
import com.example.goalgrid.ui.theme.TextSecondary
import com.example.goalgrid.ui.viewmodel.HabitViewModel

@Composable
fun HabitsScreen(
    viewModel: HabitViewModel,
    onEditHabit: (Habit) -> Unit
) {
    val habits by viewModel.habits.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        Text(
            text = "Your Habits",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(habits) { habit ->
                HabitCard(
                    habit = habit,
                    onIncrement = { viewModel.incrementHabit(habit) },
                    onEditClick = { onEditHabit(habit) }
                )
            }
        }
    }
}

@Composable
fun HabitCard(
    habit: Habit,
    onIncrement: () -> Unit,
    onEditClick: () -> Unit
) {
    val progress = if (habit.dailyTarget > 0) habit.currentCountToday.toFloat() / habit.dailyTarget else 0f
    
    GlassCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(60.dp)
            ) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    color = if (progress >= 1f) SuccessGreen else CyanNeon,
                    strokeWidth = 6.dp,
                    trackColor = Color.White.copy(alpha = 0.1f)
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.White)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                )
                Text(
                    text = "${habit.currentCountToday} / ${habit.dailyTarget} times today",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = getPriorityColor(habit.priority).copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "Priority: ${habit.priority}",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = getPriorityColor(habit.priority)
                        )
                    )
                }
            }

            IconButton(onClick = onIncrement) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increment",
                    tint = MagentaNeon
                )
            }

            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = CyanNeon
                )
            }
        }
    }
}

fun getPriorityColor(priority: Int): Color {
    return when {
        priority < 33 -> SuccessGreen
        priority < 66 -> PurpleNeon
        else -> MagentaNeon
    }
}
