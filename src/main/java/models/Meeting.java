package models;


import java.util.Calendar;
import java.util.Set;

/**
 * A class to represent meetings
 *
 * Meetings have unique IDs, scheduled date and a list of participating contacts
 */
public interface Meeting {
    /**
     * Returns the id of the meeting.
     *
     * @return the id of the meeting.
     */
    int getId();

    /**
     * Return the date of the meeting.
     *
     * @return the date of the meeting.
     */
    Calendar getDate();

    /**
     * Return the details of people that attended the meeting.
     *
     * The list contains a mininum of one contact (is there were
     * just two people: the user and the contact) and may contain an
     * arbitrary number of them.
     *
     * @return the details of people that attended the meeting.
     */
    Set<Contact> getContacts();
}
