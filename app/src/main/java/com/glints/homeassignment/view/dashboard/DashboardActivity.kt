package com.glints.homeassignment.view.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.glints.homeassignment.R
import com.glints.homeassignment.databinding.ActivityDashboardBinding
import com.glints.homeassignment.helper.SessionLogin
import com.glints.homeassignment.view.dashboard.adapter.TransactionHistoryAdapter
import com.glints.homeassignment.view.loginRegister.LoginActivity
import com.glints.homeassignment.viewmodel.DashboardViewmodel

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var sessionLogin: SessionLogin
    private lateinit var dashboardViewmodel: DashboardViewmodel
    private lateinit var token: String
    private var rvAdapter = TransactionHistoryAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showProgressBar(true)
        dashboardViewmodel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DashboardViewmodel::class.java) //initialize viewmodel class
        sessionLogin = SessionLogin(this)
        token = sessionLogin.sharedPreferences.getString(R.string.token.toString(), "").toString()
        val username = sessionLogin.sharedPreferences.getString(R.string.username.toString(), "")
        binding.tvWelcome.text = "Hello, ${username.toString()}"
        showUserBalance()
        showTransactionHistory()
    }

    private fun showTransactionHistory() {
        dashboardViewmodel.setHistoryTransactions(
            token,
            this
        )
        dashboardViewmodel.getHistoryTransactions().observe({ lifecycle }, { transactionHistory ->
            if (transactionHistory.isNullOrEmpty()) {
                binding.progressBar8.visibility = View.GONE
                binding.tvDataEmpty.visibility = View.VISIBLE
            } else {
                binding.progressBar8.visibility = View.GONE
                binding.tvDataEmpty.visibility = View.GONE
                rvAdapter.setDataHistory(transactionHistory)
                showRv()
            }
        })
    }

    private fun showRv() {
        val rv = binding.recyclerView
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = rvAdapter
    }

    private fun showUserBalance() {
        dashboardViewmodel.setBalance(token)
        dashboardViewmodel.getUserBalance().observe({ lifecycle }, { balanceData ->
            if (balanceData.status == getString(R.string.failed) &&
                balanceData.message == getString(R.string.jwt_expired)
            ) {
                val alert = AlertDialog.Builder(this)
                alert.apply {
                    setTitle(getString(R.string.session_expired))
                    setMessage(getString(R.string.sessionExpired_message))
                    setIcon(R.drawable.ic_baseline_lock_clock_24)
                    setPositiveButton(R.string.ok) { _, _ ->
                        sessionLogin.editor.clear().apply() //clear data session
                        startActivity(Intent(this@DashboardActivity, LoginActivity::class.java))
                        finish()
                    }
                    setCancelable(false)
                }.create().show()
            } else {
                showProgressBar(false)
                binding.tvBalance.text = "SGD ${balanceData.balance}"
                binding.tvAccNoDashboard.text = balanceData.accountNo
                binding.tvNameHolder.text =
                    sessionLogin.sharedPreferences.getString(R.string.username.toString(), "")
                        .toString()
            }
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