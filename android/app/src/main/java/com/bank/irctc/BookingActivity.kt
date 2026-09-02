package com.bank.irctc

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bank.irctc.databinding.ActivityBookingBinding
import com.bank.irctc.models.Booking
import com.bank.irctc.models.Passenger
import com.bank.irctc.network.RetrofitClient
import kotlinx.coroutines.launch

class BookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        val trainName = intent.getStringExtra("TRAIN_NAME")
        val trainNumber = intent.getStringExtra("TRAIN_NUM")
        val from = intent.getStringExtra("FROM") ?: ""
        val to = intent.getStringExtra("TO") ?: ""
        val date = intent.getStringExtra("DATE") ?: ""
        val trainId = intent.getLongExtra("TRAIN_ID", -1)
        val fare = intent.getDoubleExtra("FARE", 500.0)

        binding.trainName.text = "$trainName ($trainNumber)"
        binding.journeyDetails.text = "$from -> $to | $date"

        val genders = arrayOf("Male", "Female", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, genders)
        binding.pGender.setAdapter(adapter)

        binding.bookBtn.setOnClickListener {
            val name = binding.pName.text.toString()
            val age = binding.pAge.text.toString()
            val gender = binding.pGender.text.toString()

            if (name.isNotEmpty() && age.isNotEmpty() && gender.isNotEmpty()) {
                val passengers = listOf(Passenger(name, age.toInt(), gender))
                val booking = Booking(
                    userId = sessionManager.getUserId(),
                    trainId = trainId,
                    journeyDate = date,
                    fromStation = from,
                    toStation = to,
                    classType = "SL", 
                    totalFare = fare,
                    passengers = passengers
                )
                
                performBooking(booking)
            } else {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performBooking(booking: Booking) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.bookTicket(booking)
                if (response.isSuccessful && response.body() != null) {
                    val savedBooking = response.body()!!
                    val intent = Intent(this@BookingActivity, PaymentActivity::class.java)
                    intent.putExtra("AMOUNT", savedBooking.totalFare)
                    intent.putExtra("BOOKING_ID", savedBooking.id)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@BookingActivity, "Booking failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@BookingActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
