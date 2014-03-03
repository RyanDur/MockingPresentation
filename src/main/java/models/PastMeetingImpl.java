/**
 * 
 */
package models;

import util.EmptyContactException;

import java.util.Calendar;
import java.util.Set;

/**
 * @author Dave
 *
 */
public class PastMeetingImpl extends MeetingImpl implements PastMeeting {

	private static final long serialVersionUID = -7254946864044167120L;
	private String notes = "";
	
	public PastMeetingImpl(Set<Contact> contacts, Calendar date, int id, String notes) throws EmptyContactException {
		super(contacts, date, id);
		if (notes != null) {
			this.notes = notes;
		}
	}
	
	@Override
	public String getNotes() {
		return notes;
	}
}