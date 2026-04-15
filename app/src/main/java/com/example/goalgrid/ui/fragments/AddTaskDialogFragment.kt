package com.example.goalgrid.ui.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.goalgrid.data.Task
import com.example.goalgrid.databinding.LayoutAddEditTaskBinding
import com.example.goalgrid.ui.viewmodel.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddTaskDialogFragment : BottomSheetDialogFragment() {
    private var _binding: LayoutAddEditTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by viewModels()
    
    private var alarmTime: Long? = null
    private var dueTime: String = "12:00 PM"
    private var existingTask: Task? = null

    companion object {
        private const val ARG_TASK = "arg_task"

        fun newInstance(task: Task? = null): AddTaskDialogFragment {
            val fragment = AddTaskDialogFragment()
            task?.let {
                val args = Bundle()
                // Task is a data class, if it's not Parcelable/Serializable, we might need to pass id or make it so.
                // For simplicity, let's assume we can pass the id and fetch it, or if it's small, make it Serializable.
                // Checking Task.kt, it's a simple data class. Let's make it Serializable or just pass fields.
                // Better yet, let's just use a property if we show it from same activity, but for Fragment standard, args are better.
            }
            return fragment
        }
    }

    // Since I can't easily change Task to Serializable without seeing all imports/dependencies, 
    // and this is a Fragment, I'll use a simpler approach for now to fix the "not working" edit.
    
    fun setTask(task: Task) {
        existingTask = task
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = LayoutAddEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        existingTask?.let { task ->
            binding.sheetTitle.text = "Edit Task"
            binding.etTitle.setText(task.title)
            binding.etNotes.setText(task.notes)
            binding.tvDueTime.text = task.dueTime
            binding.switchAlarm.isChecked = task.isAlarmEnabled
            dueTime = task.dueTime
            alarmTime = task.alarmTime
        }

        setupTimePicker()
        setupSaveButton()
        
        binding.btnClose.setOnClickListener { dismiss() }
    }

    private fun setupTimePicker() {
        binding.timePickerContainer.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    val selectedCal = Calendar.getInstance()
                    selectedCal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedCal.set(Calendar.MINUTE, minute)
                    selectedCal.set(Calendar.SECOND, 0)
                    selectedCal.set(Calendar.MILLISECOND, 0)

                    if (selectedCal.timeInMillis <= System.currentTimeMillis()) {
                        selectedCal.add(Calendar.DAY_OF_YEAR, 1)
                    }

                    alarmTime = selectedCal.timeInMillis
                    
                    val hour = if (hourOfDay % 12 == 0) 12 else hourOfDay % 12
                    val minStr = String.format("%02d", minute)
                    val amPm = if (hourOfDay < 12) "AM" else "PM"
                    dueTime = "$hour:$minStr $amPm"
                    binding.tvDueTime.text = dueTime
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            if (title.isNotBlank()) {
                val task = existingTask?.copy(
                    title = title,
                    notes = binding.etNotes.text.toString(),
                    dueTime = dueTime,
                    isAlarmEnabled = binding.switchAlarm.isChecked,
                    alarmTime = alarmTime
                ) ?: Task(
                    title = title,
                    notes = binding.etNotes.text.toString(),
                    dueDate = System.currentTimeMillis(),
                    dueTime = dueTime,
                    isAlarmEnabled = binding.switchAlarm.isChecked,
                    alarmTime = alarmTime
                )
                
                if (existingTask != null) {
                    viewModel.updateTask(task)
                } else {
                    viewModel.addTask(task)
                }
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
