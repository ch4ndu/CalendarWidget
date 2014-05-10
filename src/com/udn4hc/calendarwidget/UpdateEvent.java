package com.udn4hc.calendarwidget;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Instances;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

public class UpdateEvent extends Activity {
	static String title;
	static String description;
	static long begin;
	static long end;
	static long id;
	static int year;
	static int dayOfMonth;
	static int month;
	static int start_hour;
	static int start_minute;
	static int end_hour;
	static int end_minute;
	static Event event;
	TextView titleview, descriptionview, dateview;
	TimePicker beginPicker, endPicker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_event);
		Intent intent = getIntent();
		event = new Event();
		event = (Event) intent.getSerializableExtra("event");
		title = event.getTitle();
		description = event.getDescription();
		begin = event.getBeginMilli();
		end = event.getEndMilli();
		id = event.getID();
		titleview = (TextView) findViewById(R.id.event_title_edittext_update);
		descriptionview = (TextView) findViewById(R.id.event_description_edittext_update);
		dateview = (TextView) findViewById(R.id.event_title_textview_update);
		beginPicker = (TimePicker) findViewById(R.id.starttimepicker_update);
		endPicker = (TimePicker) findViewById(R.id.endtimepicker_update);

		Calendar beginTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();
		beginTime.setTimeInMillis(begin);
		endTime.setTimeInMillis(end);

		year = beginTime.get(Calendar.YEAR);
		month = beginTime.get(Calendar.MONTH);
		dayOfMonth = beginTime.get(Calendar.DAY_OF_MONTH);
		start_hour = beginTime.get(Calendar.HOUR_OF_DAY);
		start_minute = beginTime.get(Calendar.MINUTE);
		end_hour = endTime.get(Calendar.HOUR_OF_DAY);
		end_minute = endTime.get(Calendar.MINUTE);
		System.out.println("year " + year + " month " + month + " day "
				+ dayOfMonth);
		titleview.setText(title);
		descriptionview.setText(description);
		dateview.setText((month + 1) + "/" + dayOfMonth + "/" + year);
		beginPicker.setCurrentHour(start_hour);
		beginPicker.setCurrentMinute(start_minute);
		endPicker.setCurrentHour(end_hour);
		endPicker.setCurrentMinute(end_minute);
	}

	public void updateEvent(View view) {
		Log.v("updateEvent", "update button pressed");

		titleview = (TextView) findViewById(R.id.event_title_edittext_update);
		descriptionview = (TextView) findViewById(R.id.event_description_edittext_update);
		dateview = (TextView) findViewById(R.id.event_title_textview_update);
		beginPicker = (TimePicker) findViewById(R.id.starttimepicker_update);
		endPicker = (TimePicker) findViewById(R.id.endtimepicker_update);

		int starthour = beginPicker.getCurrentHour();
		System.out.println("update event update method start_hour is "
				+ start_hour);
		int startminute = beginPicker.getCurrentMinute();
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(year, month, dayOfMonth, starthour, startminute, 0);
		// System.out.println("in updateevent, start variables, year, month,day are "
		// + year + month+dayOfMonth+starthour + startminute);
		long startMillis = beginTime.getTimeInMillis();

		int endhour = endPicker.getCurrentHour();
		int endminute = endPicker.getCurrentMinute();
		Calendar endTime = Calendar.getInstance();
		endTime.set(year, month, dayOfMonth, endhour, endminute, 0);
		// System.out.println("end variables are, year, month, day, hour, minute "
		// + year + month+ dayOfMonth + endhour + endminute);
		long endMillis = endTime.getTimeInMillis();

		ContentValues values = new ContentValues();
		values.put(Events.CALENDAR_ID, MainActivity.CalID);
		values.put(Events.TITLE, titleview.getText().toString());
		values.put(Events.DESCRIPTION, descriptionview.getText().toString());
		values.put(Events.DTSTART, startMillis);
		values.put(Events.DTEND, endMillis);
		values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().toString());

		
		Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, id);
		System.out.println("event id in update method is " + id);

		int rows = getContentResolver().update(uri, values, null, null);
		//Uri urit = getContentResolver().insert(uri, values);
		System.out.println(rows + " number of rows updated");

	}

	public void deleteEvent(View view) {

		Uri eventsUri = Events.CONTENT_URI;
		Uri uri = ContentUris.withAppendedId(eventsUri, id);
		System.out.println("event id in delete method is " + id);

		int rows = getContentResolver().delete(uri, null, null);
		System.out.println(rows + " row deleted");

		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_event, menu);
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
