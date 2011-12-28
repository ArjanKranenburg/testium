/**
 * 
 */
package org.testium.executor;

import org.testtoolinterfaces.testsuite.TestEntry;

/**
 * Exception to use when a Test Entry cannot be executed
 * 
 * @author Arjan Kranenburg
 *
 */
public abstract class ExecutionException extends Exception
{
	private static final long	serialVersionUID	= -7180685047199405295L;
	private TestEntry myTestEntry;

	/**
	 * @param aTestEntry
	 * @param aMessage
	 */
	public ExecutionException(TestEntry aTestEntry, String aMessage)
	{
		super( aMessage );
		myTestEntry = aTestEntry;
	}

	/**
	 * @param aTestEntry
	 * @param aCause
	 */
	public ExecutionException(TestEntry aTestEntry, Throwable aCause)
	{
		super( aCause );
		myTestEntry = aTestEntry;
	}

	/**
	 * @param aTestEntry
	 * @param aMessage
	 * @param aCause
	 */
	public ExecutionException(TestEntry aTestEntry, String aMessage, Throwable aCause)
	{
		super(aMessage, aCause);
		myTestEntry = aTestEntry;
	}
	
	public TestEntry getTestEntry()
	{
		return myTestEntry;
	}
}
