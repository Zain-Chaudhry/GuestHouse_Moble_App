package com.example.diamondguesthouse.core.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

private const val TAG = "GuestHouse"

fun String.toLog() {
    Log.d(TAG, this)
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}