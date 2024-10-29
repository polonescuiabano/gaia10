package com.example.gaia

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        username = intent.getStringExtra("USERNAME") ?: ""
        Log.d("MainActivity", "Username extraído: '$username'")

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_sessions -> {
                    val sessionsFragment = SessionsFragment().apply {
                        arguments = Bundle().apply {
                            putString("USERNAME", username)
                        }
                    }
                    replaceFragment(SessionsFragment())
                    true
                }
                R.id.nav_settings -> {
                    // Lógica para abrir o fragmento de configurações
                    true
                }
                else -> false
            }
        }

        // Carregar o fragmento inicial
        if (savedInstanceState == null) {
            replaceFragment(SessionsFragment().apply {
                arguments = Bundle().apply {
                    putString("USERNAME", username)
                }
            })
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}
