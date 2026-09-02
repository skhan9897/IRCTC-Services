package com.bank.irctc

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bank.irctc.databinding.ActivityRegisterBinding
import com.bank.irctc.models.User
import com.bank.irctc.network.RetrofitClient
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerBtn.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val mobile = binding.mobile.text.toString()
            val password = binding.password.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && mobile.isNotEmpty() && password.isNotEmpty()) {
                registerUser(name, email, mobile, password)
            } else {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginText.setOnClickListener {
            finish()
        }
    }

    private fun registerUser(name: String, email: String, mobile: String, pass: String) {
        lifecycleScope.launch {
            try {
                val user = User(name = name, email = email, mobile = mobile, password = pass)
                val response = RetrofitClient.api.register(user)
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Registration successful. Please login.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
