package org.testium.executor;

import java.io.File;
import java.util.Hashtable;

import org.testtoolinterfaces.testresult.TestResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresultinterface.TestRunResultWriter;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.Warning;


public class TestStepMetaExecutor implements TestStepExecutor
{
	private static String COMMAND = "";
	
	private Hashtable<String, TestStepExecutor> myExecutors;
	private TestRunResultWriter myTestResultWriter;

	public TestStepMetaExecutor( Hashtable<String, TestStepExecutor> aTestStepExecutors,
								 TestRunResultWriter aTestRunResultWriter )
	{
		myExecutors = aTestStepExecutors;
		myTestResultWriter = aTestRunResultWriter;
	}

	public TestStepResult execute(TestStep aStep, File aScriptDir, File aLogDir)
	{
		TestStepResult result;
		if ( myExecutors.containsKey( aStep.getCommand() ) )
		{
			TestStepExecutor executor = myExecutors.get( aStep.getCommand() );
			result = executor.execute(aStep, aScriptDir, aLogDir);
		}
		else
		{
			result = new TestStepResult( aStep );
			
			if ( aStep.getActionType().equals(TestStep.ActionType.check))
			{
				result.setResult(TestResult.FAILED);
			}
			else
			{
				result.setResult(TestResult.ERROR);
			}
			String message = "Cannot execute steps with command '" + aStep.getCommand() + "'\n"
				+ "Trying to continue, but this may affect further execution...";
			result.addComment(message);
			Warning.println(message);
			Trace.println(Trace.ALL, "Cannot execute " + aStep.toString());
		}

		myTestResultWriter.intermediateWrite();

		return result;
	}

	public String getCommand()
	{
		return COMMAND;
	}
}
