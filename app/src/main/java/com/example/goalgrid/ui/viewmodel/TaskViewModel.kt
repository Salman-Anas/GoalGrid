package com.example.goalgrid.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goalgrid.data.GoalGridDao
import com.example.goalgrid.data.Task
import com.example.goalgrid.util.AlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val dao: GoalGridDao,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    val tasks = dao.getAllTasks().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addTask(task: Task) {
        viewModelScope.launch {
            val id = dao.insertTask(task)
            val insertedTask = task.copy(id = id)
            if (insertedTask.isAlarmEnabled) {
                alarmScheduler.schedule(insertedTask)
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            dao.updateTask(task)
            if (task.isAlarmEnabled) {
                alarmScheduler.schedule(task)
            } else {
                alarmScheduler.cancel(task)
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            dao.deleteTask(task)
            alarmScheduler.cancel(task)
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            dao.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }
}
