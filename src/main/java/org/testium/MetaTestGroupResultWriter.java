package org.testium;

import java.io.File;
import java.util.ArrayList;

import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testresultinterface.TestGroupResultWriter;
import org.testtoolinterfaces.utils.Trace;


public class MetaTestGroupResultWriter implements TestGroupResultWriter
{
	private ArrayList<TestGroupResultWriter> myWriters;
	
	public MetaTestGroupResultWriter()
	{
		Trace.println( Trace.CONSTRUCTOR );

		myWriters = new ArrayList<TestGroupResultWriter>();
	}

	public void notify( TestGroupResult aGroupResult )
	{
		// NOP
		// Won't register the Meta-ResultWriters.
		// The result Writers have to do that themselves and will then be notified of updates.

//	    for (TestGroupResultWriter resultWriter : myWriters)
//	    {
//		    resultWriter.notify( aGroupResult );
//	    }
	}

	public void write(TestGroupResult aGroupResult, File aResultFile)
	{
	    for (TestGroupResultWriter resultWriter : myWriters)
	    {
		    resultWriter.write(aGroupResult, aResultFile);
	    }
	}

	public void add(TestGroupResultWriter aTestGroupResultWriter)
	{
		myWriters.add(aTestGroupResultWriter);
	}
}
