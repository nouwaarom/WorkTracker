package com.tmatix.worker

import android.app.*
import android.content.Context

import android.os.Bundle
import android.os.SystemClock

import android.content.Intent

import android.view.View
import android.widget.Button
import android.widget.Chronometer

/**
 * @author Elbert van de Put <elbert@amber.team>
 */
class MainActivity : Activity() {
    private var timeWhenStopped: Long = 0

    private val NOTIFICATION_CHANNEL_ID = "WORKER_NOTIFICATION_CHANNEL";

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        when (intent.getStringExtra("action")) {
            "stop" -> stopTimer()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //create buttons variable
        val timerButton = findViewById<Button>(R.id.timerButton)
        val saveButton = findViewById<Button>(R.id.saveTimeButton)
        val resetButton = findViewById<Button>(R.id.resetTimeButton)
        saveButton.visibility = View.INVISIBLE
        resetButton.visibility = View.INVISIBLE
        val statsButton = findViewById<Button>(R.id.selectStatsButton)

        //assign chronometer variable
        val chronometer = findViewById<Chronometer>(R.id.awesomeChronoMeter)

        //add an action to the start button click
        timerButton.setOnClickListener { v ->
            val b = v as Button
            when (b.text) {
                getString(R.string.stop_timer) -> stopTimer()
                getString(R.string.continue_timer) -> startTimer(SystemClock.elapsedRealtime() + timeWhenStopped)
                getString(R.string.start_timer) -> startTimer(SystemClock.elapsedRealtime())
            }
        }

        //add an action to the save button click
        saveButton.setOnClickListener { v ->
            val bundle = Bundle()
            bundle.putInt("time", ((SystemClock.elapsedRealtime() - chronometer.base) / 1000).toInt())

            val saveIntent = Intent(v.context, SaveActivity::class.java)
            saveIntent.putExtras(bundle)
            v.context.startActivity(saveIntent)
        }

        //add an action to the reset button click
        resetButton.setOnClickListener {
            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.stop()
            timerButton.setText(R.string.start_timer)
            saveButton.visibility = View.INVISIBLE
            resetButton.visibility = View.INVISIBLE
        }

        //add an action to the statistics button click
        statsButton.setOnClickListener { v ->
            val statsIntent = Intent(v.context, StatsActivity::class.java)
            v.context.startActivity(statsIntent)
        }

        // TODO, make sure this is executed only once
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun startTimer(startTime: Long) {
        val chronometer = findViewById<Chronometer>(R.id.awesomeChronoMeter)
        chronometer.base = startTime //SystemClock.elapsedRealtime()
        chronometer.start()

        val timerButton = findViewById<Button>(R.id.timerButton)
        timerButton.setText(R.string.stop_timer)

        val saveButton = findViewById<Button>(R.id.saveTimeButton)
        val resetButton = findViewById<Button>(R.id.resetTimeButton)
        saveButton.visibility = View.INVISIBLE
        resetButton.visibility = View.INVISIBLE

        showTimerNotification()
    }

    private fun stopTimer() {
        val chronometer = findViewById<Chronometer>(R.id.awesomeChronoMeter)
        timeWhenStopped = chronometer.base - SystemClock.elapsedRealtime()
        chronometer.stop()

        val timerButton = findViewById<Button>(R.id.timerButton)
        timerButton.setText(R.string.continue_timer)

        val saveButton = findViewById<Button>(R.id.saveTimeButton)
        val resetButton = findViewById<Button>(R.id.resetTimeButton)
        saveButton.visibility = View.VISIBLE
        resetButton.visibility = View.VISIBLE

        // Remove the timer notification as the timer is not running
        removeTimerNotification()
    }

    private fun removeTimerNotification() {
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(0)
    }

    private fun showTimerNotification() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("action", "stop")
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val notification = Notification.Builder(
            this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Work in progress")
            .setSmallIcon(R.drawable.ic_wrench)
            .addAction(R.drawable.baseline_pause_24, "PAUSE", pendingIntent)
            .build()

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)
    }
}
