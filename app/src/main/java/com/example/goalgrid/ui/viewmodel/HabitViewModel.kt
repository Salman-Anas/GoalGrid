package com.example.goalgrid.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goalgrid.data.GoalGridDao
import com.example.goalgrid.data.Habit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val dao: GoalGridDao
) : ViewModel() {

    val habits = dao.getAllHabits().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addHabit(habit: Habit) {
        viewModelScope.launch {
            dao.insertHabit(habit)
        }
    }

    fun updateHabit(habit: Habit) {
        viewModelScope.launch {
            dao.updateHabit(habit)
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            dao.deleteHabit(habit)
        }
    }

    fun incrementHabit(habit: Habit) {
        if (habit.currentCountToday < habit.dailyTarget) {
            viewModelScope.launch {
                dao.updateHabit(habit.copy(
                    currentCountToday = habit.currentCountToday + 1,
                    lastUpdatedTimestamp = System.currentTimeMillis()
                ))
            }
        }
    }
}
