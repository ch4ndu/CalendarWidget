package com.udn4hc.calendarwidget;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

public class AsyncHelper extends AsyncQueryHandler {

	public AsyncHelper(ContentResolver cr) {
		super(cr);
		// TODO Auto-generated constructor stub
	}

	public void onInsertComplete(int token, Object cookie, Uri uri) {
		
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		//long eventID = Long.parseLong(uri.getLastPathSegment());

	}

	@Override
	protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
		GetEvents.list.clear();

		try {

			while (cursor.moveToNext()) {
				String description = null;
				String title = null;
				long id = 0;
				long begin = 0;
				long end = 0;

				id = Long.parseLong(cursor.getString(0).trim());
				title = cursor.getString(1);
				description = cursor.getString(2);
				begin = Long.parseLong(cursor.getString(3).trim());
				end = Long.parseLong(cursor.getString(4).trim());
			    //System.out.println("in asynchelper, eventid is " + id);
				Event event = new Event(title, description, id, begin, end);
				GetEvents.list.add(event);
			}

		} finally {
			cursor.close();

		}

	}

}
