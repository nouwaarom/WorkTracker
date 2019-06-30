package com.tmatix.worker

import android.app.Activity

import android.os.Bundle
import android.content.SharedPreferences

import android.view.View

import android.widget.EditText
import android.widget.TextView
import android.widget.Button

/**
 * @author Elbert van de Put <elbert@amber.team>
 */
class SaveActivity : Activity() {
    internal var timeSeconds = 0

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)

        //retrieve the time we want to save
        val bundle = this.intent.extras
        timeSeconds = bundle!!.getInt("time")

        //display the time we got from the main activity
        val timerTextView = findViewById<View>(R.id.timer) as TextView
        timerTextView.setText(String.format("%02d:%02d:%02d",
                timeSeconds / 3600 % 24,
                timeSeconds / 60 % 60,
                timeSeconds % 60))

        //add an action to the save button
        val saveButton = findViewById<View>(R.id.saveButton) as Button
        saveButton.setOnClickListener {
            //add functionality to the enterTask EditText
            val timeTagEditText = this.findViewById<View>(R.id.enterTaskEditText) as EditText

            //save the data only if we filled in the tag
            if (!timeTagEditText.text.toString().isEmpty()) {
                val timeTag = timeTagEditText.text.toString()

                //save the time and timeTag
                val timeStorage = getSharedPreferences("nouwaarom_worker_prefs", 0)
                val editor = timeStorage.edit()
                editor.putInt(timeTag, timeSeconds)
                editor.apply()

                finish()
            }
        }
    }
}
