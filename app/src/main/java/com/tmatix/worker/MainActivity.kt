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
        val action = intent.getStringExtra("action")
        when (action) {
            "pause" -> stopTimer()
            "continue" -> continueTimer()
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
                getString(R.string.continue_timer) -> continueTimer()
                getString(R.string.start_timer) -> startTimer(SystemClock.elapsedRealtime())
            }
        }

        //add an action to the save button click
        saveButton.setOnClickListener { v ->
            val bundle = Bundle()
            bundle.putInt("time", ((SystemClock.elapsedRealtime() - chronometer.base) / 1000).toInt())

            val saveIntent = Intent(v.context, SaveActivity::class.java)
            saveIntent.putExtras(bundle)

            resetTimer()
            v.context.startActivity(saveIntent)
        }

        //add an action to the reset button click
        resetButton.setOnClickListener { resetTimer() }

        //add an action to the statistics button click
        statsButton.setOnClickListener { v ->
            val statsIntent = Intent(v.context, StatsActivity::class.java)
            v.context.startActivity(statsIntent)
        }

        // TODO, make sure this is executed only once
        createNotificationChannel()
    }

    override fun onStop() {
        super.onStop()
        // We remove our notification in order to not leave dangling notifications
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        channel.setSound(null, null)
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

        showPauseTimerNotification()
    }

    private fun continueTimer() {
        startTimer(SystemClock.elapsedRealtime() + timeWhenStopped)
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

        // Replace the pause timer notification by a continue timer notification.
        showContinueTimerNotification()
    }

    private fun resetTimer() {
        val chronometer = findViewById<Chronometer>(R.id.awesomeChronoMeter)
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.stop()

        val timerButton = findViewById<Button>(R.id.timerButton)
        timerButton.setText(R.string.start_timer)

        val saveButton = findViewById<Button>(R.id.saveTimeButton)
        val resetButton = findViewById<Button>(R.id.resetTimeButton)
        saveButton.visibility = View.INVISIBLE
        resetButton.visibility = View.INVISIBLE

        // The timer is not running, so we remove the notification.
        removeTimerNotification()
    }

    private fun removeTimerNotification() {
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(0)
    }

    private fun showContinueTimerNotification() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("action", "continue")
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val notification = Notification.Builder(
            this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Taking a break")
            .setSmallIcon(R.drawable.ic_wrench)
            .addAction(R.drawable.baseline_play_arrow_24, "CONTINUE", pendingIntent)
            .setStyle(Notification.MediaStyle()
                .setShowActionsInCompactView(0)) // Draws the play button at the right side of the notification.
            .build()

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)
    }

    private fun showPauseTimerNotification() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("action", "pause")
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val notification = Notification.Builder(
            this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Work in progress")
            .setSmallIcon(R.drawable.ic_wrench)
            .addAction(R.drawable.baseline_pause_24, "PAUSE", pendingIntent)
            .setStyle(Notification.MediaStyle()
                .setShowActionsInCompactView(0)) // Draws the pause action at the right side of the notification.
            .build()

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)
    }
}
