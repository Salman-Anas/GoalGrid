package com.example.goalgrid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.goalgrid.databinding.ActivityMainBinding
import com.example.goalgrid.ui.fragments.HabitsFragment
import com.example.goalgrid.ui.fragments.TasksFragment
import com.example.goalgrid.ui.fragments.AddTaskDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTabs()
        setupFab()
        
        // Default fragment
        replaceFragment(TasksFragment())
    }

    private fun setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> replaceFragment(TasksFragment())
                    1 -> replaceFragment(HabitsFragment())
                }
            }
            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
        })
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
            if (currentFragment is TasksFragment) {
                AddTaskDialogFragment().show(supportFragmentManager, "AddTaskDialog")
            } else if (currentFragment is HabitsFragment) {
                currentFragment.showAddHabitDialog()
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contentFrame, fragment)
            .commit()
    }
}
