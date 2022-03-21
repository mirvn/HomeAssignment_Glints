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

    fun setDataPayees(token: String) {
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
                    val payee = Payees()

                    payee.id = jsonObjectData.getString("id")
                    payee.name = jsonObjectData.getString("name")
                    payee.accountNo = jsonObjectData.getString("accountNo")

                    payeesObject.add(payee)
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
                val jsonData = JSONObject(result.toString())
                val transfer = Transfer()

                transfer.status = jsonData.optString("status")
                transfer.amount = jsonData.optInt("amount")
                transfer.description = jsonData.optString("description")
                transfer.transactionId = jsonData.optString("transactionId")
                transfer.recipientAccount = jsonData.optString("recipientAccount")

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
                val jsonObjectData = JSONObject(result.toString())
                val transfer = Transfer()

                transfer.status = jsonObjectData.optString("status")
                transfer.error = jsonObjectData.optString("error")

                Log.d(TAG, "onFailure-error: ${jsonObjectData.optString("error")}")
                transferLiveData.postValue(transfer)
            }
        })
    }

    fun getTransferData(): LiveData<Transfer> = transferLiveData

    fun setDummyPayees(payees: ArrayList<Payees>) {
        payeesLiveData.postValue(payees)
    }

    fun setDummyTransfer(transfer: Transfer) {
        transferLiveData.postValue(transfer)
    }
}