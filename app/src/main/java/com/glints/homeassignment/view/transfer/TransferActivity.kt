package com.glints.homeassignment.view.transfer

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.glints.homeassignment.R
import com.glints.homeassignment.databinding.ActivityTransferBinding
import com.glints.homeassignment.helper.SessionLogin
import com.glints.homeassignment.model.Payees
import com.glints.homeassignment.viewmodel.TransferViewModel

class TransferActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransferBinding
    private lateinit var transferViewModel: TransferViewModel
    private lateinit var sessionLogin: SessionLogin
    private lateinit var token: String

    //
    private lateinit var payees: ArrayList<String>
    private lateinit var adapterPayees: ArrayAdapter<String>
    private lateinit var dataPayees: ArrayList<Payees>
    private lateinit var dataPayeesSelected: List<Payees>
    private var accountNoSelected: String = ""
    private lateinit var payeesNameSelected: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionLogin = SessionLogin(this)
        transferViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(TransferViewModel::class.java) //initialize viewmodel class
        token = sessionLogin.sharedPreferences.getString(R.string.token.toString(), "").toString()
        loadListPayees()
        binding.btnTransferNow.setOnClickListener {
            beginTransfer()
        }
        binding.toolbar4.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun beginTransfer() {
        when {
            binding.actPayee.text.isNullOrEmpty() -> {
                binding.actPayee.error = getString(R.string.payee_required)
            }
            binding.edtAmount.text.isNullOrEmpty() -> {
                binding.edtAmount.error = getString(R.string.amount_required)
            }
            binding.edtDescription.text.isNullOrEmpty() -> {
                binding.edtDescription.error = getString(R.string.description_required)
            }
            else -> {
                val alertConfirmation = AlertDialog.Builder(this)
                alertConfirmation.apply {
                    setMessage(R.string.confirmation_data_message)
                    setTitle(getString(R.string.transfer_confirmation))
                    setCancelable(false)
                    setPositiveButton(R.string.transfer) { _, _ ->
                        startTransfer()
                    }
                    setNegativeButton(R.string.cancel) { _, _ -> }
                }.create().show()
            }
        }
    }

    private fun startTransfer() {
        val alert = AlertDialog.Builder(this)
        val alertLoading = alert.apply {
            setView(R.layout.layout_make_transfer)
            setCancelable(false)
        }.create()
        val amount = binding.edtAmount.text.toString()
        Log.d("TAG", "startTransfer-amount:$amount ")
        alertLoading.show()
        transferViewModel.setTransfer(
            accountNoSelected,
            amount.toInt(),
            binding.edtDescription.text.toString(),
            token,
            this
        )
        transferViewModel.getTransferData().observe({ lifecycle }, { dataTransfer ->
            alertLoading.cancel()
            if (dataTransfer.error.isNotEmpty() && dataTransfer.status == getString(R.string.failed)) {
                val alertFailed = AlertDialog.Builder(this)
                alertFailed.apply {
                    setIcon(R.drawable.ic_baseline_cancel_24)
                    setTitle(getString(R.string.transfer_failed))
                    setMessage(dataTransfer.error)
                    setPositiveButton(getString(R.string.ok)) { _, _ -> }
                    setCancelable(false)
                }.create().show()
            } else {
                val alertSuccess = AlertDialog.Builder(this)
                val v: View = View.inflate(this, R.layout.layout_transfer_success, null)
                val tId = v.findViewById<TextView>(R.id.tv_transactionId)
                val tAmount = v.findViewById<TextView>(R.id.tv_transactionAmount)
                val tDesc = v.findViewById<TextView>(R.id.tv_transactionDesc)
                val tReceipent = v.findViewById<TextView>(R.id.tv_transactionReceipent)
                val tStatus = v.findViewById<TextView>(R.id.tv_transactionStatus)
                val btnOk = v.findViewById<Button>(R.id.btn_ok)
                tId.text = dataTransfer.transactionId
                tAmount.text = dataTransfer.amount.toString()
                tDesc.text = dataTransfer.description
                tReceipent.text = dataTransfer.recipientAccount
                tStatus.text = dataTransfer.status
                val alertCreated = alertSuccess.apply {
                    setView(v)
                    setCancelable(false)
                }.create()
                alertCreated.show()
                btnOk.setOnClickListener {
                    alertCreated.cancel()
                    binding.actPayee.text.clear()
                    binding.edtDescription.text?.clear()
                    binding.edtAmount.text?.clear()
                }

            }
        })
    }

    private fun loadListPayees() {
        transferViewModel.setDataPayees(token)
        transferViewModel.getDataPayees().observe({ lifecycle }, { listPayees ->
            dataPayees = listPayees
            payees = ArrayList()
            for (i in 0 until listPayees.size) {
                payees.add(listPayees[i].name)
            }
            adapterPayees =
                ArrayAdapter(
                    this,
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    payees
                )
            binding.actPayee.setAdapter(adapterPayees)
            binding.actPayee.threshold =
                1 //how many char requires to load sugestion
        })
        getPayeesNo()
    }

    private fun getPayeesNo() {
        binding.actPayee.setOnItemClickListener { parent, _, position, _ ->
            payeesNameSelected = parent.getItemAtPosition(position).toString()
            dataPayeesSelected =
                dataPayees.filter { it.name == payeesNameSelected }
            accountNoSelected = dataPayeesSelected[0].accountNo
            Log.d("TAG", "getPayeesNo: name= $payeesNameSelected - $accountNoSelected ")
        }
    }
}