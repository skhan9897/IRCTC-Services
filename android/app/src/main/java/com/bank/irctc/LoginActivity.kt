package com.bank.irctc

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bank.irctc.databinding.ActivityLoginBinding
import com.bank.irctc.models.LoginRequest
import com.bank.irctc.network.RetrofitClient
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                loginUser(email, pass)
            } else {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
        
        binding.registerText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUser(email: String, pass: String) {
        val sessionManager = SessionManager(this)
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.login(LoginRequest(email, pass))
                if (response.isSuccessful && response.body() != null) {
                    val loginRes = response.body()!!
                    sessionManager.saveUser(loginRes.id, loginRes.name, loginRes.email)
                    Toast.makeText(this@LoginActivity, "Login successful: ${loginRes.message}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    // Try to get error message from server
                    val errorBody = response.errorBody()?.string()
                    val msg = if (errorBody != null && errorBody.contains("message")) {
                        // Very simple way to extract message from JSON if server sends one
                        errorBody.substringAfter("\"message\":\"").substringBefore("\"")
                    } else {
                        "Invalid Email or Password"
                    }
                    Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Server not reachable. Check if Backend is running at 10.0.2.2:8080", Toast.LENGTH_LONG).show()
                android.util.Log.e("LOGIN_ERROR", e.message ?: "Unknown Error")
            }
        }
    }
}
