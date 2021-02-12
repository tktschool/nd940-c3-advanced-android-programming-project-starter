package com.udacity

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var notificationManager: NotificationManager

    companion object {
        const val EXTRA_FILE_NAME = "EXTRA_FILE_NAME"
        const val EXTRA_STATUS = "EXTRA_STATUS"
        const val EXTRA_NOTIFICATION_ID = "EXTRA_NOTIFICATION_ID"
        fun starterIntent(context: Context): Intent {
            return Intent(context, DetailActivity::class.java)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)


        notificationManager = ContextCompat.getSystemService(
            application,
            NotificationManager::class.java
        ) as NotificationManager

        if (intent.hasExtra(EXTRA_FILE_NAME)) {
            filename_des_textview.text = intent.getStringExtra(EXTRA_FILE_NAME)
        }

        if (intent.hasExtra(EXTRA_STATUS)) {
            status_des_textview.text = intent.getStringExtra(EXTRA_STATUS)
        }
        if (intent.hasExtra(EXTRA_NOTIFICATION_ID)) {
            Log.v("intend", EXTRA_NOTIFICATION_ID)
            intent.getStringExtra(EXTRA_NOTIFICATION_ID)?.toIntOrNull()?.let {
                notificationManager.cancel(it)
            }
        }

        ok_button.setOnClickListener {
            //Fix issue after clicking on OK button
            // Add finish() and  override fun onBackPressed(){  finish() } in this activity
            finish()
        }

    }

    override fun onBackPressed() {
        finish()
    }

}
