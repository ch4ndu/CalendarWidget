package com.udn4hc.calendarwidget;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class UpdateEvent extends Activity {
	static String title, description;
	static long begin, end, id, calid;
	static int year, dayOfMonth, month, start_hour, start_minute, end_hour,
			end_minute;
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
		calid = intent.getLongExtra("calid", 0);
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

		Calendar beginTime = new GregorianCalendar();
		Calendar endTime = new GregorianCalendar();
		beginTime.setTimeInMillis(begin);
		endTime.setTimeInMillis(end);

		year = beginTime.get(Calendar.YEAR);
		month = beginTime.get(Calendar.MONTH);
		dayOfMonth = beginTime.get(Calendar.DAY_OF_MONTH);
		start_hour = beginTime.get(Calendar.HOUR_OF_DAY);
		start_minute = beginTime.get(Calendar.MINUTE);
		end_hour = endTime.get(Calendar.HOUR_OF_DAY);
		end_minute = endTime.get(Calendar.MINUTE);
		titleview.setText(title);
		descriptionview.setText(description);
		dateview.setText((month + 1) + "/" + dayOfMonth + "/" + year);
		beginPicker.setCurrentHour(start_hour);
		beginPicker.setCurrentMinute(start_minute);
		endPicker.setCurrentHour(end_hour);
		endPicker.setCurrentMinute(end_minute);
	}

	public void updateEvent(View view) {

		titleview = (TextView) findViewById(R.id.event_title_edittext_update);
		descriptionview = (TextView) findViewById(R.id.event_description_edittext_update);
		dateview = (TextView) findViewById(R.id.event_title_textview_update);
		beginPicker = (TimePicker) findViewById(R.id.starttimepicker_update);
		endPicker = (TimePicker) findViewById(R.id.endtimepicker_update);

		int starthour = beginPicker.getCurrentHour();
		int startminute = beginPicker.getCurrentMinute();
		Calendar beginTime = new GregorianCalendar();
		beginTime.set(year, month, dayOfMonth, starthour, startminute, 0);
		long startMillis = beginTime.getTimeInMillis();

		int endhour = endPicker.getCurrentHour();
		int endminute = endPicker.getCurrentMinute();
		Calendar endTime = new GregorianCalendar();
		endTime.set(year, month, dayOfMonth, endhour, endminute, 0);
		long endMillis = endTime.getTimeInMillis();

		ContentValues values = new ContentValues();
		values.put(Events.CALENDAR_ID, MainActivity.CalID);
		values.put(Events.TITLE, titleview.getText().toString());
		values.put(Events.DESCRIPTION, descriptionview.getText().toString());
		values.put(Events.DTSTART, startMillis);
		values.put(Events.DTEND, endMillis);
		values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

		Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, id);

		int rows = getContentResolver().update(uri, values, null, null);
		// AsyncHelper updateHelper = new AsyncHelper(getContentResolver());
		// updateHelper.startUpdate(2, null, uri, values, null, null);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		Toast.makeText(getBaseContext(), "Event successfully updated! Please Wait",
				Toast.LENGTH_LONG).show();

		Intent intent = new Intent(getApplicationContext(), GetEvents.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("year", year);
		intent.putExtra("month", month);
		intent.putExtra("day", dayOfMonth);
		intent.putExtra("calid", calid);
		startActivity(intent);

	}

	public void deleteEvent(View view) {

		Uri eventsUri = Events.CONTENT_URI;
		Uri uri = ContentUris.withAppendedId(eventsUri, id);

		int rows = getContentResolver().delete(uri, null, null);
		Toast.makeText(getBaseContext(), "Event successfully deleted",
				Toast.LENGTH_LONG).show();

		Intent intent = new Intent(getApplicationContext(), GetEvents.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("year", year);
		intent.putExtra("month", month);
		intent.putExtra("day", dayOfMonth);
		intent.putExtra("calid", calid);
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
