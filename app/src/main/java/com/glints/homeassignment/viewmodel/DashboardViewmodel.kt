package com.glints.homeassignment.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.glints.homeassignment.BuildConfig
import com.glints.homeassignment.model.Balance
import com.glints.homeassignment.model.TransactionHistory
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DashboardViewmodel : ViewModel() {
    private val TAG = DashboardViewmodel::class.java.simpleName
    private val balanceLiveData = MutableLiveData<Balance>()
    private val transactionHistoryLiveData = MutableLiveData<ArrayList<TransactionHistory>>()
    private lateinit var errorMsg: String

    fun setBalance(
        token: String
    ) {
        val url = "${BuildConfig.URL_API}/balance"

        val client = AsyncHttpClient()
        client.setURLEncodingEnabled(true)
        client.addHeader("Authorization", token)

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = responseBody?.let { String(it) }
                val jsonObject = JSONObject(result.toString())
                val balance = Balance()

                balance.status = jsonObject.optString("status")
                balance.balance = jsonObject.optString("balance")
                balance.accountNo = jsonObject.optString("accountNo")

                balanceLiveData.postValue(balance)
                Log.d(TAG, "onSuccess: $balance")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                errorMsg = when (statusCode) {
                    401 -> "$statusCode : Unauthorized"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }

                val result = responseBody?.let { String(it) }
                val jsonObject = JSONObject(result.toString())
                val balance = Balance()

                balance.status = jsonObject.optString("status")
                val errorMessage = jsonObject.optJSONObject("error")
                balance.message = errorMessage.optString("message")

                balanceLiveData.postValue(balance)
            }
        })
    }

    fun getUserBalance(): LiveData<Balance> = balanceLiveData

    fun setHistoryTransactions(
        token: String,
        context: Context
    ) {
        val url = "${BuildConfig.URL_API}/transactions"
        val transactionHistoryObject = ArrayList<TransactionHistory>()

        val client = AsyncHttpClient()
        client.setURLEncodingEnabled(true)
        client.addHeader("Authorization", token)

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = responseBody?.let { String(it) }
                val jsonObject = JSONObject(result.toString())
                val data = jsonObject.optJSONArray("data")
                for (i in 0 until data.length()) {
                    val historyData = data.optJSONObject(i)
                    val transactionHistory = TransactionHistory()

                    transactionHistory.transactionId = historyData.optString("transactionId")
                    transactionHistory.amount = historyData.optInt("amount")
                    transactionHistory.transactionDate = historyData.optString("transactionDate")
                    transactionHistory.description = historyData.optString("description")
                    transactionHistory.transactionType = historyData.optString("transactionType")

                    when {
                        historyData.has("receipient") -> {
                            val receipient = historyData.optJSONObject("receipient")
                            transactionHistory.accountNo = receipient.optString("accountNo")
                            transactionHistory.accountHolder = receipient.optString("accountHolder")
                        }
                        historyData.has("sender") -> {
                            val sender = historyData.optJSONObject("sender")
                            transactionHistory.accountNo = sender.optString("accountNo")
                            transactionHistory.accountHolder = sender.optString("accountHolder")
                        }
                    }
                    transactionHistoryObject.add(transactionHistory)
                }
                transactionHistoryLiveData.postValue(transactionHistoryObject)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                errorMsg = when (statusCode) {
                    401 -> "$statusCode : Unauthorized"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getHistoryTransactions(): LiveData<ArrayList<TransactionHistory>> =
        transactionHistoryLiveData
}