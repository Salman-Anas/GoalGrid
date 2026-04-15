package com.example.goalgrid.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val notes: String = "",
    val dueDate: Long, // Timestamp
    val dueTime: String, // String representation for display
    val isAlarmEnabled: Boolean = false,
    val alarmTime: Long? = null,
    val priority: Int = 1, // 1: Low, 2: Medium, 3: High
    val isCompleted: Boolean = false
)
