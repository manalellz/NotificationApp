package com.example.notificationapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "my_channel"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }

        val notifyButton = findViewById<Button>(R.id.notifyButton)
        notifyButton.setOnClickListener {
            showNotification()
        }
    }
    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = "My Notification Channel"
            val description = "Channel for notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(
                CHANNEL_ID,
                name,
                importance
            )

            channel.description = description

            val manager = getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            manager.createNotificationChannel(channel)
        }
    }

    // Afficher la notification
    private fun showNotification() {

        // Intent pour ouvrir l'application
        val intent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        // Construire la notification
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Notification Title")
            .setContentText("This is a notification message")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Vérifier la permission avant affichage
        if (Build.VERSION.SDK_INT < 33 ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            NotificationManagerCompat.from(this)
                .notify(1, builder.build())
        }
    }
}
