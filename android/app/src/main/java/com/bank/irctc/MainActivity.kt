package com.bank.irctc

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bank.irctc.databinding.ActivityMainBinding
import com.bank.irctc.network.RetrofitClient
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var stationList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDatePicker()
        loadStations()

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
