package com.tmatix.worker

import java.util.ArrayList
import java.util.Random

import android.app.Activity
import android.os.Bundle

import android.content.SharedPreferences
import android.graphics.Color

import com.androidplot.pie.PieChart
import com.androidplot.pie.Segment
import com.androidplot.pie.SegmentFormatter

/**
 * @author Elbert van de Put <elbert@amber.team>
 */
class StatsActivity : Activity() {

    //list of segments for the pie chart
    internal var segments: MutableList<Segment> = ArrayList()
    internal var formatter: MutableList<SegmentFormatter> = ArrayList()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        val pie = findViewById(R.id.simplePieChart) as PieChart

        val timeStorage = getSharedPreferences("nouwaarom_worker_prefs", 0)

        val rnd = Random()

        //get all stored times
        val keys = timeStorage.all

        //iterate over the stored times
        for ((key, value) in keys) {
            //add the time to the pie
            segments.add(Segment(key, value as Int))
            formatter.add(SegmentFormatter(null))

            //get the last item of the list and set the random color
            formatter[formatter.size - 1].fillPaint.color = Color.HSVToColor(floatArrayOf(rnd.nextFloat()*360, 1.0f, 0.8f))

            //add the segment to the pie
            pie.addSeries(segments[segments.size - 1], formatter[segments.size - 1])
        }

        pie.borderPaint.color = Color.TRANSPARENT
        pie.backgroundPaint.color = Color.TRANSPARENT
    }
}
