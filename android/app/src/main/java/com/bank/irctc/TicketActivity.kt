package com.bank.irctc

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bank.irctc.databinding.ActivityTicketBinding
import com.bank.irctc.network.RetrofitClient
import kotlinx.coroutines.launch

class TicketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTicketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bookingId = intent.getLongExtra("BOOKING_ID", -1)

        if (bookingId != -1L) {
            loadTicketDetails(bookingId)
        }

        binding.doneBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun loadTicketDetails(id: Long) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getBookingById(id)
                if (response.isSuccessful && response.body() != null) {
                    val booking = response.body()!!
                    binding.pnrText.text = "PNR: ${booking.pnr}"

                    val details = """
                        Train: ${booking.train?.trainName} (${booking.train?.trainNumber})
                        Route: ${booking.fromStation} to ${booking.toStation}
                        Date: ${booking.journeyDate}
                        Class: ${booking.classType}
                        Fare: ₹${booking.totalFare}
                        Status: ${booking.bookingStatus}
                        
                        Passenger: ${booking.passengers.firstOrNull()?.name ?: "N/A"}
                    """.trimIndent()

                    binding.ticketDetails.text = details
                }
            } catch (e: Exception) {
                Toast.makeText(this@TicketActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
