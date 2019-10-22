package com.tmatix.worker

import android.app.Activity
import android.os.Bundle

import android.graphics.Color
import android.widget.Button

import com.androidplot.pie.PieChart
import com.androidplot.pie.Segment
import com.androidplot.pie.SegmentFormatter
import kotlin.random.Random

/**
 * @author Elbert van de Put <elbert@amber.team>
 */
class StatsActivity : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        val clearButton = findViewById<Button>(R.id.clearButton)
        clearButton.setOnClickListener {
            val timeStorage = getSharedPreferences("nouwaarom_worker_prefs", 0)
            timeStorage.edit().clear().apply()

            val pie = findViewById<PieChart>(R.id.simplePieChart)
            fillPieChart(mapOf(), pie)
        }

        val pie = findViewById<PieChart>(R.id.simplePieChart)
        val timeStorage = getSharedPreferences("nouwaarom_worker_prefs", 0)
        fillPieChart(timeStorage.all, pie)
    }

    fun fillPieChart(keys: Map<String, Any?>, pie: PieChart) {
        pie.clear()
        //iterate over the stored times
        for ((key, value) in keys) {
            //add the time to the pie
            val segment = Segment(key, value as Int)
            val formatter = SegmentFormatter(Color.HSVToColor(floatArrayOf(Random.nextFloat()*360, 1.0f, 0.8f)))

            //add the segment to the pie
            pie.addSeries(segment, formatter)
        }

        pie.borderPaint.color = Color.TRANSPARENT
        pie.backgroundPaint.color = Color.TRANSPARENT
        pie.redraw()
    }
}
