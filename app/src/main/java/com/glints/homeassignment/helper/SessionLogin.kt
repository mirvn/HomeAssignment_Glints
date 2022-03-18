package com.glints.homeassignment.helper

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.glints.homeassignment.R

class SessionLogin(context: Context) {
    var masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    var sharedPreferences = EncryptedSharedPreferences.create(
        context,
        R.string.session_encrypted.toString(),
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    var editor = sharedPreferences.edit()

    fun setUsername(username: String) {
        editor.putString(R.string.username.toString(), username)
        editor.apply()
    }

    fun setAccountNo(accountNo: String) {
        editor.putString(R.string.accountNo.toString(), accountNo)
        editor.apply()
    }

    fun setToken(token:String) {
        editor.putString(R.string.token.toString(),token)
        editor.apply()
    }
}