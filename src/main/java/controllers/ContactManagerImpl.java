/**
 *
 */
package controllers;

import models.*;
import util.DataManager;
import util.EmptyContactException;
import util.IdGenerator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author Dave
 */
public class ContactManagerImpl implements ContactManager {

    private Map<String, Set<Contact>> contactMap;
    private Map<Integer, Contact> idMap; // master contact list
    private Map<Integer, Meeting> meetingMap; //Meeting list data structure
    private IdGenerator idGenerator;
    private DataManager dataManager;

    /**
     * The constructor requires an object of IdGenerator and DataManager in order to be created.
     *
     * @param idgen id generator
     * @param dm data manager
     */
    @SuppressWarnings("unchecked")
    public ContactManagerImpl(IdGenerator idgen, DataManager dm) {

        dataManager = dm;

        if (dm.dataFileExists()) {
            Object[] setUp = dm.loadData();
            contactMap = (Map<String, Set<Contact>>) setUp[0];
            idMap = (Map<Integer, Contact>) setUp[1];
            meetingMap = (Map<Integer, Meeting>) setUp[2];
            idGenerator = (IdGenerator) setUp[3];

        } else {
            contactMap = new HashMap<>();
            idMap = new HashMap<>();
            meetingMap = new HashMap<>();
            idGenerator = idgen;
        }
        Runtime.getRuntime().addShutdownHook(saveOnExit());
    }

    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) throws IllegalArgumentException {

        Calendar now = Calendar.getInstance();

        if (date.before(now)) {
            throw new IllegalArgumentException();
        }

        FutureMeeting meeting;
        try {
            meeting = new FutureMeetingImpl(contacts, date, idGenerator.getNewId());
            meetingMap.put(meeting.getId(), meeting);
            return meeting.getId();
        } catch (EmptyContactException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public PastMeeting getPastMeeting(int id) throws IllegalArgumentException {

        Calendar now = Calendar.getInstance();
        Meeting pm = meetingMap.get(id);

        if (pm.getDate().after(now)) {
            throw new IllegalArgumentException();
        }
        return (PastMeeting) pm;
    }

    @Override
    public FutureMeeting getFutureMeeting(int id) throws IllegalArgumentException {

        Calendar now = Calendar.getInstance();

        if (getMeeting(id).getDate().before(now)) {
            throw new IllegalArgumentException();
        }
        return (FutureMeeting) meetingMap.get(id);
    }

    @Override
    public Meeting getMeeting(int id) {
        return meetingMap.get(id);
    }

    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) throws IllegalArgumentException {

        Calendar now;
        List<Meeting> result = new ArrayList<>();
        List<FutureMeeting> futureMeetings = new ArrayList<>();

        //checks contacts exist
        HashSet<Contact> contacts = new HashSet<>();
        contacts.add(contact);
        if (!contactExists(contacts)) {
            throw new IllegalArgumentException();
        }

        //creates a list of all the meetings after now
        now = Calendar.getInstance();
        for (Meeting m : meetingMap.values()) {
            if (m.getDate().after(now)) {
                futureMeetings.add((FutureMeeting) m);
            }
        }
        //if the contact is within the meeting, that meeting is added to the result
        for (Meeting m2 : futureMeetings) {
            for (Contact c : m2.getContacts()) {
                if (c.equals(contact)) {
                    result.add(m2);
                }
            }
        }
        Collections.sort(result, new MeetSort());
        return result;
    }

    /**
     * Used for checking whether two meetings happen on the same day but disregards the time of day
     *
     * @param date1 of day one to be compared
     * @param date2 of day two to be compared
     * @return boolean true if both dates are on the same day
     */
    private boolean sameDay(Calendar date1, Calendar date2) {

        return date1.get(Calendar.DAY_OF_YEAR) == date2.get(Calendar.DAY_OF_YEAR) && date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH) && date1.get(Calendar.DATE) == date2.get(Calendar.DATE);
    }

    @Override
    public List<Meeting> getFutureMeetingList(Calendar date) {

        List<Meeting> result = new ArrayList<>();

        for (Meeting m : meetingMap.values()) {
            if (sameDay(m.getDate(), date)) {
                result.add(m);
            }
        }
        Collections.sort(result, new MeetSort());
        return result;
    }

    /**
     * Comparator for organizing a Collection of meetings into Chronological order
     */
    private class MeetSort implements Comparator<Meeting> {

        @Override
        public int compare(Meeting a, Meeting b) {
            if (a.getDate().before(b.getDate())) {
                return -1;
            } else if (a.getDate().after(b.getDate())) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    @Override
    public List<PastMeeting> getPastMeetingList(Contact contact) throws IllegalArgumentException {

        Calendar now;
        new GregorianCalendar();
        Set<PastMeeting> result = new HashSet<>();
        List<PastMeeting> resultReturn = new ArrayList<>();
        List<PastMeeting> pastMeetings = new ArrayList<>();

        HashSet<Contact> contacts = new HashSet<>();
        contacts.add(contact);
        if (!contactExists(contacts)) {
            throw new IllegalArgumentException();
        }

        //creates a list of meetings before now
        now = Calendar.getInstance();
        for (Meeting m : meetingMap.values()) {
            if (m.getDate().before(now)) {
                pastMeetings.add((PastMeeting) m);
            }

            //if the contact is a contact within these new meetings, add it to the result set
            for (Meeting m2 : pastMeetings)
                for (Contact c : m2.getContacts()) {
                    if (c.equals(contact)) {
                        result.add((PastMeeting) m2);
                    }
                }
        }
        resultReturn.addAll(result);
        Collections.sort(resultReturn, new MeetSort());
        return resultReturn;
    }

    @Override
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) throws IllegalArgumentException, NullPointerException {

        if (contacts == null || date == null || text == null) {
            throw new NullPointerException();
        }

        if (!contactExists(contacts) || contacts.size() < 1) {
            throw new IllegalArgumentException();
        }

        Meeting pastMeeting;
        try {
            pastMeeting = new PastMeetingImpl(contacts, date, idGenerator.getNewId(), text);
            meetingMap.put(pastMeeting.getId(), pastMeeting);
        } catch (EmptyContactException e) {
            e.printStackTrace();
        }
    }

    /**
     * This Method checks if a contact exists within the contacts container
     *
     * @param contacts the contacts
     * @return boolean true if the contact exists within the contacts data structure
     */
    private boolean contactExists(Set<Contact> contacts) {
        boolean contactsExist = false;
        for (Contact contact : contacts) {
            if (idMap.containsValue(contact)) {
                contactsExist = true;
            }
        }
        return contactsExist;
    }

    @Override
    public void addMeetingNotes(int id, String text) throws IllegalArgumentException, NullPointerException, IllegalStateException {

        if (text == null) {
            throw new NullPointerException();
        }

        Calendar now = Calendar.getInstance();
        FutureMeeting fm = (FutureMeeting) meetingMap.get(id);

        if (fm == null) {
            throw new IllegalArgumentException();
        }

        if (fm.getDate().before(now)) {
            throw new IllegalStateException();
        }

        PastMeeting pm = null;

        try {
            pm = new PastMeetingImpl(fm.getContacts(), fm.getDate(), id, text);
        } catch (EmptyContactException e) {
            e.printStackTrace();
        }
        meetingMap.put(id, pm);
    }

    @Override
    public void addNewContact(String name, String notes) throws NullPointerException {

        if (name == null || notes == null) {
            throw new NullPointerException();
        }

        Contact contact = new ContactImpl(name, idGenerator.getNewId());
        contact.addNotes(notes);

        idMap.put(contact.getId(), contact);

        if (contactMap.get(name) == null) {
            Set<Contact> contacts = new HashSet<>();
            contacts.add(contact);
            contactMap.put(name, contacts);
        } else {
            Set<Contact> contacts = contactMap.get(name);// create variable of the current Contact Set that exists within the map in order to point at it on the next line
            contacts.add(contact);
        }
    }

    @Override
    public Set<Contact> getContacts(int... ids) throws IllegalArgumentException {

        Set<Contact> contacts = new HashSet<>();
        for (int id : ids) {
            if (idMap.get(id) == null) {
                throw new IllegalArgumentException();
            }
            contacts.add(idMap.get(id));
        }
        return contacts;
    }

    @Override
    public Set<Contact> getContacts(String name) throws NullPointerException {

        if (name == null) {
            throw new NullPointerException();
        }
        return contactMap.get(name);
    }

    @Override
    public void flush() {
        dataManager.saveData(contactMap, idMap, meetingMap, idGenerator);
    }


    /**
     * Saves the state of the contact containers, the idgenerator and the meeting lists on exit
     *
     * @return a thread for the shutdown process to carry out
     */
    private Thread saveOnExit() {

        return new Thread(new Runnable() {

            @Override
            public void run() {
                flush();
            }
        });
    }
}