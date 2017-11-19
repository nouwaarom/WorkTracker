package com.example.worker;

import android.app.Activity;

import android.os.Bundle;
import android.content.SharedPreferences;

import android.view.View;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import java.util.Locale;

public class SaveActivity extends Activity
{
	int timeSeconds = 0;
	SharedPreferences timeStorage;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save);
		
		//retrieve the time we want to save
		Bundle bundle = this.getIntent().getExtras();
		timeSeconds = bundle.getInt("time");
		
		//display the time we got from the main activity
		TextView timerTextView = (TextView) findViewById(R.id.timer);
		timerTextView.setText(String.format("%02d:%02d:%02d",
				 							(timeSeconds / 3600) % 24,
				 							(timeSeconds / 60) % 60,
				 							timeSeconds % 60));
		
		//add functionality to the enterTask EditText
		final EditText timeTagEditText = (EditText) this.findViewById(R.id.enterTaskEditText);
		
		timeStorage = getSharedPreferences("nouwaarom_worker_prefs", 0);
		
		//add an action to the save button
		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//save the data only if we filled in the tag
				if(!timeTagEditText.getText().toString().isEmpty())
				{
					String timeTag = timeTagEditText.getText().toString();
				
					//save the time and timeTag
					SharedPreferences.Editor editor = timeStorage.edit();
					editor.putInt(timeTag, timeSeconds);
					editor.apply();
					
					finish();
				}
			}
		});
	}
}
