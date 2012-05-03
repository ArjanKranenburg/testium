package org.testium;

import java.io.File;
import java.util.ArrayList;

import org.testtoolinterfaces.testresult.TestCaseResult;
import org.testtoolinterfaces.testresultinterface.TestCaseResultWriter;
import org.testtoolinterfaces.utils.Trace;


public class MetaTestCaseResultWriter implements TestCaseResultWriter
{
	private ArrayList<TestCaseResultWriter> myWriters;

	public MetaTestCaseResultWriter()
	{
		Trace.println( Trace.CONSTRUCTOR );

		myWriters = new ArrayList<TestCaseResultWriter>();
	}

	public void notify( TestCaseResult aCaseResult )
	{
		// NOP
		// Won't register the Meta-ResultWriters.
		// The result Writers have to do that themselves and will then be notified of updates.

//	    for (TestCaseResultWriter resultWriter : myWriters)
//	    {
//		    resultWriter.update( aCaseResult );
//	    }
	}

	public void write(TestCaseResult aGroupResult, File aResultFile)
	{
	    for (TestCaseResultWriter resultWriter : myWriters)
	    {
		    resultWriter.write(aGroupResult, aResultFile);
	    }
	}

	public void add(TestCaseResultWriter aTestCaseResultWriter)
	{
		myWriters.add(aTestCaseResultWriter);
	}
}
