/**
 * 
 */
package models;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import util.EmptyContactException;

/**
 * @author Dave
 *
 */
public class PastMeetingTest {

    private Set<Contact> testContactList;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
        Contact contactA = new ContactImpl("David North", 0);
        Contact contactB = new ContactImpl("Ian James", 0);
		testContactList = new HashSet<Contact>();
		testContactList.add(contactA);
		testContactList.add(contactB);
		
	
	}

	@Test
	public void testGetEmptyNotes() throws EmptyContactException {
		PastMeeting pastMeeting = new PastMeetingImpl(testContactList, Calendar.getInstance(), 0, "");
		assertEquals(pastMeeting.getNotes(), "");	
	}

	@Test
	public void testGetNullNotes() throws EmptyContactException {
		PastMeeting pastMeeting = new PastMeetingImpl(testContactList, Calendar.getInstance(), 0, null);
		assertEquals(pastMeeting.getNotes(), "");	
	}
	
	@Test
	public void testGetSomeNotes() throws EmptyContactException {
		PastMeeting pastMeeting = new PastMeetingImpl(testContactList, Calendar.getInstance(), 0, "Some");
		assertEquals(pastMeeting.getNotes(), "Some");	
	}
	

}
