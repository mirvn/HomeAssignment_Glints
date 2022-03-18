package com.glints.homeassignment.view.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.glints.homeassignment.R
import com.glints.homeassignment.databinding.ActivityDashboardBinding
import com.glints.homeassignment.helper.SessionLogin

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var sessionLogin: SessionLogin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionLogin = SessionLogin(this)
        showProgressBar(true)
        binding.tvWelcome.text = "Hello, ${sessionLogin.sharedPreferences.getString(R.string.username.toString(),"")}"

    }
    private fun showProgressBar(show:Boolean){
        if (show){
            binding.progressBar3.visibility = View.VISIBLE
            binding.progressBar4.visibility = View.VISIBLE
            binding.progressBar5.visibility = View.VISIBLE
        }else{
            binding.progressBar3.visibility = View.GONE
            binding.progressBar4.visibility = View.GONE
            binding.progressBar5.visibility = View.GONE
        }
    }
}