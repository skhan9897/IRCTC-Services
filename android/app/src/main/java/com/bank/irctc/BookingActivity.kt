package com.bank.irctc

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bank.irctc.databinding.ActivityBookingBinding
import com.bank.irctc.models.Train
import com.bank.irctc.network.RetrofitClient
import kotlinx.coroutines.launch

class BookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trainName = intent.getStringExtra("TRAIN_NAME")
        val trainNumber = intent.getStringExtra("TRAIN_NUM")
        val from = intent.getStringExtra("FROM")
        val to = intent.getStringExtra("TO")
        val date = intent.getStringExtra("DATE")
        val trainId = intent.getLongExtra("TRAIN_ID", -1)

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
                // Here you would call booking API
                Toast.makeText(this, "Booking request sent!", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
