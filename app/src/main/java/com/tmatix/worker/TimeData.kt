package com.tmatix.worker

import android.databinding.BaseObservable
import android.databinding.Bindable

/**
 * @author Elbert van de Put <elbert@amber.team>
 */
class TimeData(private var timeSeconds: Int) : BaseObservable() {

    @Bindable
    fun getTime(): String {
        return String.format("%02d:%02d:%02d",
                timeSeconds / 3600 % 24,
                timeSeconds / 60 % 60,
                timeSeconds % 60)
    }

    fun getTimeInSeconds(): Int {
        return timeSeconds
    }

    fun setTimeInSeconds(timeInSeconds: Int) {
        timeSeconds = timeInSeconds
        notifyPropertyChanged(BR.time)
    }
}