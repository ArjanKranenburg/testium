package org.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestGroupResult;
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
	 */
	public TestGroupMetaExecutor( TestGroupExecutor aTestGroupExecutor,
	                              TestGroupLinkExecutor aTestGroupLinkExecutor )
	{
		Trace.println(Trace.CONSTRUCTOR, "TestGroupMetaExecutor( " 
						+ aTestGroupExecutor + ", "
			            + aTestGroupLinkExecutor + " )", true );
		myTestGroupExecutor = aTestGroupExecutor;
		myTestGroupLinkExecutor = aTestGroupLinkExecutor;
	}

	public TestGroupResult execute(TestGroup aTestGroup, File aScriptDir, File aLogDir)
	{
		Trace.println(Trace.EXEC, "execute( " 
				+ aTestGroup + ", "
	            + aScriptDir.getAbsolutePath() + ", "
	            + aLogDir.getAbsolutePath() + " )", true );

		TestGroupResult aTestGroupResult;
		if( aTestGroup.getType().equals(TestEntry.TYPE.GroupLink) )
		{
			aTestGroupResult = myTestGroupLinkExecutor.execute((TestGroupLink) aTestGroup, aScriptDir, aLogDir);
		}
		else
		{
			aTestGroupResult = myTestGroupExecutor.execute(aTestGroup, aScriptDir, aLogDir);
		}
		
		return aTestGroupResult;
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
