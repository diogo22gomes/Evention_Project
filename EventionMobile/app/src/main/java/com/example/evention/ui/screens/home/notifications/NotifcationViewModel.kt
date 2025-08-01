package com.example.evention.ui.screens.home.notifications

import UserPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class NotificationViewModel(private val userPreferences: UserPreferences) : ViewModel() {
    private val _notifications = mutableStateListOf<NotificationItem>()
    val notifications: List<NotificationItem> = _notifications

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        val userId = userPreferences.getUserId()
        if (userId == null) return

        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection("notifications").document(userId)

        userDocRef.get()
            .addOnSuccessListener { snapshot ->
                _notifications.clear()
                val notificationsList = snapshot.get("notifications") as? List<Map<String, String>> ?: emptyList()
                for (notif in notificationsList) {
                    val title = notif["title"] ?: ""
                    val message = notif["message"] ?: ""
                    val timestamp = notif["timestamp"] ?: ""
                    _notifications.add(NotificationItem(title, message, timestamp))
                }
            }
    }

}
