package com.example.goalgrid.ui.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.goalgrid.data.Habit
import com.example.goalgrid.databinding.LayoutAddEditHabitBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddEditHabitSheet(
    private val habit: Habit? = null,
    private val onSave: (Habit) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: LayoutAddEditHabitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        index: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutAddEditHabitBinding.inflate(index, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set initial values if editing
        habit?.let {
            binding.titleText.text = "Edit Habit"
            binding.habitNameInput.setText(it.name)
            binding.targetSlider.value = it.dailyTarget.toFloat()
            binding.targetText.text = "Daily Target: ${it.dailyTarget}"
            binding.prioritySlider.value = it.priority.toFloat()
            binding.priorityText.text = "Priority: ${it.priority}"
        }

        // Target Slider Listener
        binding.targetSlider.addOnChangeListener { _, value, _ ->
            binding.targetText.text = "Daily Target: ${value.toInt()}"
        }

        // Priority Slider Listener
        binding.prioritySlider.addOnChangeListener { _, value, _ ->
            binding.priorityText.text = "Priority: ${value.toInt()}"
        }

        // Save Button Listener
        binding.saveButton.setOnClickListener {
            val name = binding.habitNameInput.text.toString()
            if (name.isBlank()) {
                binding.habitNameInput.error = "Name cannot be empty"
                return@setOnClickListener
            }

            val newHabit = habit?.copy(
                name = name,
                dailyTarget = binding.targetSlider.value.toInt(),
                priority = binding.prioritySlider.value.toInt()
            ) ?: Habit(
                name = name,
                dailyTarget = binding.targetSlider.value.toInt(),
                priority = binding.prioritySlider.value.toInt()
            )

            onSave(newHabit)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
