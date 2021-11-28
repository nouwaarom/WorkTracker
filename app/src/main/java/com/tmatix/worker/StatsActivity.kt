package com.tmatix.worker

import android.app.Activity
import android.os.Bundle

import android.graphics.Color
import android.view.Gravity
import android.widget.*

import com.androidplot.pie.PieChart
import com.androidplot.pie.Segment
import com.androidplot.pie.SegmentFormatter
import kotlin.random.Random
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * @author Elbert van de Put <elbert@amber.team>
 */
class StatsActivity : Activity() {
    private var isList = true

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        updateView()

        val clearButton = findViewById<Button>(R.id.clearButton)
        clearButton.setOnClickListener {
            val timeStorage = getSharedPreferences("nouwaarom_worker_prefs", 0)
            timeStorage.edit().clear().apply()

            updateView()
        }
    }

    private fun updateView() {
        if (isList) {
            createListView()
        } else {
            createChartView()
        }
    }

    private fun createListView() {
        setContentView(R.layout.activity_stats_list)

        val switchViewButton = findViewById<Button>(R.id.switchViewButton)
        switchViewButton.setOnClickListener {
            isList = false
            createChartView()
        }

        val timeStorage = getSharedPreferences("nouwaarom_worker_prefs", 0)

        val tasksByDuration = timeStorage.all.toList().sortedByDescending { (_, value) -> value as Int}.toMap()

        val table = findViewById<TableLayout>(R.id.table)
        table.setColumnStretchable(0, true)
        val headerRow = TableRow(this);
        val headerValue0 = TextView(this);
        headerValue0.text = "TASK";
        headerValue0.gravity = Gravity.START;
        headerValue0.setTextColor(Color.WHITE);
        headerRow.addView(headerValue0);
        val headerValue1 = TextView(this);
        headerValue1.text = "DURATION";
        headerValue0.gravity = Gravity.START;
        headerValue1.setTextColor(Color.WHITE);
        headerRow.addView(headerValue1);
        table.addView(headerRow)
        for (task in tasksByDuration) {
            val row = TableRow(this);
            val rowValue0 =  TextView(this);
            rowValue0.text = task.key;
            rowValue0.setTextColor(Color.WHITE);
            rowValue0.gravity = Gravity.START;
            row.addView(rowValue0);
            val rowValue1 = TextView(this);
            val duration = (task.value as Int).toDuration(DurationUnit.SECONDS)
            rowValue1.text = duration.toString();
            rowValue1.setTextColor(Color.WHITE);
            rowValue1.gravity = Gravity.END;
            row.addView(rowValue1);
            table.addView(row)
        }
    }

    private fun createChartView() {
        setContentView(R.layout.activity_stats_chart)

        val switchViewButton = findViewById<Button>(R.id.switchViewButton)
        switchViewButton.setOnClickListener {
            isList = true
            createListView()
        }

        val timeStorage = getSharedPreferences("nouwaarom_worker_prefs", 0)

        val pie = findViewById<PieChart>(R.id.simplePieChart)
        fillPieChart(timeStorage.all, pie)
    }

    private fun fillPieChart(keys: Map<String, Any?>, pie: PieChart) {
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
