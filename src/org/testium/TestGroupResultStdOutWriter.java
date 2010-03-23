/**
 * 
 */
package org.testium;

import java.util.ArrayList;
import java.util.Hashtable;

import org.testtoolinterfaces.testresult.TestCaseResult;
import org.testtoolinterfaces.testresult.TestGroupResult;

import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan Kranenburg
 *
 */
public class TestGroupResultStdOutWriter
{
	private int myIndentLevel = 0;
	private TestGroupResultStdOutWriter myTgResultWriter = null;
	private TestCaseResultStdOutWriter myTcResultWriter;
	
	private ArrayList<String> myPrintedTGs = new ArrayList<String>();
	private Hashtable<String, ArrayList<String>> myPrintedTCs = new Hashtable<String, ArrayList<String>>();
	
	public TestGroupResultStdOutWriter(int anIndentLevel)
	{
		Trace.println(Trace.CONSTRUCTOR);

		myIndentLevel = anIndentLevel;
		myTcResultWriter = new TestCaseResultStdOutWriter( anIndentLevel+1 );
	}

	/**
	 * 
	 */
	public void printLatest(TestGroupResult aTestGroupResult)
	{
	    Trace.println(Trace.UTIL, "printLatest( " + aTestGroupResult.getId() + " )", true);
	    
	    String testGroupResultId = aTestGroupResult.getId();
	    if ( ! myPrintedTGs.contains( testGroupResultId ) )
	    {
			String indent = repeat( ' ', myIndentLevel );
			System.out.println( indent + testGroupResultId );
	    	
			myPrintedTGs.add(testGroupResultId);
			myPrintedTCs.put(testGroupResultId, new ArrayList<String>());
	    }
	    
		// Test Groups
		Hashtable<Integer, TestGroupResult> tgResults = aTestGroupResult.getTestGroupResults();
    	for (int key = 0; key < tgResults.size(); key++)
    	{
    		if ( myTgResultWriter == null )
    		{
        		myTgResultWriter = new TestGroupResultStdOutWriter( myIndentLevel+1 );
    		}

    		myTgResultWriter.printLatest(tgResults.get(key));
    	}

		// Test Cases
		Hashtable<Integer, TestCaseResult> tcResults = aTestGroupResult.getTestCaseResults();
    	for (int key = 0; key < tcResults.size(); key++)
    	{
    		String tcId = tcResults.get(key).getId();
    		if ( ! myPrintedTCs.get(testGroupResultId).contains(tcId) )
    		{
        		myTcResultWriter.print(tcResults.get(key));
        		myPrintedTCs.get(testGroupResultId).add( tcId );
    		}
    	}
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
