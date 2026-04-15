package com.example.goalgrid.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.goalgrid.data.Habit
import com.example.goalgrid.databinding.ItemHabitBinding

class HabitsAdapter(
    private val onIncrement: (Habit) -> Unit,
    private val onEditClick: (Habit) -> Unit
) : ListAdapter<Habit, HabitsAdapter.HabitViewHolder>(HabitDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val binding = ItemHabitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HabitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class HabitViewHolder(private val binding: ItemHabitBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(habit: Habit) {
            binding.habitName.text = habit.name
            binding.habitStatus.text = "${habit.currentCountToday} / ${habit.dailyTarget} times today"
            
            val progress = if (habit.dailyTarget > 0) (habit.currentCountToday.toFloat() / habit.dailyTarget * 100).toInt() else 0
            binding.habitProgress.progress = progress
            
            binding.incrementHabit.setOnClickListener {
                onIncrement(habit)
            }
            
            binding.editHabit.setOnClickListener {
                onEditClick(habit)
            }
        }
    }

    class HabitDiffCallback : DiffUtil.ItemCallback<Habit>() {
        override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean = oldItem == newItem
    }
}
