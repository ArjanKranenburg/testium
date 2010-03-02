/**
 * 
 */
package org.testium.executor;

/**
 * @author Arjan Kranenburg
 *
 */
public class InvalidTestTypeException extends Exception
{
	private String myType;
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4839779068555699628L;

	/**
	 * @param aMessage
	 */
	public InvalidTestTypeException(String aType, String aMessage)
	{
		super(aMessage);
		myType = aType;
	}

	/**
	 * @param aCause
	 */
	public InvalidTestTypeException(String aType, Throwable aCause)
	{
		super(aCause);
		myType = aType;
	}

	/**
	 * @param aMessage
	 * @param aCause
	 */
	public InvalidTestTypeException(String aType, String aMessage, Throwable aCause)
	{
		super(aMessage, aCause);
		myType = aType;
	}
	
	public String getType()
	{
		return myType;
	}
}
