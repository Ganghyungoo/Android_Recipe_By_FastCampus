package com.test.blindappproject.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.util.Date

@BindingAdapter("date")
fun TextView.setDate(date: Date?) {
    text = DateUtil.convertPrintDateString(date)
}
