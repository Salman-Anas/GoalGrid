package com.example.goalgrid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.goalgrid.data.Habit
import com.example.goalgrid.data.Task
import com.example.goalgrid.ui.components.AddEditHabitSheet
import com.example.goalgrid.ui.components.AddEditTaskSheet
import com.example.goalgrid.ui.components.GlassCard
import com.example.goalgrid.ui.screens.HabitsScreen
import com.example.goalgrid.ui.screens.TasksScreen
import com.example.goalgrid.ui.theme.*
import com.example.goalgrid.ui.viewmodel.HabitViewModel
import com.example.goalgrid.ui.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GoalGridTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val taskViewModel: TaskViewModel = hiltViewModel()
    val habitViewModel: HabitViewModel = hiltViewModel()
    
    var currentTab by remember { mutableIntStateOf(0) } // 0 for Tasks, 1 for Habits
    
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }
    var editingHabit by remember { mutableStateOf<Habit?>(null) }

    Scaffold(
        containerColor = DarkBg,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingTask = null
                    editingHabit = null
                    showSheet = true
                },
                shape = CircleShape,
                containerColor = Color.Transparent,
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        brush = Brush.linearGradient(listOf(CyanNeon, PurpleNeon)),
                        shape = CircleShape
                    ),
                elevation = FloatingActionButtonDefaults.elevation(0.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black, modifier = Modifier.size(32.dp))
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Sleek Header with Alarm Section
            AlarmHeaderSection()

            // Modern Tab Switcher
            CustomTabSwitcher(
                selectedTab = currentTab,
                onTabSelected = { currentTab = it }
            )

            // Content Area with Animation
            Box(modifier = Modifier.weight(1f)) {
                this@Column.AnimatedVisibility(
                    visible = currentTab == 0,
                    enter = fadeIn(tween(300)) + slideInHorizontally(tween(300)) { -it },
                    exit = fadeOut(tween(300)) + slideOutHorizontally(tween(300)) { -it }
                ) {
                    TasksScreen(
                        viewModel = taskViewModel,
                        onEditTask = {
                            editingTask = it
                            showSheet = true
                        }
                    )
                }

                this@Column.AnimatedVisibility(
                    visible = currentTab == 1,
                    enter = fadeIn(tween(300)) + slideInHorizontally(tween(300)) { it },
                    exit = fadeOut(tween(300)) + slideOutHorizontally(tween(300)) { it }
                ) {
                    HabitsScreen(
                        viewModel = habitViewModel,
                        onEditHabit = {
                            editingHabit = it
                            showSheet = true
                        }
                    )
                }
            }
        }

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { 
                    showSheet = false
                    editingTask = null
                    editingHabit = null
                },
                sheetState = sheetState,
                containerColor = Color(0xFF141419),
                dragHandle = { BottomSheetDefaults.DragHandle(color = Color.White.copy(alpha = 0.2f)) },
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                tonalElevation = 0.dp
            ) {
                if (currentTab == 0) {
                    AddEditTaskSheet(
                        task = editingTask,
                        onDismiss = { showSheet = false },
                        onSave = {
                            if (editingTask == null) taskViewModel.addTask(it)
                            else taskViewModel.updateTask(it)
                            showSheet = false
                        }
                    )
                } else {
                    AddEditHabitSheet(
                        habit = editingHabit,
                        onDismiss = { showSheet = false },
                        onSave = {
                            if (editingHabit == null) habitViewModel.addHabit(it)
                            else habitViewModel.updateHabit(it)
                            showSheet = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AlarmHeaderSection() {
    var quickNote by remember { mutableStateOf("") }
    
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
                    text = "GoalGrid",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        brush = Brush.linearGradient(listOf(CyanNeon, MagentaNeon))
                    )
                )
                Text(
                    text = "Stay Focused, Stay Productive",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
            }
            
            Surface(
                shape = CircleShape,
                color = CardBg,
                modifier = Modifier.size(48.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Alarm, contentDescription = null, tint = CyanNeon)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Alarm Card
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.History,
                    contentDescription = null,
                    tint = MagentaNeon,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    TextField(
                        value = quickNote,
                        onValueChange = { quickNote = it },
                        placeholder = { Text("Quick Alarm Note...", color = TextSecondary.copy(alpha = 0.5f)) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                IconButton(
                    onClick = { /* TODO: Quick alarm logic */ },
                    modifier = Modifier
                        .background(CyanNeon, CircleShape)
                        .size(36.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black)
                }
            }
        }
    }
}

@Composable
fun CustomTabSwitcher(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .background(CardBg, RoundedCornerShape(20.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TabItem(
            text = "Tasks",
            isSelected = selectedTab == 0,
            icon = Icons.Default.CheckCircle,
            modifier = Modifier.weight(1f),
            onClick = { onTabSelected(0) }
        )
        TabItem(
            text = "Habits",
            isSelected = selectedTab == 1,
            icon = Icons.Default.History,
            modifier = Modifier.weight(1f),
            onClick = { onTabSelected(1) }
        )
    }
}

@Composable
fun TabItem(
    text: String,
    isSelected: Boolean,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        if (isSelected) DarkBg else Color.Transparent,
        label = "tabBg"
    )
    val contentColor by animateColorAsState(
        if (isSelected) CyanNeon else TextSecondary,
        label = "tabContent"
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        modifier = modifier.height(44.dp),
        tonalElevation = 0.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = contentColor,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}
