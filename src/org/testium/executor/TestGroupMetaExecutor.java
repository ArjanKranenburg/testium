package org.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testresultinterface.TestRunResultWriter;
import org.testtoolinterfaces.testsuite.TestEntry;
import org.testtoolinterfaces.testsuite.TestGroup;
import org.testtoolinterfaces.testsuite.TestGroupLink;
import org.testtoolinterfaces.utils.Trace;


public class TestGroupMetaExecutor implements TestGroupExecutor
{
	private TestGroupExecutor		myTestGroupExecutor;
	private TestGroupLinkExecutor	myTestGroupLinkExecutor;

	/**
	 * @param aTestGroupExecutor
	 * @param aTestGroupLinkExecutor
	 * @param aTestRunResultWriter 
	 */
	public TestGroupMetaExecutor( TestGroupExecutor aTestGroupExecutor,
	                              TestGroupLinkExecutor aTestGroupLinkExecutor,
	                              TestRunResultWriter aTestRunResultWriter )
	{
		Trace.println(Trace.CONSTRUCTOR, "TestGroupMetaExecutor( " 
						+ aTestGroupExecutor + ", "
			            + aTestGroupLinkExecutor + " )", true );
		myTestGroupExecutor = aTestGroupExecutor;
		myTestGroupLinkExecutor = aTestGroupLinkExecutor;
	}

	@Override
	public void execute( TestGroup aTestGroup,
						 File aScriptDir,
						 File aLogDir,
						 TestGroupResult aResult )
	{
		Trace.println(Trace.EXEC, "execute( " 
				+ aTestGroup + ", "
	            + aScriptDir.getAbsolutePath() + ", "
	            + aLogDir.getAbsolutePath() + " )", true );

		if( aTestGroup.getType().equals(TestEntry.TYPE.GroupLink) )
		{
			myTestGroupLinkExecutor.execute((TestGroupLink) aTestGroup, aScriptDir, aLogDir, aResult);
		}
		else
		{
			myTestGroupExecutor.execute(aTestGroup, aScriptDir, aLogDir, aResult);
		}
	}

	public void setTestCaseExecutor(TestCaseExecutor aTestCaseExecutor)
	{
		myTestGroupExecutor.setTestCaseExecutor( aTestCaseExecutor );
	}

	public void setTestGroupExecutor(TestGroupExecutor aTestGroupExecutor)
	{
		myTestGroupExecutor = aTestGroupExecutor;
	}

	public void setTestGroupLinkExecutor(TestGroupLinkExecutor aTestGroupLinkExecutor)
	{
		myTestGroupLinkExecutor = aTestGroupLinkExecutor;
		myTestGroupExecutor.setTestGroupLinkExecutor( aTestGroupLinkExecutor );
	}
}
