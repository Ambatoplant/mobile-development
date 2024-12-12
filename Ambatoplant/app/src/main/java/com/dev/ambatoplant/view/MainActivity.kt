package com.dev.ambatoplant.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dev.ambatoplant.HomeFragment
import com.dev.ambatoplant.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set fragment default jika savedInstanceState null
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()) // Set fragment default ke AnalysisFragment
                .commit()
        }

        // Pengaturan Bottom Navigation
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_analisis -> {
                    replaceFragment(AnalysisFragment()) // Ganti dengan AnalysisFragment
                    true
                }
                R.id.nav_history -> {
                    replaceFragment(HistoryFragment()) // Ganti dengan HistoryFragment
                    true
                }
                else -> false
            }
        }
    }

    // Fungsi untuk mengganti fragment
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
