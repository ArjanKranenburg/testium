package org.testium;

import java.io.File;

import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testresult.TestRunResult;
import org.testtoolinterfaces.testresult.TestRunResult.TEST_RUN_STATUS;
import org.testtoolinterfaces.testresultinterface.TestRunResultWriter;
import org.testtoolinterfaces.utils.Trace;


public class TestRunResultStdOutWriter implements TestRunResultWriter
{
	private TestGroupResultStdOutWriter myTgResultWriter;

	public TestRunResultStdOutWriter()
	{
		Trace.println( Trace.CONSTRUCTOR );
		myTgResultWriter = new TestGroupResultStdOutWriter( 0 );
	}

	@Override
	public void write( TestRunResult aRunResult, File aFile )
	{
	    Trace.println(Trace.UTIL);
		if ( aRunResult == null )
		{
			return;
		}
	}

	@Override
	public void update( TestRunResult aRunResult )
	{
	    Trace.println(Trace.UTIL);

		TestGroupResult tgResult = aRunResult.getTestGroup();
   		myTgResultWriter.update(tgResult);

   		if (aRunResult.getStatus().equals(TEST_RUN_STATUS.FINISHED))
   		{
   	   		// Print grant totals
   			System.out.println( "======================================================================" );
   			System.out.println();
   			System.out.println( "Total Test Cases:        " + aRunResult.getNrOfTCs() );
   			System.out.println( "Total Test Cases Passed: " + aRunResult.getNrOfTCsPassed() );
   			System.out.println( "Total Test Cases Failed: " + aRunResult.getNrOfTCsFailed() );
   			System.out.println();
   			System.out.println( "Tests finished at:       " + aRunResult.getEndDateString() + " " + aRunResult.getEndTimeString() );
   		}
	}
}
