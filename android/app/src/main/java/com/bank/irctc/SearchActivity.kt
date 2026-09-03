package com.bank.irctc

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bank.irctc.databinding.ActivitySearchBinding
import com.bank.irctc.models.Train
import com.bank.irctc.models.TrainSchedule
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
        
        binding.backBtn.setOnClickListener { finish() }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        
        if (query.isNotEmpty()) {
            binding.searchTitle.text = "Results for: $query"
            findTrainsByQuery(query)
        } else {
            binding.searchTitle.text = "$from → $to"
            binding.searchSubtitle.text = "Available trains on $date"
            loadTrainsByRoute(from, to, date)
        }
    }

    private fun findTrainsByQuery(query: String) {
        lifecycleScope.launch {
            binding.loader.visibility = View.VISIBLE
            try {
                val response = RetrofitClient.api.findTrains(query)
                binding.loader.visibility = View.GONE
                if (response.isSuccessful) {
                    val trains = response.body() ?: emptyList()
                    binding.recyclerView.adapter = TrainAdapter(trains) { item ->
                        if (item is Train) {
                            Toast.makeText(this@SearchActivity, "Search by route and date to book ${item.trainName}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } catch (e: Exception) {
                binding.loader.visibility = View.GONE
                Toast.makeText(this@SearchActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadTrainsByRoute(from: String, to: String, date: String) {
        lifecycleScope.launch {
            binding.loader.visibility = View.VISIBLE
            try {
                val fromName = if (from.contains(" (")) from.substringBefore(" (") else from
                val toName = if (to.contains(" (")) to.substringBefore(" (") else to
                
                val response = RetrofitClient.api.searchSchedules(fromName, toName, date)
                binding.loader.visibility = View.GONE
                
                if (response.isSuccessful) {
                    val schedules = response.body() ?: emptyList()
                    if (schedules.isEmpty()) {
                        Toast.makeText(this@SearchActivity, "No trains found for $date", Toast.LENGTH_SHORT).show()
                    }
                    binding.recyclerView.adapter = TrainAdapter(schedules) { item ->
                        if (item is TrainSchedule) {
                            val intent = Intent(this@SearchActivity, BookingActivity::class.java)
                            intent.putExtra("TRAIN_ID", item.train.id)
                            intent.putExtra("TRAIN_NAME", item.train.trainName)
                            intent.putExtra("TRAIN_NUM", item.train.trainNumber)
                            intent.putExtra("FROM", item.train.source)
                            intent.putExtra("TO", item.train.destination)
                            intent.putExtra("DATE", item.journeyDate)
                            intent.putExtra("FARE", item.train.sleeperFare ?: 500.0)
                            startActivity(intent)
                        }
                    }
                }
            } catch (e: Exception) {
                binding.loader.visibility = View.GONE
                Toast.makeText(this@SearchActivity, "Network Error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
