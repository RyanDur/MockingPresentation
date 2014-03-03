/**
 * 
 */
package util;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;

import controllers.ContactManager;
import controllers.ContactManagerImpl;
import models.Contact;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Dave
 *
 */
public class DataManagerTests {
	
	ContactManager cm;
	DataManager dm;


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	
		IdGenerator idgen = new MockIdGeneratorImpl();
		
		dm = new DataManagerImpl();
		cm = new ContactManagerImpl(idgen, dm);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		
		if (new File("contacts.txt").exists()) {
			new File("contacts.txt").deleteOnExit();
		}
		
		cm = null;
		dm = null;
	}

	/*
	 * Tests the using the contact manager
	 */
	@Test
	public void flushPersistanceTestSave() {
		
		Contact contact1;
		Contact contact2;
		
		//set up a date
		Calendar future = new GregorianCalendar();
		future.add(Calendar.DATE, +10);
		String name = "james";
		//set up some contacts and create a set of them
		cm.addNewContact(name, "notes about Ian.");
		cm.addNewContact("Paul", "notes about Paul");
		Set<Contact> contacts = cm.getContacts(0);
		
		//create some meetings
		cm.addFutureMeeting(contacts, future);
		cm.addNewPastMeeting(contacts, future, "text");
		
		//first contact put in array
		Contact[] contactArray = contacts.toArray(new Contact[contacts.size()]);
		contact1 = contactArray[0];
//		System.out.println(contact1.getName() + contact1.getId());
		
		//save everything
		cm.flush();
		
		//close the cm
		cm = null;
		
		//set up and open a new cm
		IdGenerator idgen = new MockIdGeneratorImpl();
		ContactManager cm2 = new ContactManagerImpl(idgen, dm);
		
		
		//load the first contact from this new cm into contact2
		Set<Contact> contactSet = cm2.getContacts(name);
		Assert.assertNotNull(contactSet);
		Contact[] contactsLoaded = contactSet.toArray(new Contact[contactSet.size()]);
		contact2 = contactsLoaded[0];
//		System.out.println(contact2.getName() + contact2.getId());
		
		//check the first contact is the same as the previous one
		assertEquals(contact1.getName(), contact2.getName());
		assertEquals(contact1.getId(), contact2.getId());
				
	}	
}
