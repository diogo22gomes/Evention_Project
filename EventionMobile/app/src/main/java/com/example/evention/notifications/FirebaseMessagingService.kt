package com.example.evention.notifications

import UserPreferences
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.evention.R
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title ?: "Nova notificação"
        val body = remoteMessage.notification?.body ?: "Você recebeu uma nova mensagem."

        showNotification(title, body)
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "default_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Canal padrão",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.nonotification)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)

        val userPreferences = UserPreferences(applicationContext)
        val userId = userPreferences.getUserId()
        val db = FirebaseFirestore.getInstance()

        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val currentDate = formatter.format(Date())

        val data = mapOf(
            "title" to title,
            "message" to message,
            "timestamp" to currentDate
        )

        db.collection("notifications").document(userId!!)
            .update("notifications", FieldValue.arrayUnion(data))
            .addOnFailureListener {
                db.collection("notifications").document(userId)
                    .set(mapOf("notifications" to listOf(data)))
            }
    }
}
