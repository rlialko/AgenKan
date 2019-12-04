package com.agenkan.customer.presentation.main

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.agenkan.customer.R
import com.agenkan.customer.presentation.base.BaseActivity
import com.agenkan.customer.receiver.AlarmBroadcastReceiver
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class MainActivity : BaseActivity() {

    private val model: MainViewModel by viewModel()
    private var isBlocked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model.getUser().observe(this, Observer {
            textViewName.text = it.name
            textViewId.text = it.id
            textViewState.text = getString(R.string.text_blocked, it.isBlocked)
            isBlocked = it.isBlocked
            startCheck()
        })
        buttonStart.setOnClickListener {
            model.changeUserStatus()
        }
    }

    override fun onBackPressed() {
        if (!isBlocked) {
            super.onBackPressed()
        }
    }

    private fun startCheck() {
        val context = this
        val myIntent = Intent(context, AlarmBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0)

        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.SECOND, 5) // first time
        val frequency = (5 * 1000).toLong() // in
        if (isBlocked) {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                frequency,
                pendingIntent
            )
        } else {
            alarmManager.cancel(pendingIntent)
        }
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        }
    }

}
