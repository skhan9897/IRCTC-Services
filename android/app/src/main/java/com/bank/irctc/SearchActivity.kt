package com.bank.irctc

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bank.irctc.databinding.ActivitySearchBinding
import com.bank.irctc.network.RetrofitClient
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val from = intent.getStringExtra("FROM") ?: ""
        val to = intent.getStringExtra("TO") ?: ""
        val date = intent.getStringExtra("DATE") ?: ""
        val query = intent.getStringExtra("QUERY") ?: ""
        
        if (query.isNotEmpty()) {
            binding.searchTitle.text = "Results for: $query"
            findTrains(query)
        } else {
            binding.searchTitle.text = "$from → $to"
            loadTrains(from, to, date)
        }
        
        binding.backBtn.setOnClickListener { finish() }
        
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun findTrains(query: String) {
        lifecycleScope.launch {
            binding.loader.visibility = View.VISIBLE
            try {
                val response = RetrofitClient.api.findTrains(query)
                binding.loader.visibility = View.GONE
                if (response.isSuccessful) {
                    val trains = response.body() ?: emptyList()
                    binding.recyclerView.adapter = TrainAdapter(trains) { train ->
                        Toast.makeText(this@SearchActivity, "Search by route to book this train", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                binding.loader.visibility = View.GONE
                Toast.makeText(this@SearchActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadTrains(from: String, to: String, date: String) {
        lifecycleScope.launch {
            binding.loader.visibility = View.VISIBLE
            try {
                // Extract station name from "Station Name (CODE)" or use full name
                val fromStation = if (from.contains(" (")) from.substringBefore(" (") else from
                val toStation = if (to.contains(" (")) to.substringBefore(" (") else to
                
                val response = RetrofitClient.api.searchSchedules(fromStation, toStation, date)
                binding.loader.visibility = View.GONE
                
                if (response.isSuccessful) {
                    val schedules = response.body() ?: emptyList()
                    if (schedules.isEmpty()) {
                        Toast.makeText(this@SearchActivity, "No trains found for $date", Toast.LENGTH_SHORT).show()
                    }
                    binding.recyclerView.adapter = TrainAdapter(schedules) { schedule ->
                        val intent = Intent(this@SearchActivity, BookingActivity::class.java)
                        intent.putExtra("TRAIN_ID", schedule.train.id)
                        intent.putExtra("TRAIN_NAME", schedule.train.trainName)
                        intent.putExtra("TRAIN_NUM", schedule.train.trainNumber)
                        intent.putExtra("FROM", schedule.train.source)
                        intent.putExtra("TO", schedule.train.destination)
                        intent.putExtra("DATE", schedule.journeyDate)
                        intent.putExtra("FARE", schedule.train.sleeperFare ?: 500.0)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this@SearchActivity, "Failed to load trains", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                binding.loader.visibility = View.GONE
                Toast.makeText(this@SearchActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
