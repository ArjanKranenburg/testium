/**
 * 
 */
package net.sf.testium.executor;

import org.testtoolinterfaces.testsuite.TestCase;

/**
 * Exception to use when a Test Case cannot be executed
 * 
 * @author Arjan Kranenburg
 *
 */
public class TestCaseExecutionException extends ExecutionException
{
	private static final long	serialVersionUID	= 3138293884215794L;

	/**
	 * @param aTestCase
	 * @param aMessage
	 */
	public TestCaseExecutionException(TestCase aTestCase, String aMessage)
	{
		super( aTestCase, aMessage );
	}

	/**
	 * @param aTestCase
	 * @param aCause
	 */
	public TestCaseExecutionException(TestCase aTestCase, Throwable aCause)
	{
		super( aTestCase, aCause );
	}

	/**
	 * @param aTestCase
	 * @param aMessage
	 * @param aCause
	 */
	public TestCaseExecutionException(TestCase aTestCase, String aMessage, Throwable aCause)
	{
		super(aTestCase, aMessage, aCause);
	}
	
	public TestCase getTestCase()
	{
		return (TestCase) this.getTestCase();
	}
}
