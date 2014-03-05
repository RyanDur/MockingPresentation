/**
 *
 */
package controllers;

import models.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import util.*;

import java.io.File;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test suite uses the MockIdgenerator.
 * Test names generally speak for themselves, a few have a couple notes just to clarify what is being tested.
 *
 * @author Dave
 */
@SuppressWarnings("SpellCheckingInspection")
public class ContactManagerTest {

    String name = "David North";
    String notes = "Some notes";
    Calendar date;
    ContactManager cm;
    DataManager dm;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        IdGenerator idgen = new MockIdGeneratorImpl();

        dm = new DataManagerImpl();
        cm = new ContactManagerImpl(idgen, dm);
        date = Calendar.getInstance();
    }

    @Test
    public void addNewContact() {

        cm.addNewContact(name, notes);
        Set<Contact> contactSet = cm.getContacts(name);

        Contact[] contacts = contactSet.toArray(new Contact[contactSet.size()]);
        Contact contact = contacts[0];

        assertEquals(contact.getName(), name);
        assertEquals(contact.getNotes(), notes);
    }

    @Test
    public void addNewContactwithNullNameException() throws NullPointerException {
        thrown.expect(NullPointerException.class);
        cm.addNewContact(null, notes);
    }

    @Test
    public void addNewContactwithNullNotesException() throws NullPointerException {
        thrown.expect(NullPointerException.class);
        cm.addNewContact(name, null);
    }

    @Test(expected = NullPointerException.class)
    public void getContactswithNullNameException() throws NullPointerException {
        String name = null;
        cm.getContacts(name);
    }

    @Test
    public void getContactsWithSameName() {
        cm.addNewContact("Jim", notes);
        cm.addNewContact("Jim", notes);

        Set<Contact> sameNameSet = cm.getContacts("Jim");
        assertTrue(sameNameSet.size() == 2);
    }

    @Test
    public void getContactsViaId() {

        cm.addNewContact("Jim", notes);
        cm.addNewContact("Jim", notes);
        Contact[] sameNameSet = cm.getContacts(0, 1).toArray(new Contact[1]);
        assertTrue(sameNameSet[0].getName().equals("Jim"));
        assertTrue(sameNameSet[1].getName().equals("Jim"));
    }

    @Test
    public void getContactsWithUnknownIdException() throws IllegalArgumentException {
        thrown.expect(IllegalArgumentException.class);

        cm.addNewContact("Jim", notes);
        cm.addNewContact("Jim", notes);
        cm.getContacts(0, 1, 2);
    }

    /*
     * tests addFutureMeeting() and getFutureMeeting()
     */
    @Test
    public void addFutureMeeting() throws EmptyContactException {

        Calendar future = Calendar.getInstance();
        future.set(2015, 1, 1);

        cm.addNewContact("Jim", "notes"); // Started using the cm methods so exceptions are handled externally to the test
        cm.addNewContact("Jim", "Some notes");

        Set<Contact> contacts = cm.getContacts("Jim");

        int id = cm.addFutureMeeting(contacts, future);
        FutureMeeting fMeeting = cm.getFutureMeeting(id);

        assertEquals(id, fMeeting.getId());
        assertEquals(contacts, fMeeting.getContacts());
    }

    @Test
    public void addfutureMeetingInPast() throws IllegalArgumentException, InterruptedException {
        thrown.expect(IllegalArgumentException.class);
        Calendar date2 = new GregorianCalendar();

        cm.addNewContact("Jim", "notes");
        cm.addNewContact("Jim", "Some notes");

        Set<Contact> contacts = cm.getContacts("Jim");

        int id = cm.addFutureMeeting(contacts, date2);
        Thread.sleep(100);
        cm.getFutureMeeting(id);
    }

    /*
     * tests the getPastMeetingList() method
     *
     */
    @Test
    public void getPastMeetingList() throws EmptyContactException {
        Calendar past = new GregorianCalendar();
        past.add(Calendar.DATE, -1);

        Calendar future = new GregorianCalendar();
        future.add(Calendar.DATE, +1);

        cm.addNewContact("Jim", "notes");
        Set<Contact> contacts = cm.getContacts("Jim");
        Contact contactjim = contacts.toArray(new Contact[contacts.size()])[0];

        Set<Contact> testList = new HashSet<>();
        testList.add(contactjim);

        cm.addNewPastMeeting(testList, past, notes);

        cm.addFutureMeeting(testList, future);

        List<PastMeeting> returnedPmList = cm.getPastMeetingList(contactjim);
        Set<Contact> actual = returnedPmList.get(0).getContacts();

        assertEquals(actual, testList);
    }

    /*
     * tests addNewPastMeeting()
     */
    @Test
    public void addNewPastMeetingViaId() {

        Calendar date4 = new GregorianCalendar();
        date4.add(Calendar.DATE, -1);
        cm.addNewContact(name, notes);
        Set<Contact> contacts = cm.getContacts(name);
        Contact contact = contacts.toArray(new Contact[contacts.size()])[0];

        cm.addNewPastMeeting(contacts, date4, notes);
        PastMeeting ml = cm.getPastMeetingList(contact).get(0);

        assertEquals(ml.getContacts(), contacts);
    }

    /*
     * tests the addNewPastMeetingViaId IllegalArgumentException
     * if contacts parameter is empty or contact does not exist in database
     */
    @Test
    public void addNewPastMeetingIllegalArgumentException() {
        thrown.expect(IllegalArgumentException.class);

        Set<Contact> contacts = new HashSet<>();
        //Contact jonny = new ContactImpl("jonny", 0);
        //contacts.add(jonny);

        cm.addNewPastMeeting(contacts, date, notes);
    }

    /*
     * tests the NullPointerException on addNewPastMeeting
     */
    @Test
    public void addNewPastMeetingNullPointerException() {
        thrown.expect(NullPointerException.class);
        cm.addNewContact("jim", notes);
        Set<Contact> contacts = cm.getContacts("jim");
        cm.addNewPastMeeting(contacts, null, notes);
    }

    /*
     * tests getMeeting() works
     */
    @Test
    public void testGetMeeting() throws EmptyContactException {

        Calendar dateCalendar = new GregorianCalendar();


        cm.addNewContact("Jim", "notes");
        cm.addNewContact("Jim", "Some notes");

        Set<Contact> contacts = cm.getContacts("Jim");

        int id = cm.addFutureMeeting(contacts, dateCalendar);
        Meeting meeting = cm.getMeeting(id);

        assertEquals(id, meeting.getId());
        assertEquals(contacts, meeting.getContacts());
    }

    /*
     * Tests if addMeetingNotes() works
     */
    @Test
    public void testAddMeetingNotes() {
        cm.addNewContact("Jim", "notes");
        cm.addNewContact("Jim", "Some notes");

        Set<Contact> contacts = cm.getContacts("Jim");

        date.set(2040, 1, 1);
        int id = cm.addFutureMeeting(contacts, date);
        FutureMeeting fm = cm.getFutureMeeting(id);

        cm.addMeetingNotes(id, "Add some notes");
        Meeting pm = cm.getMeeting(id);

        assertEquals(fm.getContacts(), pm.getContacts());
        assertEquals(fm.getDate(), pm.getDate());
        assertEquals(fm.getId(), pm.getId());
    }

    @Test
    public void addMeetingNotesIllegalArgumentException() {
        thrown.expect(IllegalArgumentException.class);
        cm.addMeetingNotes(0, notes);
    }

    @Test(expected = NullPointerException.class)
    public void addMeetingNotesNullPointerException() {
        cm.addMeetingNotes(0, null);

    }

    @Test
    public void addMeetingNotesIllegalStateException() {
        thrown.expect(IllegalArgumentException.class);

        Calendar date = new GregorianCalendar();
        date.add(Calendar.DATE, -1);

        cm.addNewContact("Jim", "notes");
        cm.addNewContact("Jim", "Some notes");

        Set<Contact> contacts = cm.getContacts("Jim");


        int id = cm.addFutureMeeting(contacts, date);
        cm.addMeetingNotes(id, notes);

    }

    /*
     * past meeting exception test
     */
    @Test
    public void getPastMeetingExceptionTest() throws IllegalArgumentException {
        thrown.expect(IllegalArgumentException.class);

        cm.addNewContact("Jim", "notes");
        cm.addNewContact("Jim", "Some notes");

        Set<Contact> contacts = cm.getContacts("Jim");

        Calendar future = Calendar.getInstance();
        future.set(3000, 1, 1);

        int id = cm.addFutureMeeting(contacts, future);

        cm.getPastMeeting(id);
    }

    /*
     * tests the meeting meetSort() method
     */
    @Test
    public void meetSortTest() throws EmptyContactException {

        Calendar pastest = new GregorianCalendar();
        pastest.add(Calendar.DATE, -2);

        Calendar past = new GregorianCalendar();
        past.add(Calendar.DATE, -1);

        Calendar future = new GregorianCalendar();
        future.add(Calendar.DATE, +1);


        Calendar futurest = new GregorianCalendar();
        futurest.add(Calendar.DATE, +2);

        cm.addNewContact("Jim", "notes");
        Set<Contact> contacts = cm.getContacts("Jim");

        Contact contactjim = contacts.toArray(new Contact[contacts.size()])[0];

        cm.addNewPastMeeting(contacts, future, "future");
        cm.addNewPastMeeting(contacts, past, "past");
        cm.addNewPastMeeting(contacts, futurest, "futurest");
        cm.addNewPastMeeting(contacts, pastest, "pastest");

        //create a sorted PastMeeting list
        List<Meeting> meetingsOrdered = new ArrayList<>();
//        PastMeeting meet1 = new PastMeetingImpl(contacts, pastest, 1, "pastest");
//        PastMeeting meet2 = new PastMeetingImpl(contacts, past, 2, "past");
//        PastMeeting meet3 = new PastMeetingImpl(contacts, future, 3, "future");
//        PastMeeting meet4 = new PastMeetingImpl(contacts, futurest, 4, "futurest");

//        meetingsOrdered.add(meet1);
//        meetingsOrdered.add(meet2);
//        meetingsOrdered.add(meet3);
//        meetingsOrdered.add(meet4);

        List<PastMeeting> pastMeetingList = cm.getPastMeetingList(contactjim);
        PastMeeting actual = pastMeetingList.toArray(new PastMeeting[pastMeetingList.size()])[0];
        PastMeeting control = meetingsOrdered.toArray(new PastMeeting[meetingsOrdered.size()])[0];

        assertTrue(cm.getPastMeetingList(contactjim).size() == 2);
        assertEquals(control.getNotes(), actual.getNotes());
        assertTrue(actual.getNotes().equals("pastest"));

    }

    @Test
    public void getPastMeetingListException() throws IllegalArgumentException {
        thrown.expect(IllegalArgumentException.class);

//        Contact bob = new ContactImpl("Bob", 0);
//        cm.getPastMeetingList(bob);
    }

    @Test
    public void getFutureMeetingList() {

        Calendar past = new GregorianCalendar();
        past.add(Calendar.DATE, -1);

        Calendar future = new GregorianCalendar();
        future.add(Calendar.DATE, +1);

        Calendar futurest = new GregorianCalendar();
        futurest.add(Calendar.DATE, +2);

        cm.addNewContact("Jim", "notes");
        Set<Contact> contacts = cm.getContacts("Jim");

        cm.addNewPastMeeting(contacts, past, "some notes");
        cm.addFutureMeeting(contacts, futurest);
        cm.addFutureMeeting(contacts, future);

        Contact contactjim = contacts.toArray(new Contact[contacts.size()])[0];
        assertTrue(cm.getFutureMeetingList(contactjim).size() == 2);
    }

    @Test
    public void getFutureMeetingListException() throws IllegalArgumentException {
        thrown.expect(IllegalArgumentException.class);

//        Contact bob = new ContactImpl("Bob", 0);
//        cm.getFutureMeetingList(bob);
    }

    @Test
    public void getFutureMeetingListByDate() {

        Calendar past = new GregorianCalendar();
        past.add(Calendar.DATE, -1);

        Calendar future = new GregorianCalendar();
        future.add(Calendar.DATE, +10);

        Calendar futurest = new GregorianCalendar();
        futurest.add(Calendar.DATE, +20);

        cm.addNewContact("Jim", "notes");
        Set<Contact> contacts = cm.getContacts("Jim");

        cm.addNewPastMeeting(contacts, past, "some notes");
        cm.addFutureMeeting(contacts, future);
        cm.addFutureMeeting(contacts, futurest);
        cm.addFutureMeeting(contacts, future);

        assertTrue(cm.getFutureMeetingList(future).size() == 2);
    }

    @Test
    public void getMeetingisnullifempty() {
        assertTrue(cm.getMeeting(0) == null);
    }
}