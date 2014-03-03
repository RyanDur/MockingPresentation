package util;

/**
 * IdGenerator classes are used to create an Integer ID for any Contact or Meeting
 * @author Dave
 *
 */
public interface IdGenerator {
	
	/**
	 * Creates a unique Integer every time it is called
	 * @return a unique Integer
	 */
	public abstract int getNewId();

}