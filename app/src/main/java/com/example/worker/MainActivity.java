package com.example.worker;

import android.app.Activity;

import android.os.Bundle;
import android.os.SystemClock;

import android.content.Intent;

import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class MainActivity extends Activity
{	
	Chronometer chronometer;

	long timeWhenStopped = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //create buttons variable
        final Button timerButton = (Button) findViewById(R.id.timerButton);
        final Button saveButton =  (Button) findViewById(R.id.saveTimeButton);
        final Button resetButton = (Button) findViewById(R.id.resetTimeButton);
        saveButton.setVisibility(View.INVISIBLE);
        resetButton.setVisibility(View.INVISIBLE);
        Button statsButton = findViewById(R.id.selectStatsButton);
        
        //assign chronometer variable
        chronometer = findViewById(R.id.awesomeChronoMeter);
        
        //add an action to the start button click
        timerButton.setOnClickListener(new View.OnClickListener()
        {	
			@Override
			public void onClick(View v)
			{	
				Button b = (Button) v;
                if (b.getText().equals(getString(R.string.stop_timer)))
                {
					timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                    chronometer.stop();

                    b.setText(R.string.continue_timer);
                    saveButton.setVisibility(View.VISIBLE);
                    resetButton.setVisibility(View.VISIBLE);
                }
                else if(b.getText().equals(getString(R.string.continue_timer)))
                {
					chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                	chronometer.start();

                	b.setText(R.string.stop_timer);
                	saveButton.setVisibility(View.INVISIBLE);
                	resetButton.setVisibility(View.INVISIBLE);
                }
                else if(b.getText().equals(getString(R.string.start_timer)))
                {
					chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    b.setText(R.string.stop_timer);
                }
			}
		});
        
        //add an action to the save button click
        saveButton.setOnClickListener(new View.OnClickListener()
        {
			@Override
			public void onClick(View v)
			{				
				Bundle bundle = new Bundle();
				bundle.putInt("time", (int)(( SystemClock.elapsedRealtime()-chronometer.getBase() )/1000));
				
				Intent saveIntent = new Intent(v.getContext(), SaveActivity.class);
				saveIntent.putExtras(bundle);
				v.getContext().startActivity(saveIntent);
			}
		});
        
        //add an action to the reset button click
        resetButton.setOnClickListener(new View.OnClickListener()
        {
			@Override
			public void onClick(View v)
			{
				chronometer.setBase(SystemClock.elapsedRealtime());
				chronometer.start();
            	timerButton.setText(R.string.stop_timer);
            	saveButton.setVisibility(View.INVISIBLE);
            	resetButton.setVisibility(View.INVISIBLE);
			}
		});
        
        //add an action to the statistics button click
        statsButton.setOnClickListener(new View.OnClickListener()
        {
			@Override
			public void onClick(View v)
			{
				Intent statsIntent = new Intent(v.getContext(), StatsActivity.class);
				v.getContext().startActivity(statsIntent);
			}
		});
    }
}
