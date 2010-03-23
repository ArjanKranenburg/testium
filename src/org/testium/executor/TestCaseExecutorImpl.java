package org.testium.executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.util.ArrayList;

import org.testtoolinterfaces.testresult.TestCaseResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresultinterface.TestRunResultWriter;
import org.testtoolinterfaces.testsuite.TestCase;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.utils.Trace;


public class TestCaseExecutorImpl implements TestCaseExecutor
{
	private TestStepExecutor		myTestStepExecutor;
	private TestRunResultWriter 	myTestRunResultWriter;

	/**
	 * @param aTestStepExecutor
	 * @param aTestRunResultWriter 
	 */
	public TestCaseExecutorImpl( TestStepExecutor aTestStepExecutor,
								 TestRunResultWriter aTestRunResultWriter )
	{
		myTestStepExecutor = aTestStepExecutor;
		myTestRunResultWriter = aTestRunResultWriter;
	}

	public void execute( TestCase aTestCase,
						 File aScriptDir,
						 File aLogDir,
						 TestCaseResult aResult )
	{
		if ( !aLogDir.isDirectory() )
		{
			FileNotFoundException exc = new FileNotFoundException("Directory does not exist: " + aLogDir.getAbsolutePath());
			throw new IOError( exc );
		}
		Trace.println(Trace.LEVEL.EXEC, "execute( " 
				+ aTestCase
	            + aLogDir.getAbsolutePath()
	            + " )", true );

		File groupLogDir = new File(aLogDir.getAbsolutePath() + File.separator + aTestCase.getId());
		groupLogDir.mkdir();

    	ArrayList<TestStep> initSteps = aTestCase.getInitializationSteps();
    	executeInitSteps(initSteps, aResult, aScriptDir, aLogDir);

    	ArrayList<TestStep> execSteps = aTestCase.getExecutionSteps();
    	executeExecSteps(execSteps, aResult, aScriptDir, aLogDir);

    	ArrayList<TestStep> restoreSteps = aTestCase.getRestoreSteps();
    	executeRestoreSteps(restoreSteps, aResult, aScriptDir, aLogDir);

    	// We write intermediate results after each Test Case, not each Test Step
		myTestRunResultWriter.intermediateWrite();
	}
	
	public void executeInitSteps(ArrayList<TestStep> anInitSteps, TestCaseResult aResult, File aScriptDir, File aLogDir)
	{
		for (int key = 0; key < anInitSteps.size(); key++)
    	{
    		TestStep step = anInitSteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir);
			aResult.addInitialization(tsResult);

			// Enable this if we want intermediate results written after each Test Step
			// myTestRunResultWriter.intermediateWrite();
    	}
	}

	public void executeExecSteps(ArrayList<TestStep> anExecSteps, TestCaseResult aResult, File aScriptDir, File aLogDir)
	{
		for (int key = 0; key < anExecSteps.size(); key++)
    	{
			TestStep step = anExecSteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir);
			aResult.addExecution(tsResult);

			// Enable this if we want intermediate results written after each Test Step
			// myTestRunResultWriter.intermediateWrite();
    	}
	}

	public void executeRestoreSteps(ArrayList<TestStep> aRestoreSteps, TestCaseResult aResult, File aScriptDir, File aLogDir)
	{
		for (int key = 0; key < aRestoreSteps.size(); key++)
    	{
    		TestStep step = aRestoreSteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir);
			aResult.addRestore(tsResult);

			// Enable this if we want intermediate results written after each Test Step
			// myTestRunResultWriter.intermediateWrite();
    	}
	}
}
