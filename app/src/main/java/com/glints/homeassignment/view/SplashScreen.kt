package com.glints.homeassignment.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.glints.homeassignment.databinding.SplashscreenActivityBinding
import com.glints.homeassignment.view.loginRegister.LoginActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    private lateinit var binding : SplashscreenActivityBinding
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashscreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch {
            delay(2000)
            // Check if user is signed in (non-null) and update UI accordingly.
            val intent = Intent(this@SplashScreen, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}