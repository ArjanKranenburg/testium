/**
 * 
 */
package net.sf.testium.executor;

import org.testtoolinterfaces.testsuite.TestCaseLink;

/**
 * Exception to use when a Test Case Link cannot be executed
 * 
 * @author Arjan Kranenburg
 *
 */
public class TestCaseLinkExecutionException extends ExecutionException
{
	private static final long	serialVersionUID	= -6550139698499202078L;

	/**
	 * @param aTestCaseLink
	 * @param aMessage
	 */
	public TestCaseLinkExecutionException(TestCaseLink aTestCaseLink, String aMessage)
	{
		super( aTestCaseLink, aMessage );
	}

	/**
	 * @param aTestCaseLink
	 * @param aCause
	 */
	public TestCaseLinkExecutionException(TestCaseLink aTestCaseLink, Throwable aCause)
	{
		super( aTestCaseLink, aCause );
	}

	/**
	 * @param aTestCaseLink
	 * @param aMessage
	 * @param aCause
	 */
	public TestCaseLinkExecutionException(TestCaseLink aTestCaseLink, String aMessage, Throwable aCause)
	{
		super(aTestCaseLink, aMessage, aCause);
	}
	
	public TestCaseLink getTestCaseLink()
	{
		return (TestCaseLink) this.getTestCaseLink();
	}
}
