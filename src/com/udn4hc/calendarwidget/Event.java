package com.udn4hc.calendarwidget;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Event implements Serializable {
	
	public Event() {
		super();
		// TODO Auto-generated constructor stub
	}
	String Title;
	String Description;
	long ID;
	long begin;
	long end;
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public long getBeginMilli() {
		return begin;
	}
	public long getEndMilli() {
		return end;
	}
	public String getBegin() {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(this.begin);
		return formatter.format(cal.getTime());
	}
	public void setBegin(long begin) {
		this.begin = begin;
	}
	public String getEnd() {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(this.end);
		return formatter.format(cal.getTime());
	}
	public void setEnd(long end) {
		this.end = end;
	}
	public Event(String title, String description, long iD, long begin,
			long end) {
		super();
		Title = title;
		Description = description;
		ID = iD;
		this.begin = begin;
		this.end = end;
	}
	

}
