package com.agenkan.app.presentation.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.content.ContextCompat
import com.agenkan.app.R
import com.agenkan.app.data.repository.DataRepository
import com.agenkan.app.presentation.ui.MainActivity
import com.agenkan.app.presentation.util.AlarmHelper
import com.agenkan.app.presentation.util.PolicyManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class PushService : FirebaseMessagingService(), KoinComponent {

    private val repo: DataRepository by inject()
    private val policyManager: PolicyManager by inject()

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.let {
            if (it.title != null) {
                showNotification(this, it.title, it.body)
            }
        }
        val blocked: Boolean? = message.data["blocked"]?.equals("true")
        val passwordHash: String? = message.data["passwordHash"]
        blocked?.let {
            handleBlocked(this, blocked, passwordHash)
        }
        val trigger: String? = message.data["trigger"]
        trigger?.let {
            handleTrigger(it)
        }

    }

    private fun handleTrigger(trigger: String) {
        GlobalScope.launch {
            when (trigger) {
                TriggerType.VERIFICATION.id -> {

                }
                TriggerType.DEACTIVATE.id -> {
                    policyManager.deactivate()
                    repo.clearData()
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
                TriggerType.PROFILE_CHANGED.id -> {
                    repo.getProfile()
                }
            }
        }
    }

    enum class TriggerType(val id: String) {
        VERIFICATION("verification"),
        DEACTIVATE("deactivate"),
        PROFILE_CHANGED("profileChanged")
    }

    private fun handleBlocked(context: Context, isBlocked: Boolean, passwordHash: String?) {
        GlobalScope.launch {
            repo.setBlocked(isBlocked, passwordHash)
            repo.getProfile()

            AlarmHelper.updatedAlarm(context, isBlocked)

            if (isBlocked) {
                ProtectService.start(context)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        GlobalScope.launch { repo.updatePushToken(token) }
    }

    private fun showNotification(context: Context, title: String?, body: String?) {
        val soundUri =
            Uri.parse("android.resource://" + packageName + "/" + R.raw.raw_notification)
        //        final Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder: Notification.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANEL_ID,
                CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )
            val att = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            channel.setSound(soundUri, att)
            notificationManager.createNotificationChannel(channel)
            notificationBuilder = Notification.Builder(context, CHANEL_ID)
            notificationBuilder.setChannelId(CHANEL_ID)
            notificationManager.getNotificationChannel(CHANEL_ID).setSound(soundUri, att)
        } else {
            notificationBuilder = Notification.Builder(context)
        }
        notificationBuilder
            .setSmallIcon(R.mipmap.ic_stat_main)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
            .setContentTitle(title)
            .setContentText(body)
            .setTicker(body)
            .setColor(ContextCompat.getColor(this, R.color.colorAccent))
            .setPriority(Notification.PRIORITY_HIGH)
            .setSound(soundUri)
            .setAutoCancel(true)
        val pIntent = PendingIntent.getActivity(
            context, System.currentTimeMillis().toInt(),
            MainActivity.getRestartIntent(context), PendingIntent.FLAG_UPDATE_CURRENT
        )
        if (pIntent != null) {
            notificationBuilder.setContentIntent(pIntent)
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    companion object {
        private const val CHANEL_ID = "notifications_id"
        private const val CHANEL_NAME = "Notifications"
        private const val NOTIFICATION_ID = 1
    }
}