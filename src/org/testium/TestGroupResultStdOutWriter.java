/**
 * 
 */
package org.testium;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import org.testtoolinterfaces.testresult.TestCaseResultLink;
import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testresult.TestGroupResultLink;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresultinterface.TestGroupResultReader;
import org.testtoolinterfaces.testresultinterface.TestGroupResultWriter;

import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan Kranenburg
 *
 */
public class TestGroupResultStdOutWriter implements TestGroupResultWriter
{
	private int myIndentLevel = 0;
	private TestGroupResultStdOutWriter myTgResultWriter = null;
	private TestCaseResultStdOutWriter myTcResultWriter;
	private TestStepResultStdOutWriter myTsResultWriter;
	
	private TestGroupResultReader myTestGroupResultReader;
	
	private ArrayList<String> myPrintedTGs = new ArrayList<String>();
	private Hashtable<String, ArrayList<String>> myPrintedTCs = new Hashtable<String, ArrayList<String>>();
	private Hashtable<String, ArrayList<String>> myPrintedPrepares = new Hashtable<String, ArrayList<String>>();
	private Hashtable<String, ArrayList<String>> myPrintedRestores = new Hashtable<String, ArrayList<String>>();
	
	public TestGroupResultStdOutWriter(int anIndentLevel)
	{
		Trace.println(Trace.CONSTRUCTOR);

		myIndentLevel = anIndentLevel;
		myTcResultWriter = new TestCaseResultStdOutWriter( anIndentLevel+1 );
		myTsResultWriter = new TestStepResultStdOutWriter( anIndentLevel+1 );
		
		myTestGroupResultReader = new TestGroupResultReader();
	}

	@Override
	public void write(TestGroupResult aTestGroupResult, File aResultFile)
	{
		// NOP		
	}

	@Override
	public void update(TestGroupResult aTestGroupResult)
	{
	    String testGroupResultId = aTestGroupResult.getId();
	    Trace.println(Trace.UTIL, "printLatest( " + testGroupResultId + " )", true);

	    if ( ! myPrintedTGs.contains( testGroupResultId ) )
	    {
			String indent = repeat( ' ', myIndentLevel );
			System.out.println( indent + testGroupResultId );
	    	
			myPrintedTGs.add(testGroupResultId);
			myPrintedTCs.put(testGroupResultId, new ArrayList<String>());
			myPrintedPrepares.put(testGroupResultId, new ArrayList<String>());
			myPrintedRestores.put(testGroupResultId, new ArrayList<String>());
	    }
	    
		// Prepare Steps
		Hashtable<Integer, TestStepResult> prepareResults = aTestGroupResult.getPrepareResults();
    	for (int key = 0; key < prepareResults.size(); key++)
    	{
    		String tsId = prepareResults.get(key).getDisplayName();
    		if ( ! myPrintedPrepares.get(testGroupResultId).contains(tsId) )
    		{
    			myTsResultWriter.print(prepareResults.get(key));
    			myPrintedPrepares.get(testGroupResultId).add( tsId );
    		}
    	}

		// Test Groups
		Hashtable<Integer, TestGroupResultLink> tgResults = aTestGroupResult.getTestGroupResultLinks();
    	for (int key = 0; key < tgResults.size(); key++)
    	{
    		if ( myTgResultWriter == null )
    		{
        		myTgResultWriter = new TestGroupResultStdOutWriter( myIndentLevel+1 );
    		}

    	    TestGroupResult testGroupResult = myTestGroupResultReader.readTgResultFile( tgResults.get(key) );
    	    myTgResultWriter.update( testGroupResult );
    	}

		// Test Cases
		Hashtable<Integer, TestCaseResultLink> tcResults = aTestGroupResult.getTestCaseResultLinks();
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
