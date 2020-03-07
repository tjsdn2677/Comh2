package com.example.reminder2020

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fabOpened = false;

        fab_main.setOnClickListener {
            if (!fabOpened) {
                fabOpened = true;
                fab_map.animate().translationY(-resources.getDimension(R.dimen.standard_66))
                fab_time.animate().translationY(-resources.getDimension(R.dimen.standard_116))
            }
            else {
                fabOpened = false;
                fab_map.animate().translationY(0f);
                fab_time.animate().translationY(0f);
            }
        }


        fab_time.setOnClickListener {
            toast("TimeActivity");
            var intent = Intent(applicationContext, TimeActivity::class.java);
            startActivity(intent);

        }

        fab_map.setOnClickListener {
            toast("MapActivity");
            var intent = Intent(applicationContext, MapActivity::class.java);
            startActivity(intent);
        }


    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList(){

        doAsync {
            val db = Room.databaseBuilder(applicationContext, AppDataBase::class.java, "reminders").build()
            val reminders = db.reminderDao().getReminders()
            db.close()

            uiThread {

                if (reminders.isNotEmpty()){
                    val adapter = ReminderAdapter(applicationContext, reminders)
                    list.adapter = adapter
                }
                else{
                    toast("No reminders yet")
                }

            }
        }

    }

    companion object {

        val CHANNEL_ID="REMINDER_CHANNEL_ID"
        var NotificationID = 1567
        fun showNotification(context: Context, message: String){
            var notificationBuilder = NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(context.getString(R.string.app_name)).setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            var notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                val channel = NotificationChannel(CHANNEL_ID, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT
                ).apply { description = context.getString(R.string.app_name) }

                notificationManager.createNotificationChannel(channel)
            }
            val notification = NotificationID + Random(NotificationID).nextInt(1, 30)
            notificationManager.notify(notification, notificationBuilder.build())
        }

     }

}

