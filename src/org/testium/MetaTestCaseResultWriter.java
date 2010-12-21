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

	@Override
	public void update( TestCaseResult aCaseResult )
	{
	    for (TestCaseResultWriter resultWriter : myWriters)
	    {
		    resultWriter.update( aCaseResult );
	    }
	}

	@Override
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
