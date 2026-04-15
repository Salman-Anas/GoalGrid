package com.example.goalgrid.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class, Habit::class], version = 1, exportSchema = false)
abstract class GoalGridDatabase : RoomDatabase() {
    abstract fun dao(): GoalGridDao
}
