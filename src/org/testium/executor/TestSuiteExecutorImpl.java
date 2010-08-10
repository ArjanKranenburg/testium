package org.testium.executor;

import java.io.File;
import java.net.UnknownHostException;
import java.util.Calendar;

import org.testium.Testium;
import org.testium.systemundertest.SutControl;
import org.testtoolinterfaces.testresult.SutInfo;
import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testresult.TestRunResult;
import org.testtoolinterfaces.testresultinterface.TestRunResultWriter;
import org.testtoolinterfaces.testsuite.TestGroup;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;


public class TestSuiteExecutorImpl implements TestSuiteExecutor
{
	private TestGroupExecutor myTestGroupExecutor;
	private SutControl mySutControl;
	private TestRunResultWriter myTestRunResultWriter;
	
	/**
	 * @param aTestGroupExecutor	Executor for Test Groups
	 * @param aTestRunResultWriter 
	 */
	public TestSuiteExecutorImpl( TestGroupExecutor aTestGroupExecutor, SutControl aSutControl, TestRunResultWriter aTestRunResultWriter )
	{
		myTestGroupExecutor = aTestGroupExecutor;
		mySutControl = aSutControl;
		myTestRunResultWriter = aTestRunResultWriter;
	}

	@Override
	public void execute(TestGroup aTestGroup, String aTestGroupId, File aScriptDir, RunTimeData anRtData)
			throws TestExecutionException
	{
		Trace.println(Trace.LEVEL.EXEC, "execute( " 
	            + aTestGroup.getId() + ", "
	            + aTestGroupId + ", "
	            + aScriptDir.getAbsolutePath() + ", runTimeData )", true );

		Calendar date = Calendar.getInstance();
		File logDir = anRtData.getValueAsFile(Testium.RESULTBASEDIR);
		if ( !logDir.isDirectory() )
		{
			throw new TestExecutionException("Directory does not exist: " + logDir.getAbsolutePath());
		}

		SutInfo sut = mySutControl.getSutInfo( logDir, anRtData );

		String username = System.getProperty("user.name");
		String hostname = "Unknown";
		try
		{
			hostname = java.net.InetAddress.getLocalHost().getHostName();
		}
		catch (UnknownHostException exc)
		{
			// nop, i.e. leave it Unknown
		}

		TestRunResult result = new TestRunResult( aTestGroup.getId(), // Test Suite
		                                          aTestGroupId, // DisplayName
		                                          username,
		                                          hostname,
		                                          sut,
		                                          date,
		                                          TestRunResult.STARTED );

		myTestRunResultWriter.setResult( result );
    	TestGroupResult tgResult = new TestGroupResult(aTestGroup);
		result.setTestGroup(tgResult);

		myTestGroupExecutor.execute(aTestGroup, aTestGroupId, aScriptDir, logDir, tgResult);
		
		Calendar endDate = Calendar.getInstance();
		endDate.setTimeInMillis(System.currentTimeMillis());
		result.setEndDate( endDate );
		result.setStatus(TestRunResult.FINISHED);
		
		myTestRunResultWriter.write();
	}

	/**
	 * @param aSutControl			SutControl to get product and version information
	 */
	public void setSutControl(SutControl aSutControl)
	{
		mySutControl = aSutControl;
	}

	public void setTestGroupExecutor(TestGroupExecutor aTestGroupExecutor)
	{
		myTestGroupExecutor = aTestGroupExecutor;
	}
}
