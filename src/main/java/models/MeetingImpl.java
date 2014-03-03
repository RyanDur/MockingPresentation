/**
 * 
 */
package models;

import util.EmptyContactException;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;


/**
 * @author Dave
 *
 */
public class MeetingImpl implements Meeting, Serializable {
	
	private static final long serialVersionUID = -1922728117007903192L;
	private int id;
	private Calendar date;
	Set<Contact> contacts;
	
	/*
	 * meeting constructor used to create all meetings from scratch, Past and Future
	 */
	public MeetingImpl(Set<Contact> contacts, Calendar date, int iD) throws EmptyContactException {
		
		if(contacts == null || contacts.isEmpty()) {
			throw new EmptyContactException();
		}
		
		this.date = date;
		this.contacts = contacts;
		this.id = iD;
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public Calendar getDate() {
		return date;
	}

	@Override
	public Set<Contact> getContacts() {
		return contacts;
	}
}