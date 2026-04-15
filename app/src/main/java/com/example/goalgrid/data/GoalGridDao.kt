package com.example.goalgrid.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalGridDao {
    // Tasks
    @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    // Habits
    @Query("SELECT * FROM habits ORDER BY priority DESC")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits ORDER BY priority DESC LIMIT 3")
    fun getTopHabits(): Flow<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)
}
