package com.example.worker;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import android.R.color;
import android.app.Activity;
import android.os.Bundle;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;

//needed to plot a pie chart
import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;


public class StatsActivity extends Activity
{
	SharedPreferences timeStorage;
	
	//list of segments for the pie chart
	List<Segment> segments = new ArrayList<Segment>();
	List<SegmentFormatter> formatter = new ArrayList<SegmentFormatter>();
	PieChart pie;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats);
		
		pie = findViewById(R.id.simplePieChart);
		
		timeStorage = getSharedPreferences("nouwaarom_worker_prefs", 0);
		
		Random rnd = new Random();
		
		//get all stored times
		Map<String,?> keys = timeStorage.getAll();
		
		//iterate over the stored times
		for(Map.Entry<String,?>entry:keys.entrySet())
		{
			//add the time to the pie
			segments.add(new Segment( entry.getKey(),(Integer)entry.getValue() ));
			formatter.add(new SegmentFormatter());
			
			//get the last item of the list and set the random color
			formatter.get(formatter.size() - 1).getFillPaint().setColor(rnd.nextInt());
			
			
			//add the segment to the pie
			pie.addSeries(segments.get(segments.size() - 1), formatter.get(segments.size() - 1));
		}
		
		pie.getBorderPaint().setColor(Color.TRANSPARENT);
		pie.getBackgroundPaint().setColor(Color.TRANSPARENT);
	}
}
