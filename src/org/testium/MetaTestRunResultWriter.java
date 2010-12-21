package org.testium;

import java.io.File;
import java.util.ArrayList;

import org.testtoolinterfaces.testresult.TestRunResult;
import org.testtoolinterfaces.testresultinterface.TestRunResultWriter;
import org.testtoolinterfaces.utils.Trace;


public class MetaTestRunResultWriter implements TestRunResultWriter
{
	private ArrayList<TestRunResultWriter> myWriters;

	public MetaTestRunResultWriter()
	{
		Trace.println( Trace.CONSTRUCTOR );

		myWriters = new ArrayList<TestRunResultWriter>();
	}

	@Override
	public void update(TestRunResult aRunResult)
	{
	    for (TestRunResultWriter resultWriter : myWriters)
	    {
		    resultWriter.update( aRunResult );
	    }
	}

	@Override
	public void write(TestRunResult aRunResult, File aResultFile)
	{
	    for (TestRunResultWriter resultWriter : myWriters)
	    {
		    resultWriter.write( aRunResult, aResultFile );
	    }
	}

	public void add(TestRunResultWriter aTestRunResultWriter)
	{
		myWriters.add(aTestRunResultWriter);
	}
}
