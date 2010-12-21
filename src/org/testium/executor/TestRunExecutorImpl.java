package org.testium.executor;

import java.io.File;
import java.util.Calendar;

import org.testium.Testium;
import org.testtoolinterfaces.testresult.SutInfo;
import org.testtoolinterfaces.testresult.TestCaseResultLink;
import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testresult.TestGroupResultLink;
import org.testtoolinterfaces.testresult.TestRunResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresultinterface.TestRunResultWriter;
import org.testtoolinterfaces.testsuite.TestCaseLink;
import org.testtoolinterfaces.testsuite.TestEntry;
import org.testtoolinterfaces.testsuite.TestEntryArrayList;
import org.testtoolinterfaces.testsuite.TestGroup;
import org.testtoolinterfaces.testsuite.TestGroupLink;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestStepArrayList;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.Warning;

public class TestRunExecutorImpl
{
	private TestStepMetaExecutor	myTestStepExecutor;
	private TestRunResultWriter 	myTestRunResultWriter;

	private TestGroupMetaExecutor	myTestGroupExecutor;
	private TestCaseMetaExecutor	myTestCaseExecutor;

	/**
	 * @param aTestRunResultWriter 
	 * @param myTestStepExecutor
	 * @param myTestCaseScriptExecutor
	 */
	public TestRunExecutorImpl( TestStepMetaExecutor aTestStepMetaExecutor,
	                              TestCaseMetaExecutor aTestCaseMetaExecutor,
	                              TestGroupMetaExecutor aTestGroupMetaExecutor,
	                              TestRunResultWriter aTestRunResultWriter )
	{
		myTestStepExecutor = aTestStepMetaExecutor;
		myTestCaseExecutor = aTestCaseMetaExecutor;
		myTestGroupExecutor = aTestGroupMetaExecutor;
		myTestRunResultWriter = aTestRunResultWriter;
	}

	public void execute( TestGroup aTestGroup,
	                     String aUsername,
	                     String aHostname,
	                     SutInfo aSut,
	                     File aBaseExecutionDir,
	                     RunTimeData anRtData )
	{
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

		execute(aTestGroup, result, aBaseExecutionDir, logDir);
		
		Calendar endDate = Calendar.getInstance();
		endDate.setTimeInMillis(System.currentTimeMillis());
		result.setEndDate( endDate );
		result.setStatus(TestRunResult.FINISHED);
		
		myTestRunResultWriter.update( result );
	}

	public void execute(TestGroup aTestGroup, TestRunResult aTestRunResult, File aScriptDir, File aLogDir)
	{
		Trace.println(Trace.EXEC, "execute( " 
				+ aTestGroup.getId() + ", "
				+ aScriptDir.getPath() + ", "
	            + aLogDir.getPath() + " )", true );

		TestGroupResult tgResult = new TestGroupResult( aTestGroup );
		aTestRunResult.setTestGroup(tgResult);

    	TestStepArrayList prepareSteps = aTestGroup.getPrepareSteps();
    	executePrepareSteps(prepareSteps, aTestRunResult, aScriptDir, aLogDir);

    	TestEntryArrayList execSteps = aTestGroup.getExecutionEntries();
		executeExecSteps(execSteps, aTestRunResult, aScriptDir, aLogDir);

    	TestStepArrayList restoreSteps = aTestGroup.getRestoreSteps();
    	executeRestoreSteps(restoreSteps, aTestRunResult, aScriptDir, aLogDir);
	}

	public void executePrepareSteps(TestStepArrayList aPrepareSteps, TestRunResult aResult, File aScriptDir, File aLogDir)
	{
		Trace.println(Trace.EXEC_PLUS, "executeInitSteps( " 
						+ aPrepareSteps + ", "
						+ aResult + ", "
			            + aLogDir.getPath()
			            + " )", true );

		for (int key = 0; key < aPrepareSteps.size(); key++)
    	{
    		TestStep step = aPrepareSteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir);
			aResult.getTestGroup().addInitialization(tsResult);

			myTestRunResultWriter.update( aResult );
    	}
	}

	public void executeExecSteps( TestEntryArrayList anExecEntries,
	                              TestRunResult aResult,
	                              File aScriptDir,
	                              File aLogDir )
	{
		Trace.println(Trace.EXEC_PLUS, "executeExecSteps( " 
						+ anExecEntries + ", "
						+ aResult + ", "
			            + aScriptDir.getPath() + ", "
			            + aLogDir.getPath()
			            + " )", true );

		for (int key = 0; key < anExecEntries.size(); key++)
    	{
			TestEntry entry = anExecEntries.get(key);
			try
			{
				if ( entry.getType() == TestEntry.TYPE.GroupLink )
				{
					if ( myTestGroupExecutor == null )
					{
						throw new Error("No Executor is defined for TestGroupLinks");
					}
					
					TestGroupLink tgLink = (TestGroupLink) entry;
					tgLink.setLinkDir( aScriptDir );

					TestGroupResultLink tgResult = myTestGroupExecutor.execute(tgLink, aLogDir);
					aResult.getTestGroup().addTestGroup(tgResult);
				}
				else if ( entry.getType() == TestEntry.TYPE.CaseLink )
				{
					if ( myTestCaseExecutor == null )
					{
						throw new Error("No Executor is defined for TestCaseLinks");
					}
					
					TestCaseLink tcLink = (TestCaseLink) entry;
					tcLink.setLinkDir( aScriptDir );

					TestCaseResultLink tcResult = myTestCaseExecutor.execute(tcLink, aLogDir);
					aResult.getTestGroup().addTestCase(tcResult);
				}
				else
				{
					throw new InvalidTestTypeException( entry.getType().toString(), "Cannot execute execution entries of type " + entry.getType() );
				}
			}
			catch (InvalidTestTypeException e)
			{
				String message = "Execution of " + entry.getType() + " " + entry.getId() + " failed:\n"
					+ e.getMessage()
					+ "\nTrying to continue, but this may affect further execution...";
				aResult.getTestGroup().addComment(message);
				Warning.println(message);
				Trace.print(Trace.ALL, e);
			}

			myTestRunResultWriter.update( aResult );
    	}
	}

	public void executeRestoreSteps(TestStepArrayList aRestoreSteps, TestRunResult aResult, File aScriptDir, File aLogDir)
	{
		Trace.println(Trace.EXEC_PLUS, "executeRestoreSteps( " 
						+ aRestoreSteps + ", "
						+ aResult + ", "
			            + aLogDir.getPath()
			            + " )", true );

		for (int key = 0; key < aRestoreSteps.size(); key++)
    	{
    		TestStep step = aRestoreSteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir);
			aResult.getTestGroup().addRestore(tsResult);

			myTestRunResultWriter.update( aResult );
    	}
	}
}
