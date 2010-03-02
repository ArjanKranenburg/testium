package org.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.TestStep;


public class TestStepWaitExecutor implements TestStepExecutor
{
	private static String COMMAND = "wait";
	
	public TestStepResult execute(TestStep aStep, File aScriptDir, File aLogDir)
	{
		TestStepResult result = new TestStepResult( aStep );
		
//		int time = aStep.getParameters().get("TIME");
		int time = 3; // TODO add parameters to steps and use the time as time-to-wait
	    long sleeptime = new Long(time * 1000);
		try
		{
			Thread.sleep( sleeptime );
		}
		catch (InterruptedException e)
		{
			throw new Error( e );
		}
		result.setResult(TestResult.PASSED);
		
		return result;
	}

	public String getCommand()
	{
		return COMMAND;
	}
}
