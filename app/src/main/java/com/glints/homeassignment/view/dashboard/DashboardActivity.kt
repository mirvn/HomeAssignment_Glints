package com.glints.homeassignment.view.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.glints.homeassignment.R
import com.glints.homeassignment.databinding.ActivityDashboardBinding
import com.glints.homeassignment.helper.SessionLogin
import com.glints.homeassignment.viewmodel.DashboardViewmodel

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var sessionLogin: SessionLogin
    private lateinit var dashboardViewmodel: DashboardViewmodel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dashboardViewmodel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DashboardViewmodel::class.java) //initialize viewmodel class
        sessionLogin = SessionLogin(this)
        showProgressBar(true)
        binding.tvWelcome.text = "Hello, ${sessionLogin.sharedPreferences.getString(R.string.username.toString(),"")}"
        showUserBalance()
    }

    private fun showUserBalance() {
        val token = sessionLogin.sharedPreferences.getString(R.string.token.toString(),"").toString()
        Log.d("TAG", "showUserBalance$token: ")
        dashboardViewmodel.setBalance(token)
        dashboardViewmodel.getUserBalance().observe({lifecycle},{balanceData->
            showProgressBar(false)
            binding.tvBalance.text = "SGD "+balanceData.balance
            binding.tvAccNoDashboard.text = balanceData.accountNo
            binding.tvNameHolder.text = sessionLogin.sharedPreferences.getString(R.string.username.toString(),"").toString()
        })
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