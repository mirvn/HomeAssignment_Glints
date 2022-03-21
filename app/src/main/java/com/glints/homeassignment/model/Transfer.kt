package com.glints.homeassignment.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transfer(
    var status: String = "",
    var error: String = "",
    var transactionId: String = "",
    var amount: Int = 0,
    var description: String = "",
    var recipientAccount: String = ""
) : Parcelable
