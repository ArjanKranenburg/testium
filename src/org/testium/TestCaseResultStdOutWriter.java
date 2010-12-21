/**
 * 
 */
package org.testium;

import org.testtoolinterfaces.testresult.TestCaseResultLink;
import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan Kranenburg
 *
 */
public class TestCaseResultStdOutWriter
{
	private int myIndentLevel = 0;

	/**
	 * @param aTestCaseName
	 */
	public TestCaseResultStdOutWriter(int anIndentLevel)
	{
		Trace.println(Trace.CONSTRUCTOR);

		myIndentLevel = anIndentLevel;
	}

	/**
	 * @param aTestCaseResultLink	the Test Case Result
	 * 
	 */
	public void print(TestCaseResultLink aTestCaseResultLink)
	{
		Trace.println(Trace.UTIL);

		String indent = repeat( ' ', myIndentLevel );
		String tcId = indent + aTestCaseResultLink.getId();
		int spaceleft = 1;
		if ( tcId.length() < 80 )
		{
			spaceleft = 80 - tcId.length();
		}
		
		String outline = repeat( ' ', spaceleft );
		System.out.println( tcId + outline + aTestCaseResultLink.getResult().toString() );
	}
	
	private static String repeat(char c,int i)
	{
		String str = "";
		for(int j = 0; j < i; j++)
		{
			str = str+c;
		}
		return str;
	}
}
