package com.bank.irctc

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bank.irctc.databinding.ActivityMyBookingsBinding
import com.bank.irctc.network.RetrofitClient
import kotlinx.coroutines.launch

class MyBookingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyBookingsBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBookingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        binding.bookingsRecyclerView.layoutManager = LinearLayoutManager(this)

        loadBookings()
    }

    private fun loadBookings() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getMyBookings(sessionManager.getUserId())
                if (response.isSuccessful) {
                    val bookings = response.body() ?: emptyList()
                    binding.bookingsRecyclerView.adapter = BookingAdapter(bookings)
                } else {
                    Toast.makeText(this@MyBookingsActivity, "Failed to load bookings", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MyBookingsActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
