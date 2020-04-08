package com.agenkan.app.presentation.service


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import com.agenkan.app.R
import com.agenkan.app.data.ContentResult
import com.agenkan.app.data.preferences.AppPreferences
import com.agenkan.app.data.repository.DataRepository
import com.agenkan.app.presentation.ui.MainActivity
import com.agenkan.app.presentation.util.ShaConverter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class ProtectService : Service(), KoinComponent {

    private val db: DataRepository by inject()
    private val pref: AppPreferences by inject()
    private var wm: WindowManager? = null
    private var overlayView: LinearLayout? = null
    private var editPassword: EditText? = null
    private var buttonUnlock: TextView? = null


    override fun onCreate() {
        super.onCreate()
        wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val myUserLiveData = db.observeDevice()
        myUserLiveData.observeForever {
            if (it is ContentResult.Success && it.data != null && it.data.isBlocked) {
                if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                    startActivity(MainActivity.getRestartIntent(this))
                } else {
                    if (overlayView == null) {
                        createOverDrawView()
                        val layoutFlags: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                        } else {
                            WindowManager.LayoutParams.TYPE_PHONE
                        }

                        val params = WindowManager.LayoutParams(
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT,
                            layoutFlags,
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                            PixelFormat.TRANSLUCENT
                        )
                        params.gravity = Gravity.START or Gravity.TOP
                        params.x = 0
                        params.y = 0
                        wm!!.addView(overlayView, params)
                    }
                }
            } else {
                if (overlayView != null) {
                    wm!!.removeView(overlayView)
                    overlayView = null
                }
                stopSelf()
            }
        }
        startForeground()
    }

    private fun createOverDrawView() {
        overlayView = LinearLayout(this)
        overlayView!!.setBackgroundColor(Color.WHITE)
        LayoutInflater.from(this).inflate(R.layout.fragment_protect, overlayView)
        editPassword = overlayView?.findViewById(R.id.editPassword)
        buttonUnlock = overlayView?.findViewById(R.id.buttonUnlock)

        buttonUnlock?.setOnClickListener {
            checkPasswordAndUnlock(editPassword?.text.toString())
        }
    }

    private fun checkPasswordAndUnlock(password: String) {
        GlobalScope.launch {
            if (pref.getHash() == ShaConverter.convert(password)) {
                db.setBlocked(false)
            }
        }
    }

    private fun startForeground() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel()
            } else {
                ""
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(101, notification)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String {
        val channelId = "protect_service"
        val channelName = getString(R.string.protect_chanel_name)
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_HIGH
        )
        chan.lightColor = Color.BLUE
        chan.importance = NotificationManager.IMPORTANCE_NONE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    override fun onDestroy() {
        super.onDestroy()
        if (overlayView != null) {
            wm!!.removeView(overlayView)
            overlayView = null
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        fun start(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(Intent(context, ProtectService::class.java))
            } else {
                context.startService(Intent(context, ProtectService::class.java))
            }
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, ProtectService::class.java))
        }

    }
}