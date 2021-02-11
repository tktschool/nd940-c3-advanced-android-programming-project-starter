package com.udacity

import android.app.DownloadManager
import android.app.DownloadManager.Query
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.util.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var downloadUrl: String = ""
    private var downloadFileName: String = ""

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    lateinit var custom_button: LoadingButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        notificationManager = ContextCompat.getSystemService(
            application,
            NotificationManager::class.java
        ) as NotificationManager
        createChannel(
            getString(R.string.download_notification_channel_id),
            getString(R.string.download_notification_channel_name)
        )

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        custom_button = findViewById(R.id.custom_button)


        download_radio_group.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            downloadUrl = when (i) {
                R.id.udacity_radio_button -> {
                    downloadFileName = applicationContext.getString(R.string.load_app_text)
                    UDACITY_URL
                }
                R.id.glide_radio_button
                -> {
                    downloadFileName = applicationContext.getString(R.string.glide_text)
                    GLIDE_URL
                }
                R.id.retrofit_radio_button
                -> {
                    downloadFileName = applicationContext.getString(R.string.retrofit_text)
                    RETROFIT_URL
                }
                else -> ""
            }
        }

        custom_button.setOnClickListener {
            custom_button.setState(ButtonState.Clicked)
            download()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            custom_button.setState(ButtonState.Completed)

            //Code from stackoverflow.com
            if (downloadID == id) {
                if (intent.action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                    val query = Query().setFilterById(downloadID)
                    val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                    val cursor = downloadManager.query(query)
                    if (cursor.moveToFirst()) {
                        if (cursor.count > 0) {
                            val statusDownloadManager = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                            val titleDownloadManager = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
                            if (statusDownloadManager == DownloadManager.STATUS_SUCCESSFUL) {
                                Toast.makeText(context, getString(R.string.download_completed) + downloadID.toString(), Toast.LENGTH_LONG).show()
                                notificationManager.sendNotification(
                                    titleDownloadManager, "Success",downloadID,
                                    application
                                )
                                cursor.close()
                            } else {
                                Toast.makeText(context, getString(R.string.download_failed) + downloadID.toString(), Toast.LENGTH_SHORT).show()
                                notificationManager.sendNotification(
                                    titleDownloadManager, "Fail",downloadID,
                                    application
                                )
                                cursor.close()
                            }
                        }
                    }
                }
            }


        }
    }

    private fun download() {
        //Check radiogroup selection
        if (downloadUrl.isNotEmpty()) {
            custom_button.setState(ButtonState.Loading)
            val request =
                DownloadManager.Request(Uri.parse(UDACITY_URL))
                    .setTitle(downloadFileName)
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        } else {
            custom_button.setState(ButtonState.Completed)
            Toast.makeText(
                applicationContext,
                resources.getString(R.string.please_select),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun createChannel(channelId: String, channelName: String) {
        // create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description =
                getString(R.string.download_notification_channel_description)
            notificationChannel.setShowBadge(false)
            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

    companion object {

        private const val UDACITY_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val GLIDE_URL =
            "https://github.com/bumptech/glide/archive/master.zip"
        private const val RETROFIT_URL =
            "https://github.com/square/retrofit/archive/master.zip"

        private const val CHANNEL_ID = "channelId"
    }

}
