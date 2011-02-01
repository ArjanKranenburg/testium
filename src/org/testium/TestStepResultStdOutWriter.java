package org.testium;

import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.utils.Trace;

public class TestStepResultStdOutWriter
{
	private int myIndentLevel = 0;

	/**
	 * @param aTestCaseName
	 */
	public TestStepResultStdOutWriter(int anIndentLevel)
	{
		Trace.println(Trace.CONSTRUCTOR);

		myIndentLevel = anIndentLevel;
	}

	/**
	 * @param aResult	the Test Case Result
	 * 
	 */
	public void print(TestStepResult aResult)
	{
		Trace.println(Trace.UTIL);

		String indent = repeat( ' ', myIndentLevel );
    	String tsId = indent + aResult.getDisplayName();

    	int spaceleft = 1;
		if ( tsId.length() < 70 )
		{
			spaceleft = 70 - tsId.length();
		}
		
		String outline = repeat( ' ', spaceleft );
		String result = "";
		if ( aResult.getResult().equals(VERDICT.PASSED) )
		{
			result = "OK";
		}
		else
		{
			result = "ERROR: " + aResult.getComment();
		}
		System.out.println( tsId + outline + result );
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
