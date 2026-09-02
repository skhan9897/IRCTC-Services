package com.bank.irctc

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bank.irctc.databinding.ActivityPaymentBinding

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val amount = intent.getDoubleExtra("AMOUNT", 0.0)
        val bookingId = intent.getLongExtra("BOOKING_ID", -1)

        binding.amountText.text = "Amount to Pay: ₹$amount"

        binding.payBtn.setOnClickListener {
            val card = binding.cardNumber.text.toString()
            val expiry = binding.expiry.text.toString()
            val cvv = binding.cvv.text.toString()

            if (card.length == 16 && expiry.isNotEmpty() && cvv.length == 3) {
                // Mock Payment Successful
                Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show()
                
                val intent = Intent(this, TicketActivity::class.java)
                intent.putExtra("BOOKING_ID", bookingId)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid Card Details", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
