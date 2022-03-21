package com.glints.homeassignment.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionHistory(
    var transactionId: String = "",
    var amount: Int = 0,
    var transactionDate: String = "",
    var description: String = "",
    var transactionType: String = "",
    var accountNo: String = "",
    var accountHolder: String = "",
) : Parcelable
