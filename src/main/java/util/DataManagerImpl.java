package util;


import models.Contact;
import models.Meeting;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
/**
 * 
 */
import java.util.Set;


/**
 * This implementation of DataManager is dependent on the objects within the ContactManager
 * being serializable.
 * 
 * 
 * @author Dave
 *
 */
public class DataManagerImpl implements DataManager {
	
	final String FILENAME = "contacts.txt";
	
	@Override
	public boolean dataFileExists() {		
		return new File(FILENAME).exists();
	} 
	
	@SuppressWarnings("unchecked")
	@Override
	public Object[] loadData() {
		Map<String, Set<Contact>> contactMap = null;
		Map<Integer, Contact> idMap = null;
		Map<Integer, Meeting> meetingMap = null;
		IdGenerator idGenerator = null;
		ObjectInputStream decode;
		try {
			decode = new ObjectInputStream(new BufferedInputStream(new FileInputStream(FILENAME)));
			contactMap = (Map<String, Set<Contact>>) decode.readObject();
			idMap = (Map<Integer, Contact>) decode.readObject();
			meetingMap = (Map<Integer, Meeting>) decode.readObject();
			idGenerator = (IdGenerator) decode.readObject();
			decode.close();	
		} catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new Object[]{contactMap, idMap, meetingMap, idGenerator};
	}
	
	@Override
	public void saveData(Map<String, Set<Contact>> contactMap, Map<Integer, Contact> idMap, Map<Integer, Meeting> meetingMap, IdGenerator idGenerator) {
		ObjectOutputStream encode;
		try {
			encode = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(FILENAME)));		
			encode.writeObject(contactMap);
			encode.writeObject(idMap);
			encode.writeObject(meetingMap);
			encode.writeObject(idGenerator);
			encode.close();
		} catch (IOException ex2) {
			ex2.printStackTrace();
		}
	}
}
