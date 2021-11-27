package com.tmatix.worker

import android.app.Activity

/**
 * @author Elbert van de Put <elbert@amber.team>
 */
class SaveActivityPresenter(val view: SaveActivityView, val activity: Activity) {
    fun onSave() {
        val description = view.description.title
        val timeSeconds = view.time.getTimeInSeconds()

        //save the data only if we filled in the tag
        if (description.isEmpty()) {
            return;
        }

        //save the time and timeTag
        val timeStorage = activity.getSharedPreferences("nouwaarom_worker_prefs", 0)
        val editor = timeStorage.edit()
        editor.putInt(description, timeSeconds)
        editor.apply()

        activity.finish()
    }
}