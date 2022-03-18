package com.glints.homeassignment.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.glints.homeassignment.BuildConfig
import com.glints.homeassignment.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class LoginViewModel:ViewModel() {
    private val TAG = LoginViewModel::class.java.simpleName
    private val userLiveData= MutableLiveData<User>()
    private lateinit var errorMsg: String

    fun setLogin(
        username: String,
        password: String,
        context: Context
    ) {
        val url = "${BuildConfig.URL_API}/login"
        val params = RequestParams()
        params.put("username", username)
        params.put("password", password)

        val client = AsyncHttpClient()
        client.setURLEncodingEnabled(true)

        client.post(url, params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = responseBody?.let { String(it) }
                val jsonObject = JSONObject(result.toString())
                val user = User()

                user.status = jsonObject.optString("status")
                user.username = jsonObject.optString("username")
                user.accountNo = jsonObject.optString("accountNo")
                user.token = jsonObject.optString("token")

                userLiveData.postValue(user)
                Log.d(TAG, "onSuccess: $user")
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
                val user = User()

                user.status = jsonObject.optString("status")
                user.error = jsonObject.optString("error")
                user.username = jsonObject.optString("username")
                user.accountNo = jsonObject.optString("accountNo")
                user.token = jsonObject.optString("token")

                Log.d(TAG, "onFailure: $jsonObject.optString(\"status\")")
                userLiveData.postValue(user)
            }
        })
    }

    fun getUserLogin(): LiveData<User> = userLiveData
}