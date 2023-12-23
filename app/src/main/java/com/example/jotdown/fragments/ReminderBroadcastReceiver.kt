package com.example.jotdown.fragments

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.app.NotificationChannel
import android.app.NotificationManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.jotdown.R

class ReminderBroadcastReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("ReminderBroadcast", "Reminder received!")


        // Retrieve necessary data from the intent if passed while scheduling the reminder
        if(ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        )==PackageManager.PERMISSION_GRANTED){
            val noteText = intent.getStringExtra("note_text")

            // Create and display a notification when the reminder triggers
            val notification = NotificationCompat.Builder(context, "reminder_channel_id")
                .setContentTitle("Reminder")
                .setContentText(noteText ?: noteText) // Use the note text received from intent
                .setSmallIcon(R.drawable.alarm)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build()

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(NOTIFICATION_ID, notification)
        }else {
            // Handle the scenario where the app doesn't have notification permission
            // You might request permission or handle this according to your app's logic
            // For example, show a message to the user indicating the need for permission
            Toast.makeText(context, "Please grant notification permissions", Toast.LENGTH_SHORT).show()
        }


    }

    companion object {
        private const val NOTIFICATION_ID = 123 // Unique ID for the notification
    }
}
