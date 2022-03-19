package com.glints.homeassignment.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Payees(
    var id: String = "",
    var name: String = "",
    var accountNo: String = ""
) : Parcelable {}
