/**
 * 
 */
package util;

/**
 * @author Dave
 *
 */
public class EmptyContactException extends Exception {
	
	/**
	 * Exception that is thrown if a set of contacts is empty.
	 */
	private static final long serialVersionUID = 1L;

	public EmptyContactException() {
		super("The Contact list passed in was empty/null. Meeting not created");
	}
	
}
