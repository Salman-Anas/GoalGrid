package com.example.goalgrid.ui.components

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.goalgrid.data.Task
import com.example.goalgrid.databinding.LayoutAddEditTaskBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class AddEditTaskSheet(
    private val task: Task? = null,
    private val onSave: (Task) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: LayoutAddEditTaskBinding? = null
    private val binding get() = _binding!!

    private var alarmTime: Long? = task?.alarmTime
    private var dueTime: String = task?.dueTime ?: "12:00 PM"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutAddEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set initial values if editing
        task?.let {
            binding.sheetTitle.text = "Edit Task"
            binding.etTitle.setText(it.title)
            binding.etNotes.setText(it.notes)
            binding.tvDueTime.text = it.dueTime
            binding.switchAlarm.isChecked = it.isAlarmEnabled
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.timePickerContainer.setOnClickListener {
            showTimePicker()
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            if (title.isBlank()) {
                binding.etTitle.error = "Title cannot be empty"
                return@setOnClickListener
            }

            val newTask = task?.copy(
                title = title,
                notes = binding.etNotes.text.toString(),
                isAlarmEnabled = binding.switchAlarm.isChecked,
                dueTime = dueTime,
                alarmTime = alarmTime
            ) ?: Task(
                title = title,
                notes = binding.etNotes.text.toString(),
                dueDate = System.currentTimeMillis(),
                dueTime = dueTime,
                isAlarmEnabled = binding.switchAlarm.isChecked,
                alarmTime = alarmTime
            )

            onSave(newTask)
            dismiss()
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        alarmTime?.let { calendar.timeInMillis = it }

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                val newCalendar = Calendar.getInstance()
                newCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                newCalendar.set(Calendar.MINUTE, minute)
                newCalendar.set(Calendar.SECOND, 0)
                newCalendar.set(Calendar.MILLISECOND, 0)

                if (newCalendar.timeInMillis <= System.currentTimeMillis()) {
                    newCalendar.add(Calendar.DAY_OF_YEAR, 1)
                }

                alarmTime = newCalendar.timeInMillis
                
                val hour = if (hourOfDay % 12 == 0) 12 else hourOfDay % 12
                val minuteStr = String.format(Locale.getDefault(), "%02d", minute)
                val amPm = if (hourOfDay < 12) "AM" else "PM"
                dueTime = "$hour:$minuteStr $amPm"
                binding.tvDueTime.text = dueTime
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        timePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
