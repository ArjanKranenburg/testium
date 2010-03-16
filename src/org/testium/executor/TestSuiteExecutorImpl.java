package org.testium.executor;

import java.io.File;
import java.net.UnknownHostException;
import java.util.Calendar;

import org.testium.systemundertest.SutControl;
import org.testtoolinterfaces.testresult.SutInfo;
import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testresult.TestRunResult;
import org.testtoolinterfaces.testresultinterface.TestRunResultWriter;
import org.testtoolinterfaces.testsuite.TestGroup;
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

	public void execute(TestGroup aTestGroup, File aScriptDir, File aLogDir, Calendar aDate) throws TestExecutionException
	{
		if ( !aLogDir.isDirectory() )
		{
			throw new TestExecutionException("Directory does not exist: " + aLogDir.getAbsolutePath());
		}
		Trace.println(Trace.LEVEL.EXEC, "execute( " 
	            + aTestGroup.getId() + ", "
	            + aScriptDir.getAbsolutePath() + ", "
	            + aLogDir.getAbsolutePath() + " )", true );

		SutInfo sut = mySutControl.getSutInfo( aLogDir );

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
		                                          aTestGroup.getId(), // DisplayName
		                                          username,
		                                          hostname,
		                                          sut,
		                                          aDate,
		                                          TestRunResult.STARTED );

		myTestRunResultWriter.setResult( result );
    	TestGroupResult tgResult = new TestGroupResult(aTestGroup);
		result.setTestGroup(tgResult);

		myTestGroupExecutor.execute(aTestGroup, aScriptDir, aLogDir, tgResult);
		
		result.setEndDate( Calendar.getInstance() );
		result.setStatus(TestRunResult.FINISHED);
		
		myTestRunResultWriter.write( result );
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
