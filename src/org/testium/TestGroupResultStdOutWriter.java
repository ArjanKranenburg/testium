/**
 * 
 */
package org.testium;

import java.util.ArrayList;
import java.util.Hashtable;

import org.testtoolinterfaces.testresult.TestCaseResult;
import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testresult.TestStepResult;

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
	private TestStepResultStdOutWriter myTsResultWriter;
	
	private ArrayList<String> myPrintedTGs = new ArrayList<String>();
	private Hashtable<String, ArrayList<String>> myPrintedTCs = new Hashtable<String, ArrayList<String>>();
	private Hashtable<String, ArrayList<String>> myPrintedInitializes = new Hashtable<String, ArrayList<String>>();
	private Hashtable<String, ArrayList<String>> myPrintedRestores = new Hashtable<String, ArrayList<String>>();
	
	public TestGroupResultStdOutWriter(int anIndentLevel)
	{
		Trace.println(Trace.CONSTRUCTOR);

		myIndentLevel = anIndentLevel;
		myTcResultWriter = new TestCaseResultStdOutWriter( anIndentLevel+1 );
		myTsResultWriter = new TestStepResultStdOutWriter( anIndentLevel+1 );
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
			myPrintedInitializes.put(testGroupResultId, new ArrayList<String>());
			myPrintedRestores.put(testGroupResultId, new ArrayList<String>());
	    }
	    
		// Initialization Steps
		Hashtable<Integer, TestStepResult> initResults = aTestGroupResult.getInitializationResults();
    	for (int key = 0; key < initResults.size(); key++)
    	{
    		String tsId = initResults.get(key).getDisplayName();
    		if ( ! myPrintedInitializes.get(testGroupResultId).contains(tsId) )
    		{
    			myTsResultWriter.print(initResults.get(key));
    			myPrintedInitializes.get(testGroupResultId).add( tsId );
    		}
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

		// Restoration Steps
		Hashtable<Integer, TestStepResult> restoreResults = aTestGroupResult.getRestoreResults();
    	for (int key = 0; key < restoreResults.size(); key++)
    	{
    		String tsId = restoreResults.get(key).getDisplayName();
    		if ( ! myPrintedRestores.get(testGroupResultId).contains(tsId) )
    		{
    			myTsResultWriter.print(restoreResults.get(key));
    			myPrintedRestores.get(testGroupResultId).add( tsId );
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
