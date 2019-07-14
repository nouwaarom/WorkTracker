package com.tmatix.worker

import android.app.Activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil

import com.tmatix.worker.databinding.ActivitySaveBinding

/**
 * @author Elbert van de Put <elbert@amber.team>
 */
class SaveActivity : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val saveActivityView = SaveActivityView(TimeData(0), WorkDescriptionData(""))
        val saveActivityPresenter = SaveActivityPresenter(saveActivityView, this)

        val binding: ActivitySaveBinding = DataBindingUtil.setContentView(this, R.layout.activity_save)
        binding.setTime(saveActivityView.time)
        binding.setDescription(saveActivityView.description)
        binding.setPresenter(saveActivityPresenter)

        //retrieve the time we want to save
        val bundle = this.intent.extras
        val timeSeconds = bundle!!.getInt("time")
        saveActivityView.time.setTimeInSeconds(timeSeconds)
    }
}
