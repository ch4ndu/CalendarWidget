package com.udn4hc.calendarwidget;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	CalendarView cal;
	public static long CalID;
	public static final String[] EVENT_PROJECTION = new String[] {
			Calendars._ID, // 0
			Calendars.ACCOUNT_NAME, // 1
			Calendars.CALENDAR_DISPLAY_NAME, // 2
			Calendars.OWNER_ACCOUNT // 3
	};
	static int currentMonth;
	static int currentYear;
	static int currentDay;

	// The indices for the projection array above.
	public static final int PROJECTION_ID_INDEX = 0;
	public static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
	public static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
	public static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

	public static final String accountName = "Calendar_Widget";
	public static final String ownerAccount = "CalendarWidget";
	public static final String calendarName = "CalendarWidget";
	public static final String calendarDisplayname = "Calendar Widget";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("Calendar Widget");

		TextView tv = (TextView) findViewById(R.id.textView1);

		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
		String formattedDate = df.format(c.getTime());
		tv.setText(formattedDate);

		initCalendar();

		cal = (CalendarView) findViewById(R.id.calendarView1);
		cal.setOnDateChangeListener(new OnDateChangeListener() {

			@Override
			public void onSelectedDayChange(CalendarView view, int year,
					int month, int dayOfMonth) {
				// TODO Auto-generated method stub

				// Toast.makeText(
				// getBaseContext(),
				// "Selected Date is\n\n" + dayOfMonth + " : " + month
				// + " : " + year, Toast.LENGTH_LONG).show();

				currentYear = year;
				currentMonth = month;
				currentDay = dayOfMonth;

				TextView tv = (TextView) findViewById(R.id.textView1);
				tv.setText((month + 1) + "/" + dayOfMonth + "/" + year);

			}
		});

	}

	public void getAgenda(View view) {

		Intent intent = new Intent(getApplicationContext(), GetEvents.class);
		intent.putExtra("year", currentYear);
		intent.putExtra("month", currentMonth);
		intent.putExtra("day", currentDay);
		startActivity(intent);

	}

	public void initCalendar() {

		Cursor cur = null;
		ContentResolver cr = getContentResolver();
		Uri uri = Calendars.CONTENT_URI;
		String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
				+ Calendars.ACCOUNT_TYPE + " = ?) AND ("
				+ Calendars.OWNER_ACCOUNT + " = ?))";
		String[] selectionArgs = new String[] { accountName,
				CalendarContract.ACCOUNT_TYPE_LOCAL, ownerAccount };
		// Submit the query and get a Cursor object back.
		cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
		if (!cur.moveToNext()) {
			// create the calendar since it doesnt exist yet
			// System.out.println("cursor is null, so creating the calendar");
			Uri calUri = CalendarContract.Calendars.CONTENT_URI;
			ContentValues cv = new ContentValues();
			cv.put(CalendarContract.Calendars.ACCOUNT_NAME, accountName);
			cv.put(CalendarContract.Calendars.ACCOUNT_TYPE,
					CalendarContract.ACCOUNT_TYPE_LOCAL);
			cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, ownerAccount);
			cv.put(CalendarContract.Calendars.NAME, calendarName);
			cv.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
					calendarDisplayname);
			cv.put(CalendarContract.Calendars.CALENDAR_COLOR, "#33B5E5");
			cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
					CalendarContract.Calendars.CAL_ACCESS_OWNER);
			// cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, true);
			cv.put(CalendarContract.Calendars.VISIBLE, 1);
			cv.put(CalendarContract.Calendars.SYNC_EVENTS, 1);

			calUri = calUri
					.buildUpon()
					.appendQueryParameter(
							CalendarContract.CALLER_IS_SYNCADAPTER, "true")
					.appendQueryParameter(
							CalendarContract.Calendars.ACCOUNT_NAME,
							accountName)
					.appendQueryParameter(
							CalendarContract.Calendars.ACCOUNT_TYPE,
							CalendarContract.ACCOUNT_TYPE_LOCAL).build();
			uri = this.getContentResolver().insert(calUri, cv);
		}
		cur.close();

		// calendarUri = uri;

		cur = null;
		cr = getContentResolver();
		// Uri uri = Calendars.CONTENT_URI;
		selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
				+ Calendars.ACCOUNT_TYPE + " = ?) AND ("
				+ Calendars.OWNER_ACCOUNT + " = ?))";
		selectionArgs = new String[] { accountName,
				CalendarContract.ACCOUNT_TYPE_LOCAL, ownerAccount };
		// Submit the query and get a Cursor object back.

		try {

			cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs,
					null);

			while (cur.moveToNext()) {
				long calID = 0;
				String displayName = null;
				String accountName = null;
				String ownerName = null;

				// Get the field values
				calID = cur.getLong(PROJECTION_ID_INDEX);
				displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
				accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
				ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

				// Do something with the values...
				// System.out.println("reading the values");
				// Log.v("displayname", displayName);
				// Log.v("accountname", accountName);
				// Log.v("ownername", ownerName);
				// Log.v("calid", String.valueOf(calID));
				CalID = calID;
			}

		} finally {
			cur.close();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
