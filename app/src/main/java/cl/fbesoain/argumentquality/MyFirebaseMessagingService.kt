package cl.fbesoain.argumentquality

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", remoteMessage.from)
        if (remoteMessage.data.size > 0)
            Log.d("FCM", "PAYLOAD: " + remoteMessage.data)
        if (remoteMessage.notification != null) {
            Log.d("FCM", "NOTIFICATION: " + remoteMessage.notification!!.body)
            sendNotification(remoteMessage)
        }
    }

    fun sendNotification(message: RemoteMessage) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder: NotificationCompat.Builder

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel("notChannel", "Default notifications",
                    NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description = "Notification channel"
            notificationManager.createNotificationChannel(notificationChannel)
            notificationBuilder = NotificationCompat.Builder(this, "notChannel")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(message.notification!!.title)
                    .setContentText(message.notification!!.body)
                    .setPriority(message.priority)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSound(soundUri)
                    .setAutoCancel(true)
        } else {
            notificationBuilder = NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(message.notification!!.title)
                    .setContentText(message.notification!!.body)
                    .setPriority(message.priority)
                    .setContentIntent(pendingIntent)
                    .setSound(soundUri)
                    .setAutoCancel(true)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

}