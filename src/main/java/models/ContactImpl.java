package models;

import java.io.Serializable;



/**
 * 
 * @author Dave
 *
 */
public class ContactImpl implements Contact, Serializable {

	private static final long serialVersionUID = -225015209537721411L;
	private int id;
	private String name;
	private String notes = "";
	
	public ContactImpl(String name, int iD){
		this.name = name;
		this.id = iD;
	}
	
	/*
	 * returns the Contacts Id
	 */
	@Override
	public int getId() {
		return id;
	}

	/*
	 * returns the Contacts name as a String
	 */
	@Override
	public String getName() {		
		return name;
	}

	/*
	 * returns the Contacts notes as a String
	 */
	@Override
	public String getNotes() {		
		return notes;
	}

	/*
	 * Used to add notes to the meeting in String format
	 */
	@Override
	public void addNotes(String note) {
		notes += note;
	}
		
	

}