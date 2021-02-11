package com.udacity

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.util.cancelNotifications
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var notificationManager: NotificationManager

    companion object {
        const val EXTRA_FILE_NAME = "EXTRA_FILE_NAME"
        const val EXTRA_STATUS = "EXTRA_STATUS"
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

        notificationManager.cancelNotifications()
        //TODO What difference between intend , bundle , parcerable
        if (intent.hasExtra(EXTRA_FILE_NAME)) {
            filename_des_textview.text = intent.getStringExtra(EXTRA_FILE_NAME)
        }

        if (intent.hasExtra(EXTRA_STATUS)) {
            status_des_textview.text= intent.getStringExtra(EXTRA_STATUS)
        }

        ok_button.setOnClickListener{
            //TODO should i navigate back by startActivity(intent) or have better solution
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

}
