package net.sf.testium.executor;

import java.io.File;
import java.util.Calendar;

import net.sf.testium.Testium;

import org.testtoolinterfaces.testresult.SutInfo;
import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testresult.TestRunResult;
import org.testtoolinterfaces.testresultinterface.TestGroupResultWriter;
import org.testtoolinterfaces.testresultinterface.TestRunResultWriter;
import org.testtoolinterfaces.testsuite.TestGroup;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;

public class TestRunExecutorImpl
{
	private TestGroupExecutorImpl	myTestGroupExecutor;

	private TestRunResultWriter 	myTestRunResultWriter;

	/**
	 * @param aTestRunResultWriter 
	 * @param myTestStepExecutor
	 * @param myTestCaseScriptExecutor
	 */
	public TestRunExecutorImpl( TestStepMetaExecutor aTestStepMetaExecutor,
	                            TestCaseMetaExecutor aTestCaseMetaExecutor,
	                            TestGroupMetaExecutor aTestGroupExecutor,
	                            TestGroupResultWriter aTestGroupResultWriter,
	                            TestRunResultWriter aTestRunResultWriter )
	{
		Trace.println( Trace.CONSTRUCTOR );
		myTestGroupExecutor = new TestGroupExecutorImpl( aTestStepMetaExecutor,
		                                                 aTestCaseMetaExecutor,
		                                                 aTestGroupExecutor,
		                                                 aTestGroupResultWriter );

		myTestRunResultWriter = aTestRunResultWriter;
	}

	public int execute( TestGroup aTestGroup,
	                     String aUsername,
	                     String aHostname,
	                     SutInfo aSut,
	                     File aBaseExecutionDir,
	                     RunTimeData anRtData )
	{
		Trace.println(Trace.EXEC, "execute( " 
						+ aTestGroup.getId() + ", "
						+ aUsername + ", "
						+ aHostname + ", "
						+ aSut.getName() + ", "
			            + aBaseExecutionDir.getPath() + ", "
						+ anRtData.size() + " Variables )", true );

		String testGroupId = anRtData.getValueAsString(Testium.TESTGROUP);
		if ( testGroupId == null )
		{
			testGroupId = aTestGroup.getId();
		}

		Calendar date = Calendar.getInstance();
		File logDir = anRtData.getValueAsFile(Testium.RESULTBASEDIR);
		
		TestRunResult result = new TestRunResult( aTestGroup.getId(), // Test Suite
		                                          testGroupId, // DisplayName
		                                          aUsername,
		                                          aHostname,
		                                          aSut,
		                                          date,
		                                          TestRunResult.STARTED );

		File runLogFile = new File(logDir, aTestGroup.getId() + "_run.xml");
		myTestRunResultWriter.write( result, runLogFile );

		if ( myTestGroupExecutor == null )
		{
			throw new Error("No Executor is defined for TestGroupLinks");
		}

		TestGroupResult tgResult = new TestGroupResult( aTestGroup );
		result.setTestGroup(tgResult);
		tgResult.setExecutionPath(aSut.getName());
		myTestGroupExecutor.execute( aTestGroup,
		                             aBaseExecutionDir,
		                             logDir,
		                             tgResult,
		                             anRtData );
		
		Calendar endDate = Calendar.getInstance();
		endDate.setTimeInMillis(System.currentTimeMillis());
		result.setEndDate( endDate );
		result.setStatus(TestRunResult.FINISHED);
		myTestRunResultWriter.write( result, runLogFile );
		
		if ( result.getNrOfTCsError() > 0 )
		{
			return anRtData.getValueAs(Integer.class, Testium.EXITCODEONERRORS).intValue();
		}

		if ( result.getNrOfTCsFailed() > 0 )
		{
			return anRtData.getValueAs(Integer.class, Testium.EXITCODEONFAILURES).intValue();
		}
		
		return 0;
	}
}
