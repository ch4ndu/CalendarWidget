package com.udn4hc.calendarwidget;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateEvent extends Activity {
	public static int month, year, dayOfMonth;
	TimePicker startTimep;
	TimePicker endTimep;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		month = intent.getIntExtra("month", 0);
		year = intent.getIntExtra("year", 0);
		dayOfMonth = intent.getIntExtra("dayofmonth", 0);
		TextView tv = (TextView) findViewById(R.id.event_title_textview);
		tv.setText(String.valueOf(month + 1) + "/" + String.valueOf(dayOfMonth)
				+ "/" + String.valueOf(year));

	}

	public void addEvent(View view) {

		startTimep = (TimePicker) findViewById(R.id.starttimepicker);
		int start_hour = startTimep.getCurrentHour();
		int start_minute = startTimep.getCurrentMinute();
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(year, month, dayOfMonth, start_hour, start_minute);
		long startMillis = beginTime.getTimeInMillis();

		endTimep = (TimePicker) findViewById(R.id.endtimepicker);
		int end_hour = endTimep.getCurrentHour();
		int end_minute = endTimep.getCurrentMinute();
		Calendar endTime = Calendar.getInstance();
		endTime.set(year, month, dayOfMonth, end_hour, end_minute);
		long endMillis = endTime.getTimeInMillis();

		TextView eventDescription = (TextView) findViewById(R.id.event_description_edittext);
		TextView eventTitle = (TextView) findViewById(R.id.event_title_edittext);
		String eventDescStr = eventDescription.getText().toString();
		String eventtitleStr = eventTitle.getText().toString();

		ContentResolver cr = getContentResolver();
		ContentValues values = new ContentValues();
		values.put(Events.CALENDAR_ID, MainActivity.CalID);
		values.put(Events.DTSTART, startMillis);
		values.put(Events.DTEND, endMillis);
		values.put(Events.TITLE, eventtitleStr);
		values.put(Events.DESCRIPTION, eventDescStr);
		values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().toString());
		Uri uri = cr.insert(Events.CONTENT_URI, values);

		long eventID = Long.parseLong(uri.getLastPathSegment());

		Toast.makeText(getBaseContext(), "Event successfully added",
				Toast.LENGTH_LONG).show();
		Intent intent = new Intent(getApplicationContext(), GetEvents.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("year", year);
		intent.putExtra("month", month);
		intent.putExtra("day", dayOfMonth);
		startActivity(intent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_event, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
