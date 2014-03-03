/**
 * 
 */
package models;

import util.EmptyContactException;

import java.util.Calendar;
import java.util.Set;

public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {

	private static final long serialVersionUID = -6410188274623775033L;

	public FutureMeetingImpl(Set<Contact> contacts, Calendar date, int iD) throws EmptyContactException {
		super(contacts, date, iD);
	}

}
