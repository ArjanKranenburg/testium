package net.sf.testium;

import java.io.File;

import org.testtoolinterfaces.testresult.ResultSummary;
import org.testtoolinterfaces.testresult.TestRunResult;
import org.testtoolinterfaces.testresult.TestRunResult.TEST_RUN_STATUS;
import org.testtoolinterfaces.testresultinterface.TestRunResultWriter;
import org.testtoolinterfaces.utils.Trace;


public class TestRunResultStdOutWriter implements TestRunResultWriter
{

	public TestRunResultStdOutWriter()
	{
		Trace.println( Trace.CONSTRUCTOR );
	}

	public void write( TestRunResult aRunResult, File aFile )
	{
	    Trace.println(Trace.UTIL);
		if ( aRunResult == null )
		{
			return;
		}

//		System.out.println( "Tests started at:        " + aRunResult.getStartDateString() + " " + aRunResult.getStartTimeString() );
		System.out.println();
		System.out.println( aRunResult.getTestSuite() );

		aRunResult.register(this);
	}

	public void notify( TestRunResult aRunResult )
	{
	    Trace.println(Trace.UTIL);

   		if (aRunResult.getStatus().equals(TEST_RUN_STATUS.FINISHED))
   		{
   	   		// Print grand totals
   			System.out.println( "======================================================================" );
   			System.out.println();
   			ResultSummary summary = aRunResult.getSummary();
   			System.out.println( "Total Test Cases:         " + summary.getNrOfTCs() );
   			System.out.println( "Total Test Cases Passed:  " + summary.getNrOfTCsPassed() );
   			System.out.println( "Total Test Cases Failed:  " + summary.getNrOfTCsFailed() );
   			System.out.println( "Total Test Cases Error:   " + summary.getNrOfTCsError() );
   			System.out.println( "Total Test Cases Unknown: " + summary.getNrOfTCsUnknown() );
   			System.out.println();
   			System.out.println( "Tests finished at:       " + aRunResult.getEndDateString() + " " + aRunResult.getEndTimeString() );
   			System.out.println( "Tests started at:        " + aRunResult.getStartDateString() + " " + aRunResult.getStartTimeString() );
   		}
	}
}
