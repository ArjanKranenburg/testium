package org.testium;

import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testresult.TestRunResult;
import org.testtoolinterfaces.testresultinterface.TestRunResultWriter;
import org.testtoolinterfaces.utils.Trace;


public class TestRunResultStdOutWriter implements TestRunResultWriter
{
	private TestRunResult myRunResult;

	private TestGroupResultStdOutWriter myTgResultWriter;

	public TestRunResultStdOutWriter()
	{
		Trace.println( Trace.CONSTRUCTOR );
		myTgResultWriter = new TestGroupResultStdOutWriter( 0 );
	}

	public void setResult(TestRunResult aRunResult)
	{
		myRunResult = aRunResult;
	}

	@Override
	public void intermediateWrite()
	{
	    Trace.println(Trace.UTIL);

		if ( myRunResult == null )
		{
			return;
		}

		// Test Groups
		TestGroupResult tgResult = myRunResult.getTestGroup();
   		myTgResultWriter.printLatest(tgResult);
	}

	@Override
	public void write()
	{
	    Trace.println(Trace.UTIL);
		if ( myRunResult == null )
		{
			return;
		}

		// Print grant totals
		System.out.println( "======================================================================" );
		System.out.println();
		System.out.println( "Total Test Cases:        " + myRunResult.getNrOfTCs() );
		System.out.println( "Total Test Cases Passed: " + myRunResult.getNrOfTCsPassed() );
		System.out.println( "Total Test Cases Failed: " + myRunResult.getNrOfTCsFailed() );
		System.out.println();
		System.out.println( "Tests finished at:       " + myRunResult.getEndDateString() + " " + myRunResult.getEndTimeString() );
	}
}
