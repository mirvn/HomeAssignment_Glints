package com.glints.homeassignment.view.loginRegister

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.glints.homeassignment.R
import com.glints.homeassignment.databinding.ActivityRegisterBinding
import com.glints.homeassignment.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var registerViewModel:RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(RegisterViewModel::class.java) //initialize viewmodel class
        binding.toolbar2.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.btnRegister2.setOnClickListener {
            beginRegistration()
        }
    }

    private fun beginRegistration() {
        when{
            binding.edtUsernameRegister.text.isNullOrEmpty()->{
                binding.edtUsernameRegister.error = getString(R.string.username_err_warning)
            }
            binding.edtPwRegister.text.isNullOrEmpty()->{
                binding.edtPwRegister.error = getString(R.string.pw_err_warning)
            }
            binding.edtConfirmPwRegister.text.isNullOrEmpty()->{
                binding.edtConfirmPwRegister.error = getString(R.string.confirmPw_err_warning)
            }
            else->{
                if (binding.edtConfirmPwRegister.text.toString() != binding.edtPwRegister.text.toString()){
                    binding.edtConfirmPwRegister.error = getString(R.string.pw_not_match)
                }else{
                    val alert = AlertDialog.Builder(this)
                    alert.apply {
                        setTitle(getString(R.string.confirmation_register))
                        setMessage(getString(R.string.confirmation_data_message))
                        setCancelable(false)
                        setNegativeButton(getString(R.string.cancel)){_,_->}
                        setPositiveButton(getString(R.string.register)){_,_->
                            // start registration
                            startRegistration()
                        }
                    }.create().show()
                }
            }
        }
    }

    private fun startRegistration() {
        val alertLoading = AlertDialog.Builder(this)
        val alert = alertLoading.apply {
            setView(R.layout.layout_loading)
            setCancelable(false)
        }.create()
        alert.show()
        registerViewModel.setRegister(
            binding.edtUsernameRegister.text.toString(),
            binding.edtConfirmPwRegister.text.toString(),
            this
        )
        registerViewModel.getUserRegistered().observe({lifecycle},{
            alert.cancel()
            when{
                it.status!=getString(R.string.failed)->{
                    val alertSuccess = AlertDialog.Builder(this)
                    alertSuccess.apply {
                        setTitle(getString(R.string.Registration_Success))
                        setMessage(getString(R.string.account_created_message))
                        setIcon(R.drawable.ic_baseline_check_circle_24)
                        setPositiveButton(getString(R.string.login)){_,_->
                            onBackPressed()
                            /*al intent = Intent(this@RegisterActivity,LoginActivity::class.java)
                            startActivity(intent)
                            finish()*/
                        }
                        setCancelable(false)
                    }.create().show()
                }
                it.status==getString(R.string.failed)->{
                    val alertFailed = AlertDialog.Builder(this)
                    alertFailed.apply {
                        setTitle(getString(R.string.Registration_Failed))
                        setMessage(it.error)
                        setIcon(R.drawable.ic_baseline_cancel_24)
                        setPositiveButton(getString(R.string.ok)){ _, _->
                            onBackPressed()
                        /*val intent = Intent(this@RegisterActivity,LoginActivity::class.java)
                            startActivity(intent)
                            finish()*/
                        }
                        setCancelable(false)
                    }.create().show()
                }
            }
        })
    }
}