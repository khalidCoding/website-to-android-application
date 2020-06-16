package com.tutorial.sitetoapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {


        val body : String?
        val title : String?
        val url  :String?

        if(remoteMessage.data.isNotEmpty()){
            url = remoteMessage.data["url"]
            title = remoteMessage.data["title"]
            body = remoteMessage.data["body"]
            displayNotification(body,title,url)


        }


    }


    private fun displayNotification(body:String?,title:String?,url:String?){
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("url",url)
        intent.setAction("new_post")
        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)
        val channelId = "myNotification"
        val notificationBuilder = NotificationCompat.Builder(this,channelId)
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            notificationBuilder.setSmallIcon(R.drawable.notification_icon)
            notificationBuilder.setColor(Color.BLACK)
        }else{
            notificationBuilder.setSmallIcon(R.drawable.notification_icon)
        }

            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId,"firebase_notification",NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)

        }

        notificationManager.notify(0,notificationBuilder.build())

    }


}