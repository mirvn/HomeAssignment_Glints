package com.glints.homeassignment.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Balance(
    var status: String = "",
    var message: String = "",
    var accountNo: String = "",
    var balance: String = "",
) : Parcelable