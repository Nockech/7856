package com.example.taskhub.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.taskhub.MainActivity
import com.example.taskhub.R
import com.example.taskhub.database.Task
import java.text.SimpleDateFormat
import java.util.*

class NotifyService {
    companion object{
        fun initNotifyService(context: Context) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = context.getString(R.string.notify_channel_name)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(context.getString(R.string.notify_channel_id), name, importance)

                // Register the channel with the system
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                notificationManager.createNotificationChannel(channel)
            }
        }

        fun setNotification(ctx: Context, task: Task, delay: String) {
            val timeDelay = List(delay.split(":").size) { i -> delay.split(":")[i].toInt() }

            val taskDateRaw = task.date.split(".").reversed()
            val taskDate: List<Int> = List(taskDateRaw.size) { j -> taskDateRaw[j].toInt()}

            val taskTimeRaw = task.time.split(":")
            val taskTime: List<Int> = List(taskTimeRaw.size) { k -> taskTimeRaw[k].toInt() }

            //alarm manager
            val alarmManager = ctx.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            //intent to receiver
            val intentToTimerReceiver = Intent(ctx.applicationContext, TimeReceiver::class.java)
            intentToTimerReceiver.action = task.title + " is planned on " + task.time

            //calendar
            val alarmDate = Calendar.getInstance()
            alarmDate.timeInMillis = System.currentTimeMillis()
            alarmDate.set(taskDate[0], taskDate[1] - 1, taskDate[2], taskTime[0],taskTime[1])

            alarmDate.add(Calendar.HOUR_OF_DAY, -timeDelay[0])
            alarmDate.add(Calendar.MINUTE, -timeDelay[1])

            //pending intent
            @SuppressLint("UnspecifiedImmutableFlag")
            val pendingIntent = PendingIntent.getBroadcast(
                ctx,
                task.id,
                intentToTimerReceiver,
                PendingIntent.FLAG_CANCEL_CURRENT
            )

            //alarm set
            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(alarmDate.timeInMillis, pendingIntent),
                pendingIntent
            )


            Log.d(TimeManager.TAG, "===================================")
            Log.d(TimeManager.TAG, task.title)
            Log.d(TimeManager.TAG, task.time + " " + task.date)
            Log.d(TimeManager.TAG, "Set alarm to: " + alarmDate.get(Calendar.HOUR_OF_DAY) + SimpleDateFormat(":mm / d.MM.yyyy", Locale.getDefault()).format(alarmDate.time))
            Log.d(TimeManager.TAG, "===================================")
        }
    }

    class TimeReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TimeManager.TAG, "onReceive Triggered!")
            if (intent != null) {
                Log.d(TimeManager.TAG, "Received: " + intent.action)

                val tapActionIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                @SuppressLint("UnspecifiedImmutableFlag")
                val tapPendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, tapActionIntent, 0)

                context?.let {
                    val builder = NotificationCompat.Builder(context, context.getString(R.string.notify_channel_id))
                        .setContentTitle("Task reminding")
                        .setSmallIcon(R.drawable.ic_menu_today)
                        .setContentText(intent.action)
                        .setStyle(
                            NotificationCompat.BigTextStyle()
                            .bigText(intent.action))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(tapPendingIntent)
                        .setAutoCancel(true)

                    with(NotificationManagerCompat.from(context)) {
                        notify(0, builder.build())
                    }
                }
            }
        }
    }
}