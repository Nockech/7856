package com.example.taskhub.extensions

import android.app.Activity
import android.widget.Toast

//Activity fast toast Extension
fun Activity.fastToast(text: String, duration: Int = 0){
    Toast.makeText(this, text, duration).show()
}