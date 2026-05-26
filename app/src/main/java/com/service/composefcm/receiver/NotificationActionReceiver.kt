package com.service.composefcm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        if (action == "ACTION_TERIMA") {
            Log.d("FCM_ACTION", "Tombol Terima Diklik")
            Toast.makeText(context, "Anda mengklik Terima!", Toast.LENGTH_SHORT).show()
            // Lakukan aksi spesifik di sini, misalnya membuka layar tertentu
        }
    }
}