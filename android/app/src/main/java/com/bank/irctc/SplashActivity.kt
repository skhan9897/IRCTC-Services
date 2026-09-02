package com.bank.irctc

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.logoSpin)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // 1. Logo Spinning Animation (3 seconds)
        val rotate = ObjectAnimator.ofFloat(logo, "rotationY", 0f, 360f)
        rotate.duration = 1000
        rotate.repeatCount = 2 // Total 3 cycles
        rotate.interpolator = LinearInterpolator()
        rotate.start()

        // 2. ProgressBar Animation
        val progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, 100)
        progressAnimator.duration = 3000
        progressAnimator.start()

        // 3. Navigation to Login after 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}
