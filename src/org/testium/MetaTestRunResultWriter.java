package org.testium;

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

	public void setResult(TestRunResult aRunResult)
	{
	    for (TestRunResultWriter resultWriter : myWriters)
	    {
		    resultWriter.setResult( aRunResult );
	    }
	}

	@Override
	public void intermediateWrite()
	{
	    for (TestRunResultWriter resultWriter : myWriters)
	    {
		    resultWriter.intermediateWrite();
	    }
	}

	@Override
	public void write()
	{
	    for (TestRunResultWriter resultWriter : myWriters)
	    {
		    resultWriter.write();
	    }
	}

	public void add(TestRunResultWriter aTestRunResultWriter)
	{
		myWriters.add(aTestRunResultWriter);
	}
}
