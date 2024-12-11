package com.dev.ambatoplant.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dev.ambatoplant.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set default fragment if savedInstanceState is null
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AnalysisFragment()) // Set default fragment to AnalysisFragment
                .commit()
        }

        // Bottom Navigation setup
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->  // Changed to setOnItemSelectedListener
            when (item.itemId) {
                R.id.nav_analisis -> {
                    replaceFragment(AnalysisFragment()) // Replace with AnalysisFragment
                    true
                }
                R.id.nav_history -> {
                    replaceFragment(HistoryFragment()) // Replace with HistoryFragment
                    true
                }
                else -> false
            }
        }
    }

    // Function to replace the fragment
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment) // Replace current fragment in the container
            .addToBackStack(null) // Optional: Add to back stack to enable back navigation
            .commit()
    }
}
