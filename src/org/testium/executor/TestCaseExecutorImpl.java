package org.testium.executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.util.ArrayList;

import org.testtoolinterfaces.testresult.TestCaseResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.TestCase;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.utils.Trace;


public class TestCaseExecutorImpl implements TestCaseExecutor
{
	private TestStepExecutor		myTestStepExecutor;

	/**
	 * @param myTestStepExecutor
	 * @param myTestCaseExecutor
	 * @param myTestCaseScriptExecutor
	 * @param myTestGroupLinkExecutor
	 */
	public TestCaseExecutorImpl(TestStepExecutor aTestStepExecutor)
	{
		myTestStepExecutor = aTestStepExecutor;
	}

	public TestCaseResult execute(TestCase aTestCase, File aScriptDir, File aLogDir)
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

		System.out.println("Executing Test Group: " + aTestCase.getId());

		File groupLogDir = new File(aLogDir.getAbsolutePath() + File.separator + aTestCase.getId());
		groupLogDir.mkdir();

    	TestCaseResult result = new TestCaseResult(aTestCase);
    	
    	ArrayList<TestStep> initSteps = aTestCase.getInitializationSteps();
    	executeInitSteps(initSteps, result, aScriptDir, aLogDir);

    	ArrayList<TestStep> execSteps = aTestCase.getExecutionSteps();
    	executeExecSteps(execSteps, result, aScriptDir, aLogDir);

    	ArrayList<TestStep> restoreSteps = aTestCase.getRestoreSteps();
    	executeRestoreSteps(restoreSteps, result, aScriptDir, aLogDir);
    	
		return result;
	}
	
	public void executeInitSteps(ArrayList<TestStep> anInitSteps, TestCaseResult aResult, File aScriptDir, File aLogDir)
	{
		for (int key = 0; key < anInitSteps.size(); key++)
    	{
    		TestStep step = anInitSteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir);
			aResult.addInitialization(tsResult);

//			String message = "Initialization Step " + step.getId() + " failed:\n"
//					+ e.getMessage()
//					+ "\nTrying to continue, but this will probably affect further execution...";
//				aResult.addComment(message);
//				Warning.println(message);
//				Trace.printException(Trace.LEVEL.ALL, e);
    	}
	}

	public void executeExecSteps(ArrayList<TestStep> anExecSteps, TestCaseResult aResult, File aScriptDir, File aLogDir)
	{
		for (int key = 0; key < anExecSteps.size(); key++)
    	{
			TestStep step = anExecSteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir);
			aResult.addExecution(tsResult);
    	}
	}

	public void executeRestoreSteps(ArrayList<TestStep> aRestoreSteps, TestCaseResult aResult, File aScriptDir, File aLogDir)
	{
		for (int key = 0; key < aRestoreSteps.size(); key++)
    	{
    		TestStep step = aRestoreSteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir);
			aResult.addRestore(tsResult);

//			String message = "Restore Step " + step.getId() + " failed:\n"
//					+ e.getMessage()
//					+ "\nTrying to continue, but this will probably affect further execution...";
//				aResult.addComment(message);
//				Warning.println(message);
//				Trace.printException(Trace.LEVEL.ALL, e);
    	}
	}
}
