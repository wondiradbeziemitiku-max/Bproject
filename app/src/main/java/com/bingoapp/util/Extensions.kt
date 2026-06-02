package com.bingoapp.util

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toCoinString(): String {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
    return formatter.format(this)
}

fun Date.toFormattedString(): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return sdf.format(this)
}

fun Long.toDateString(): String {
    return Date(this).toFormattedString()
}

fun String.maskPhoneNumber(): String {
    if (this.length < 8) return this
    val prefix = this.substring(0, 4)
    val suffix = this.substring(this.length - 2)
    val masked = "*".repeat(this.length - 6)
    return "$prefix$masked$suffix"
}

fun String.maskMiddle(count: Int = 4): String {
    if (this.length <= count * 2) return this
    val start = this.take(count)
    val end = this.takeLast(2)
    return "$start${"*".repeat(this.length - count - 2)}$end"
}

@Composable
fun ShowToast(message: String) {
    val context = LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
