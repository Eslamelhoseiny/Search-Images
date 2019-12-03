package com.example.searchforimages.utils.extension

import android.content.Context
import android.widget.Toast
import androidx.annotation.NonNull

fun Context.showToast(@NonNull message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}