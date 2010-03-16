package org.testium.executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;

import org.testtoolinterfaces.testresult.TestCaseResult;
import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresultinterface.TestRunResultWriter;
import org.testtoolinterfaces.testsuite.TestCase;
import org.testtoolinterfaces.testsuite.TestCaseLink;
import org.testtoolinterfaces.testsuite.TestEntry;
import org.testtoolinterfaces.testsuite.TestEntryArrayList;
import org.testtoolinterfaces.testsuite.TestGroup;
import org.testtoolinterfaces.testsuite.TestGroupLink;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestStepArrayList;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.Warning;


public class TestGroupExecutorImpl implements TestGroupExecutor
{
	private TestStepExecutor		myTestStepExecutor;
	private TestCaseExecutor		myTestCaseExecutor;
	private TestCaseScriptExecutor	myTestCaseScriptExecutor;
	private TestGroupExecutor		myTestGroupExecutor;
	private TestGroupLinkExecutor	myTestGroupLinkExecutor;
	private TestRunResultWriter 	myTestRunResultWriter;

	/**
	 * @param aTestRunResultWriter 
	 * @param myTestStepExecutor
	 * @param myTestCaseScriptExecutor
	 */
	public TestGroupExecutorImpl(TestStepExecutor aTestStepExecutor,
			TestCaseScriptExecutor aTestCaseScriptExecutor, TestRunResultWriter aTestRunResultWriter)
	{
		myTestStepExecutor = aTestStepExecutor;
		myTestCaseScriptExecutor = aTestCaseScriptExecutor;
		myTestGroupExecutor = this;
		myTestRunResultWriter = aTestRunResultWriter;
	}

	public void setTestGroupLinkExecutor( TestGroupLinkExecutor aTestGroupLinkExecutor )
	{
		myTestGroupLinkExecutor = aTestGroupLinkExecutor;
	}

	@Override
	public void execute( TestGroup aTestGroup,
						 File aScriptDir,
						 File aLogDir,
						 TestGroupResult aResult )
	{
		if ( !aLogDir.isDirectory() )
		{
			FileNotFoundException exc = new FileNotFoundException("Directory does not exist: " + aLogDir.getAbsolutePath());
			throw new IOError( exc );
		}
		Trace.println(Trace.EXEC, "execute( " 
				+ aTestGroup + ", "
	            + aScriptDir.getAbsolutePath() + ", "
	            + aLogDir.getAbsolutePath() + " )", true );

		System.out.println("Executing Test Group: " + aTestGroup.getId());

		File groupLogDir = new File(aLogDir.getAbsolutePath() + File.separator + aTestGroup.getId());
		groupLogDir.mkdir();

    	TestStepArrayList initSteps = aTestGroup.getInitializationSteps();
    	executeInitSteps(initSteps, aResult, aScriptDir, groupLogDir);

    	TestEntryArrayList execSteps = aTestGroup.getExecutionEntries();
		executeExecSteps(execSteps, aResult, aScriptDir, groupLogDir);

    	TestStepArrayList restoreSteps = aTestGroup.getRestoreSteps();
    	executeRestoreSteps(restoreSteps, aResult, aScriptDir, groupLogDir);
	}
	
	public void executeInitSteps(TestStepArrayList anInitSteps, TestGroupResult aResult, File aScriptDir, File aLogDir)
	{
		Trace.println(Trace.EXEC_PLUS, "executeInitSteps( " 
						+ anInitSteps + ", "
						+ aResult + ", "
			            + aLogDir.getAbsolutePath()
			            + " )", true );
		for (int key = 0; key < anInitSteps.size(); key++)
    	{
    		TestStep step = anInitSteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir);
			aResult.addInitialization(tsResult);

			myTestRunResultWriter.intermediateWrite();
    	}
	}

	public void executeExecSteps(TestEntryArrayList anExecEntries, TestGroupResult aResult, File aScriptDir, File aLogDir)
	{
		Trace.println(Trace.EXEC_PLUS, "executeExecSteps( " 
						+ anExecEntries + ", "
						+ aResult + ", "
			            + aScriptDir.getAbsolutePath() + ", "
			            + aLogDir.getAbsolutePath()
			            + " )", true );
		for (int key = 0; key < anExecEntries.size(); key++)
    	{
			TestEntry entry = anExecEntries.get(key);
			try
			{
				if ( entry.getType() == TestEntry.TYPE.Group )
				{
			    	TestGroupResult subTgResult = new TestGroupResult((TestGroup) entry);
			    	aResult.addTestGroup(subTgResult);

					myTestGroupExecutor.execute((TestGroup) entry, aScriptDir, aLogDir, subTgResult);
				}
				else if ( entry.getType() == TestEntry.TYPE.GroupLink )
				{
					if ( myTestGroupLinkExecutor == null )
					{
						throw new Error("No Executor is defined for TestGroupLinks");
					}
			    	TestGroupResult subTgLinkResult = new TestGroupResult((TestGroupLink) entry);
					aResult.addTestGroup(subTgLinkResult);

					myTestGroupLinkExecutor.execute((TestGroupLink) entry, aScriptDir, aLogDir, subTgLinkResult);
				}
				else if ( entry.getType() == TestEntry.TYPE.Case )
				{
			    	TestCaseResult tcResult = new TestCaseResult((TestCase) entry);
					aResult.addTestCase(tcResult);

					myTestCaseExecutor.execute((TestCase) entry, aScriptDir, aLogDir, tcResult);
				}
				else if ( entry.getType() == TestEntry.TYPE.CaseLink )
				{
			    	TestCaseResult tcResult = new TestCaseResult((TestCaseLink) entry);
					aResult.addTestCase(tcResult);

					myTestCaseScriptExecutor.execute((TestCaseLink) entry, aScriptDir, aLogDir, tcResult);
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
				aResult.addComment(message);
				Warning.println(message);
				Trace.print(Trace.ALL, e);
			}

			myTestRunResultWriter.intermediateWrite();
    	}
	}

	public void executeRestoreSteps(TestStepArrayList aRestoreSteps, TestGroupResult aResult, File aScriptDir, File aLogDir)
	{
		Trace.println(Trace.EXEC_PLUS, "executeRestoreSteps( " 
						+ aRestoreSteps + ", "
						+ aResult + ", "
			            + aLogDir.getAbsolutePath()
			            + " )", true );
		for (int key = 0; key < aRestoreSteps.size(); key++)
    	{
    		TestStep step = aRestoreSteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir);
			aResult.addRestore(tsResult);

			myTestRunResultWriter.intermediateWrite();
    	}
	}

	public void setTestCaseExecutor(TestCaseExecutor aTestCaseExecutor)
	{
		myTestCaseExecutor = aTestCaseExecutor;
	}
}
