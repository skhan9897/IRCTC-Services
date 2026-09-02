package com.bank.irctc

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import com.bank.irctc.databinding.ActivityMainBinding
import com.bank.irctc.network.RetrofitClient
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager
    private var stationList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        if (!sessionManager.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setupDrawer()
        setupDatePicker()
        loadStations()

        binding.welcomeText.text = "Welcome, ${sessionManager.getUserName()}"

        binding.menuBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.searchBtn.setOnClickListener {
            val from = binding.fromStation.text.toString()
            val to = binding.toStation.text.toString()
            val date = binding.journeyDate.text.toString()

            if (from.isEmpty() || to.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, SearchActivity::class.java)
                intent.putExtra("FROM", from)
                intent.putExtra("TO", to)
                intent.putExtra("DATE", date)
                startActivity(intent)
            }
        }
    }

    private fun setupDrawer() {
        binding.navigationView.setNavigationItemSelectedListener(this)
        val header = binding.navigationView.getHeaderView(0)
        header.findViewById<TextView>(R.id.navUserName).text = sessionManager.getUserName()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {}
            R.id.nav_bookings -> {
                startActivity(Intent(this, MyBookingsActivity::class.java))
            }
            R.id.nav_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            R.id.nav_logout -> {
                sessionManager.logout()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        binding.journeyDate.setOnClickListener {
            val datePicker = DatePickerDialog(this, { _, y, m, d ->
                val formattedDate = "$y-${m + 1}-$d"
                binding.journeyDate.setText(formattedDate)
            }, year, month, day)
            datePicker.datePicker.minDate = System.currentTimeMillis()
            datePicker.show()
        }
    }

    private fun loadStations() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getStations()
                if (response.isSuccessful) {
                    val stations = response.body() ?: emptyList()
                    stationList = stations.map { "${it.stationName} (${it.stationCode})" }.toMutableList()
                    
                    val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_dropdown_item_1line, stationList)
                    binding.fromStation.setAdapter(adapter)
                    binding.toStation.setAdapter(adapter)
                }
            } catch (e: Exception) {
                // Fallback stations if backend is unreachable
                stationList = mutableListOf("NEW DELHI (NDLS)", "MUMBAI CENTRAL (MMCT)", "BAREILLY (BE)", "LUCKNOW (LKO)")
                val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_dropdown_item_1line, stationList)
                binding.fromStation.setAdapter(adapter)
                binding.toStation.setAdapter(adapter)
            }
        }
    }
}
