package com.example.goalgrid.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val dailyTarget: Int,
    val currentCountToday: Int = 0,
    val priority: Int, // 1-100 scale
    val lastUpdatedTimestamp: Long = System.currentTimeMillis()
)
