package com.glints.homeassignment.view.loginRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.glints.homeassignment.R
import com.glints.homeassignment.databinding.ActivityLoginBinding
import com.glints.homeassignment.helper.SessionLogin
import com.glints.homeassignment.view.dashboard.DashboardActivity
import com.glints.homeassignment.viewmodel.LoginViewModel
import com.glints.homeassignment.viewmodel.RegisterViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var sessionLogin: SessionLogin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionLogin = SessionLogin(this)
        loginViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(LoginViewModel::class.java) //initialize viewmodel class
        binding.toolbar.setNavigationOnClickListener {
            exitApp()
        }
        binding.btnLogin.setOnClickListener {
            login()
        }
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }

    private fun login() {
        when{
            binding.edtPw.text.isNullOrEmpty()->{
                binding.edtPw.error = getString(R.string.password_required)
            }
            binding.edtUsername.text.isNullOrEmpty()->{
                binding.edtUsername.error = getString(R.string.username_required)
            }
            else->{
                val alertLoading = AlertDialog.Builder(this)
                val showLoading = alertLoading.apply {
                    setView(R.layout.layout_loading)
                    setCancelable(false)
                }.create()
                showLoading.show()
                loginViewModel.setLogin(
                    binding.edtUsername.text.toString(),
                    binding.edtPw.text.toString(),
                    this
                )
                loginViewModel.getUserLogin().observe({lifecycle},{userData->
                    showLoading.cancel()
                    sessionLogin.setUsername(userData.username)
                    sessionLogin.setAccountNo(userData.accountNo)
                    sessionLogin.setToken(userData.token)
                    startActivity(Intent(this,DashboardActivity::class.java))
                    finish()
                    Toast.makeText(this,"Welcome ${userData.username}", Toast.LENGTH_SHORT).show()
                })
            }
        }
    }

    override fun onBackPressed() {
        exitApp()
    }

    private fun exitApp() {
        val dialogExit = AlertDialog.Builder(this)
        val alert = dialogExit.apply {
            setMessage(getString(R.string.exit_message))
            setTitle(getString(R.string.exit_tittle))
            setPositiveButton(getString(R.string.exit)){ _, _->
                finish()
            }
            setNegativeButton(getString(R.string.cancel)){ _, _->
            }
            setCancelable(false)
        }
        alert.create().show()
    }
}