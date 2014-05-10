package com.udn4hc.calendarwidget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GetEvents extends Activity {

	public static ArrayList<Event> list = new ArrayList<Event>();
	static int year, month, dayOfMonth;
	static long startMillis, endMillis;
	EventAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_events);


		Intent intent = getIntent();
		year = intent.getIntExtra("year", 0);
		month = intent.getIntExtra("month", 0);
		dayOfMonth = intent.getIntExtra("day", 0);

		setTitle((month + 1) + "/" + dayOfMonth + "/" + year);

		list = getEvents();

		ListView lv = (ListView) findViewById(R.id.list);
		adapter = new EventAdapter(getApplicationContext(), list);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Event event = list.get(position);
				Intent intent = new Intent(getApplicationContext(),
						UpdateEvent.class);
				intent.putExtra("event", (Serializable) event);
				startActivity(intent);
			}

		});

	}

	public ArrayList<Event> getEvents() {

		list.clear();

		Calendar beginTime = Calendar.getInstance();
		beginTime.set(year, month, dayOfMonth, 0, 0, 0);
		startMillis = beginTime.getTimeInMillis();

		Calendar endTime = Calendar.getInstance();
		endTime.set(year, month, dayOfMonth, 23, 59, 59);
		endMillis = endTime.getTimeInMillis();

		// ContentResolver cr = getContentResolver();

		Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI
				.buildUpon();

		ContentUris.appendId(eventsUriBuilder, startMillis);
		ContentUris.appendId(eventsUriBuilder, endMillis);

		// Uri eventUri = eventsUriBuilder.build();

		String testsel = "((" + CalendarContract.Instances.CALENDAR_ID
				+ " = ?)" + " AND (" + CalendarContract.Events.DTSTART
				+ " >= ?) AND (" + CalendarContract.Events.DTEND + " <= ?)"
				+ " AND ( deleted!=1 ))";

		// String selection = "((" + Instances.CALENDAR_ID + " = ?))";
		String[] selectionArgs = new String[] { "" + MainActivity.CalID,
				String.valueOf(startMillis), String.valueOf(endMillis) };

		String[] projection = new String[] { "_id", "title", "description",
				"dtstart", "dtend" };

		Uri testuri = Events.CONTENT_URI;
		Cursor eventCursor = getContentResolver().query(testuri, projection,
				testsel, selectionArgs,
				CalendarContract.Instances.DTSTART + " ASC");

		// Cursor eventCursor = cr.query(eventUri, projection, testsel,
		// selectionArgs, CalendarContract.Instances.DTSTART + " ASC");
		// Cursor eventCursor = null;

		try {

			// eventCursor = cr.query(eventUri, projection, null, null,
			// CalendarContract.Instances.DTSTART + " ASC");

			while (eventCursor.moveToNext()) {
				// System.out.println("something in cursor");
				String description = null;
				String title = null;
				long id = 0;
				long begin = 0;
				long end = 0;

				id = eventCursor.getLong(0);
				title = eventCursor.getString(1);
				description = eventCursor.getString(2);
				begin = eventCursor.getLong(3);
				end = eventCursor.getLong(4);
				// System.out.println("in getEvenets, eventid is " + id);
				Event event = new Event(title, description, id, begin, end);
				list.add(event);
			}

		} finally {
			eventCursor.close();

		}

		return list;

	}

	@Override
	public void onResume() {
		super.onResume();
		list = getEvents();
		adapter.notifyDataSetChanged();

	}

	private static class ViewHolder {
		TextView TitleHolder;
		TextView TimeHolder;

	}

	private class EventAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<Event> events = new ArrayList<Event>();

		EventAdapter(Context context, ArrayList<Event> events) {
			this.context = context;
			this.events = events;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return events.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return events.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ViewHolder holder;
			if (convertView == null) {

				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.event_row, null);
				holder.TitleHolder = (TextView) convertView
						.findViewById(R.id.event_row_title);
				holder.TimeHolder = (TextView) convertView
						.findViewById(R.id.event_row_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.TitleHolder.setText(events.get(position).getTitle());
			holder.TimeHolder.setText("From " + events.get(position).getBegin()
					+ " To " + events.get(position).getEnd());

			return convertView;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.get_events, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.AddEvent) {

			Intent intent = new Intent(getApplicationContext(),
					CreateEvent.class);
			intent.putExtra("dayofmonth", dayOfMonth);
			intent.putExtra("month", month);
			intent.putExtra("year", year);
			startActivity(intent);

		}
		return super.onOptionsItemSelected(item);
	}

}
