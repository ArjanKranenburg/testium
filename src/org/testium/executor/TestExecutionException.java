/**
 * 
 */
package org.testium.executor;

/**
 * @author Arjan Kranenburg
 *
 */
public class TestExecutionException extends Exception
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1753333459102488249L;

	/**
	 * @param aMessage
	 */
	public TestExecutionException(String aMessage)
	{
		super(aMessage);
	}

	/**
	 * @param aCause
	 */
	public TestExecutionException(Throwable aCause)
	{
		super(aCause);
	}

	/**
	 * @param aMessage
	 * @param aCause
	 */
	public TestExecutionException(String aMessage, Throwable aCause)
	{
		super(aMessage, aCause);
	}
}
