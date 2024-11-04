package com.example.scotiapracticaltest.ui.common

import android.content.Context
import android.widget.Toast

class RealToastNotifier(private val context: Context) : ToastNotifier {
    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

class NoOpToastNotifier : ToastNotifier {
    override fun showToast(message: String) {
        // Do nothing
    }
}

interface ToastNotifier {
    fun showToast(message: String)
}