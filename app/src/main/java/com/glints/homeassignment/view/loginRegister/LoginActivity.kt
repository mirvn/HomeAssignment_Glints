package com.glints.homeassignment.view.loginRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.glints.homeassignment.R
import com.glints.homeassignment.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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