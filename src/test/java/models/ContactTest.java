/**
 * 
 */
package models;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


/**
 * @author Dave
 *
 */
public class ContactTest {

	ContactImpl contactA;
	
	
	@Before
	public void setUp(){
		contactA = new ContactImpl("David North", 0);
	}
	
	
	/**
	 * Test method for {@link ContactImpl#getId()}.
	 */
	@Test
	public void testGetId() {
		assertEquals(contactA.getId(), 0);
	}

	/**
	 * Test method for {@link ContactImpl#getName()}.
	 */
	@Test
	public void testGetName() {		
		assertEquals("Name not found", contactA.getName(), "David North");
	}

	/**
	 * Test method for {@link ContactImpl#getNotes()}.
	 */
	@Test
	public void testGetEmptyNotes() {
		assertEquals(contactA.getNotes(), "");
	}

	/**
	 * Test method for {@link ContactImpl#addNotes(java.lang.String)}.
	 */
	@Test
	public void testAddNotes() {
		contactA.addNotes("New note");
		assertEquals(contactA.getNotes(), "New note");
		contactA.addNotes("; Another");
		assertEquals(contactA.getNotes(), "New note; Another");
	}

}
