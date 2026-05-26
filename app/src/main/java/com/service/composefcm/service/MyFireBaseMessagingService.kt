package com.service.composefcm.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.service.composefcm.R
import com.service.composefcm.receiver.NotificationActionReceiver

class MyFireBaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FCM Notification"
        const val DEFAULT_NOTIFICATION_ID = 0
    }

    override fun onNewToken(token: String) {
        Log.i(TAG, "new FCM token created: $token")
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        initNotificationChannel(notificationManager)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body

        // 1. SIAPKAN ACTION INTENT (Tombol)
        val actionIntent = Intent(this, NotificationActionReceiver::class.java).apply {
            action = "ACTION_TERIMA"
        }

        // Menggunakan FLAG_IMMUTABLE agar aman untuk Android 12 ke atas
        val actionPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            actionIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Pastikan channel dibuat sebelum notify
        initNotificationChannel(notificationManager)

        val notificationBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(applicationContext, "1")
        } else {
            NotificationCompat.Builder(applicationContext)
        }

        notificationBuilder
            .setSmallIcon(R.drawable.ic_android_black_24dp) // Pastikan ikon ini ada
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            // 2. TAMBAHKAN TOMBOL AKSI KE NOTIFIKASI
            // Pastikan R.drawable.ic_check sudah Anda buat di folder drawable
            .addAction(R.drawable.ic_android_black_24dp, "Terima", actionPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(DEFAULT_NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun initNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            notificationManager.createNotificationChannelIfNOtExists(
                channelId = "fcm_high_priority_channel",
                channelName = "Default",
                importance = NotificationManager.IMPORTANCE_HIGH
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NotificationManager.createNotificationChannelIfNOtExists(
    channelId: String,
    channelName: String,
    importance: Int = NotificationManager.IMPORTANCE_DEFAULT
) {
    var channel = this.getNotificationChannel(channelId)

    if (channel == null) {
        channel = NotificationChannel(
            channelId,
            channelName,
            importance
        )
        this.createNotificationChannel(channel)
    }
}





















