package com.glints.homeassignment.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.glints.homeassignment.BuildConfig
import com.glints.homeassignment.model.Payees
import com.glints.homeassignment.model.Transfer
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import org.json.JSONObject

class TransferViewModel : ViewModel() {
    private val TAG = TransferViewModel::class.java.simpleName
    private val payeesLiveData = MutableLiveData<ArrayList<Payees>>()
    private val transferLiveData = MutableLiveData<Transfer>()
    private lateinit var errorMsg: String

    fun setDataPayees(token: String, context: Context) {
        val payeesObject = ArrayList<Payees>()

        val url = "${BuildConfig.URL_API}/payees"
        val client = AsyncHttpClient(true, 80, 443)
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
                val jsonArray = jsonObject.getJSONArray("data")
                for (i in 0 until jsonArray.length()) {
                    val jsonObjectData = jsonArray.getJSONObject(i)
                    val pembimbing = Payees()

                    pembimbing.id = jsonObjectData.getString("id")
                    pembimbing.name = jsonObjectData.getString("name")
                    pembimbing.accountNo = jsonObjectData.getString("accountNo")

                    payeesObject.add(pembimbing)
                }
                payeesLiveData.postValue(payeesObject)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                errorMsg = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
            }

        })
    }

    fun getDataPayees(): LiveData<ArrayList<Payees>> = payeesLiveData

    fun setTransfer(
        receipientAccountNo: String,
        amount: Int,
        description: String,
        token: String,
        context: Context
    ) {
        val url = "${BuildConfig.URL_API}/transfer"
        val jsonObject = JSONObject()
        jsonObject.put("receipientAccountNo", receipientAccountNo)
        jsonObject.put("amount", amount)
        jsonObject.put("description", description)
        val params = StringEntity(jsonObject.toString())
        params.setContentType("application/json")
        /*params.put("receipientAccountNo", receipientAccountNo)
        params.put("description", description)*/

        val client = AsyncHttpClient()
        client.setURLEncodingEnabled(true)
        client.addHeader("Authorization", token)

        client.post(context, url, params, "application/json", object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = responseBody?.let { String(it) }
                val jsonObject = JSONObject(result.toString())
                val transfer = Transfer()

                transfer.status = jsonObject.optString("status")
                transfer.amount = jsonObject.optInt("amount")
                transfer.description = jsonObject.optString("description")
                transfer.transactionId = jsonObject.optString("transactionId")
                transfer.recipientAccount = jsonObject.optString("recipientAccount")

                transferLiveData.postValue(transfer)
                Log.d(TAG, "onSuccess: $transfer")
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
                val transfer = Transfer()

                transfer.status = jsonObject.optString("status")
                transfer.error = jsonObject.optString("error")

                Log.d(TAG, "onFailure-error: ${jsonObject.optString("error")}")
                transferLiveData.postValue(transfer)
            }
        })
    }

    fun getTransferData(): LiveData<Transfer> = transferLiveData
}