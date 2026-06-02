package com.bingoapp.data.remote

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.bingoapp.MainActivity
import com.bingoapp.R
import com.bingoapp.util.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Token will be saved via UserRepository
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        createNotificationChannel()

        val title = message.notification?.title ?: message.data["title"] ?: "Bingo"
        val body = message.notification?.body ?: message.data["body"] ?: ""

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, Constants.FCM_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bingo_ball)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.FCM_CHANNEL_ID,
                Constants.FCM_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Bingo game notifications"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}
