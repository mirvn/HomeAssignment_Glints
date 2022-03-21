package com.glints.homeassignment.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var status: String = "",
    var error: String = "",
    var token: String = "",
    var username: String = "",
    var accountNo: String = ""
) : Parcelable