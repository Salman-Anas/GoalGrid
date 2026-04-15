package com.example.goalgrid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goalgrid.databinding.FragmentHabitsBinding
import com.example.goalgrid.ui.adapters.HabitsAdapter
import com.example.goalgrid.ui.components.AddEditHabitSheet
import com.example.goalgrid.ui.viewmodel.HabitViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HabitsFragment : Fragment() {
    private var _binding: FragmentHabitsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HabitViewModel by viewModels()
    private lateinit var adapter: HabitsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHabitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        adapter = HabitsAdapter(
            onIncrement = { habit -> viewModel.incrementHabit(habit) },
            onEditClick = { habit -> 
                val sheet = AddEditHabitSheet(habit = habit) { updatedHabit ->
                    viewModel.updateHabit(updatedHabit)
                }
                sheet.show(parentFragmentManager, "AddEditHabitSheet")
            }
        )
        
        binding.habitsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.habitsRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.habits.collect { habits ->
                adapter.submitList(habits)
            }
        }
    }

    fun showAddHabitDialog() {
        val sheet = AddEditHabitSheet { newHabit ->
            viewModel.addHabit(newHabit)
        }
        sheet.show(parentFragmentManager, "AddEditHabitSheet")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
