/**
 * 
 */
package net.sf.testium;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import org.testtoolinterfaces.testresult.TestCaseResultLink;
import org.testtoolinterfaces.testresult.TestGroupEntryResult;
import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testresult.TestStepResultBase;
import org.testtoolinterfaces.testresultinterface.TestGroupResultWriter;
import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan Kranenburg
 *
 */
public class TestGroupResultStdOutWriter implements TestGroupResultWriter
{
	private int myIndentLevel = 0;
	private TestStepResultStdOutWriter myTsResultWriter;
	private TestCaseResultStdOutWriter myTcResultWriter;
//	private TestGroupResultStdOutWriter myTgResultWriter;
	
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
	}

	public void write(TestGroupResult aTestGroupResult, File aResultFile)
	{
	    String testGroupResultId = aTestGroupResult.getId();
	    Trace.println(Trace.UTIL, "write( " + testGroupResultId + " )", true);

	    String tgLongId = aTestGroupResult.getExecutionPath() + "." + testGroupResultId;

	    if ( ! myPrintedTGs.contains( tgLongId ) )
	    {
		    String indent = repeat( ' ', myIndentLevel );
			System.out.println( indent + testGroupResultId );
	    	
			myPrintedTGs.add(tgLongId);
			myPrintedTCs.put(tgLongId, new ArrayList<String>());
			myPrintedPrepares.put(tgLongId, new ArrayList<String>());
			myPrintedRestores.put(tgLongId, new ArrayList<String>());
	    }

	    aTestGroupResult.register(this);
	}

	public void notify(TestGroupResult aTestGroupResult)
	{
	    String testGroupResultId = aTestGroupResult.getId();
	    Trace.println(Trace.UTIL, "update( " + testGroupResultId + " )", true);

	    String tgLongId = aTestGroupResult.getExecutionPath() + "." + testGroupResultId;
	    if ( ! myPrintedTGs.contains( tgLongId ) )
	    {
			String indent = repeat( ' ', myIndentLevel );
			System.out.println( indent + testGroupResultId );
	    	
			myPrintedTGs.add(tgLongId);
			myPrintedTCs.put(tgLongId, new ArrayList<String>());
			myPrintedPrepares.put(tgLongId, new ArrayList<String>());
			myPrintedRestores.put(tgLongId, new ArrayList<String>());
	    }
	    
		// Prepare Steps
		Hashtable<Integer, TestStepResultBase> prepareResults = aTestGroupResult.getPrepareResults();
    	for (int key = 0; key < prepareResults.size(); key++)
    	{
    		String tsDisplayName = prepareResults.get(key).getDisplayName();
    		if ( ! myPrintedPrepares.get(tgLongId).contains(tsDisplayName) )
    		{
    			myTsResultWriter.print(prepareResults.get(key));
    			myPrintedPrepares.get(tgLongId).add( tsDisplayName );
    		}
    	}

		// Test Groups
//		if ( myTgResultWriter == null )
//		{
//    		myTgResultWriter = new TestGroupResultStdOutWriter( myIndentLevel+1 );
//		}
//
//		Hashtable<Integer, TestGroupResultLink> tgResults = aTestGroupResult.getTestGroupResultLinks();
//    	for (int key = 0; key < tgResults.size(); key++)
//    	{
//    	    TestGroupResult testGroupResult = myTestGroupResultReader.readTgResultFile( tgResults.get(key) );
//    	    myTgResultWriter.update( testGroupResult );
//    	}

		// Test Cases
		Hashtable<Integer,TestGroupEntryResult> teResults = aTestGroupResult.getTestGroupEntryResultsTable();
    	for (int key = 0; key < teResults.size(); key++)
    	{
    		TestGroupEntryResult teResult = teResults.get(key);
    		if ( teResult instanceof TestCaseResultLink ) {
    			TestCaseResultLink tcResultLink = (TestCaseResultLink) teResult;
	    		String idPath = tcResultLink.getExecutionIdPath();
	    		if ( ! myPrintedTCs.get(tgLongId).contains(idPath) )
	    		{
	        		myTcResultWriter.print(tcResultLink);
	        		myPrintedTCs.get(tgLongId).add( idPath );
	    		}
    		}
    	}

		// Restoration Steps
		Hashtable<Integer, TestStepResultBase> restoreResults = aTestGroupResult.getRestoreResults();
    	for (int key = 0; key < restoreResults.size(); key++)
    	{
    		String tsDisplayName = restoreResults.get(key).getDisplayName();
    		if ( ! myPrintedRestores.get(tgLongId).contains(tsDisplayName) )
    		{
    			myTsResultWriter.print(restoreResults.get(key));
    			myPrintedRestores.get(tgLongId).add( tsDisplayName );
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
