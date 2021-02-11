package com.udacity.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.MainActivity
import com.udacity.R


// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(fileName: String, status: String,downloadId: Long, applicationContext: Context) {

    // Create the content intent for the notification, which launches this activity
    // create intent
    val contentIntent = DetailActivity.starterIntent(applicationContext)
    contentIntent.putExtra(DetailActivity.EXTRA_FILE_NAME, fileName)
    contentIntent.putExtra(DetailActivity.EXTRA_STATUS, status)
    //  create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        downloadId.toInt(),
        contentIntent,
        PendingIntent.FLAG_ONE_SHOT
    )

    // Build the notification
    //TODO How to to separate every notification (now it look like update current notification)
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.download_notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(
            applicationContext
                .getString(R.string.notification_title)
        )
        .setContentText(applicationContext.getString(R.string.notification_description))
        //.setContentIntent(contentPendingIntent)
        //when the user taps on the notification, the notification dismisses itself
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.notification_button),
            contentPendingIntent
        )

    notify(NOTIFICATION_ID, builder.build())


}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}


