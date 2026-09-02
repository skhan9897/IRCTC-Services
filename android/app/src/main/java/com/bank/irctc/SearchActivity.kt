package com.bank.irctc

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
        
        binding.searchTitle.text = "Trains: $from to $to"
        
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        
        loadTrains(from, to)
    }

    private fun loadTrains(from: String, to: String) {
        lifecycleScope.launch {
            try {
                // Extract station name from "Station Name (CODE)"
                val fromCode = from.substringAfter("(").substringBefore(")")
                val toCode = to.substringAfter("(").substringBefore(")")
                
                val response = RetrofitClient.api.searchTrains(fromCode, toCode)
                if (response.isSuccessful) {
                    val trains = response.body() ?: emptyList()
                    if (trains.isEmpty()) {
                        Toast.makeText(this@SearchActivity, "No trains found", Toast.LENGTH_SHORT).show()
                    }
                    binding.recyclerView.adapter = TrainAdapter(trains) { train ->
                        val intent = Intent(this@SearchActivity, BookingActivity::class.java)
                        intent.putExtra("TRAIN_ID", train.id)
                        intent.putExtra("TRAIN_NAME", train.trainName)
                        intent.putExtra("TRAIN_NUM", train.trainNumber)
                        intent.putExtra("FROM", train.source)
                        intent.putExtra("TO", train.destination)
                        intent.putExtra("DATE", date)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this@SearchActivity, "Failed to load trains", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SearchActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
