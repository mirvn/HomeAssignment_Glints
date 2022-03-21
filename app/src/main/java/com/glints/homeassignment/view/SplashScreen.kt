package com.glints.homeassignment.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.glints.homeassignment.R
import com.glints.homeassignment.databinding.SplashscreenActivityBinding
import com.glints.homeassignment.helper.SessionLogin
import com.glints.homeassignment.view.dashboard.DashboardActivity
import com.glints.homeassignment.view.loginRegister.LoginActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var binding: SplashscreenActivityBinding
    private lateinit var sessionLogin: SessionLogin

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashscreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionLogin = SessionLogin(this)
        GlobalScope.launch {
            delay(2000)
            // Check if user is signed in (non-null) and update UI accordingly.
            val token =
                sessionLogin.sharedPreferences.getString(R.string.token.toString(), "").toString()
            if (token.isEmpty() || token == "") {
                val intent = Intent(this@SplashScreen, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@SplashScreen, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}