package com.glints.homeassignment.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.glints.homeassignment.BuildConfig
import com.glints.homeassignment.model.Balance
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DashboardViewmodel:ViewModel() {
    private val TAG = DashboardViewmodel::class.java.simpleName
    private val balanceLiveData= MutableLiveData<Balance>()
    private lateinit var errorMsg: String

    fun setBalance(
        token: String
    ) {
        val url = "${BuildConfig.URL_API}/balance"

        val client = AsyncHttpClient()
        client.setURLEncodingEnabled(true)
        client.addHeader("Authorization",token)

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
                val error = jsonObject.optJSONObject("error")
                balance.message = error.optString("message")

                Log.d(TAG, "onFailure: ${error.toString()}")
                balanceLiveData.postValue(balance)
            }
        })
    }

    fun getUserBalance(): LiveData<Balance> = balanceLiveData
}