package com.tmatix.worker

import android.app.Activity

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
    internal var timeWhenStopped: Long = 0

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
                getString(R.string.stop_timer) -> {
                    timeWhenStopped = chronometer.base - SystemClock.elapsedRealtime()
                    chronometer.stop()

                    b.setText(R.string.continue_timer)
                    saveButton.visibility = View.VISIBLE
                    resetButton.visibility = View.VISIBLE
                }
                getString(R.string.continue_timer) -> {
                    chronometer.base = SystemClock.elapsedRealtime() + timeWhenStopped
                    chronometer.start()

                    b.setText(R.string.stop_timer)
                    saveButton.visibility = View.INVISIBLE
                    resetButton.visibility = View.INVISIBLE
                }
                getString(R.string.start_timer) -> {
                    chronometer.base = SystemClock.elapsedRealtime()
                    chronometer.start()
                    b.setText(R.string.stop_timer)
                }
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
    }
}
