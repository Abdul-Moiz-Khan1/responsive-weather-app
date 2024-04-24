package com.example.temp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.temp.databinding.ActivitySplashscreenBinding

class splashscreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashscreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.umbrellafly.setAnimation(R.raw.umbrella)
        binding.umbrellafly.playAnimation()
        Handler(Looper.getMainLooper()).postDelayed({
            binding.sunspawn.setAnimation(R.raw.sun)
            binding.sunspawn.playAnimation()
        }, 2500)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(
                Intent(this, MainActivity::class.java)

            )
            finish ()
        }, 5000)
//        finish()

    }
}