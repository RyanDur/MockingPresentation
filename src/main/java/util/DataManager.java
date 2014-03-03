package util;

import models.Contact;
import models.Meeting;

import java.util.Map;
import java.util.Set;

/**
 * 
 * An interface to create DataManagers that save and load a file for the
 * ContactManagerImpl class 
 * @author Dave
 *
 */
public interface DataManager {
	
	/**
	 * determines whether a data file exists for the contactManager save data
	 * 
	 * @return boolean in respect to whether the data file already exists
	 */
	public abstract boolean dataFileExists();

	/**
	 * loads the fields of a given ContactManager into an array of type Object
	 * 
	 * @return Object[]
	 */
	public abstract Object[] loadData();

	/**
	 * takes the contactMap, idMap, meetingMap and IdGenerator from a given
	 * ContactManager and saves it out to a file.
	 * 
	 * @param Map<String, Set<Contact>>
	 * 
	 * @param Map<Integer, Contact>
	 * 
	 * @param Map<Integer, Meeting>
	 * 
	 * @param IdGenerator
	 */
	public abstract void saveData(Map<String, Set<Contact>> contactMap,
			Map<Integer, Contact> idMap, Map<Integer, Meeting> meetingMap,
			IdGenerator idGenerator);

}