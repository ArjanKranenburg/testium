package org.testium.executor;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.testtoolinterfaces.testresult.TestResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestStepScript;
import org.testtoolinterfaces.testsuite.TestStepCommand;
import org.testtoolinterfaces.testsuite.TestStep.StepType;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.Warning;


public class TestStepMetaExecutor
{
	private Hashtable<String, TestStepCommandExecutor> myCommandExecutors;
	private Hashtable<String, TestStepScriptExecutor> myScriptExecutors;
	private TestStepSetExecutor mySetExecutor;

	public TestStepMetaExecutor()
	{
		Trace.println( Trace.CONSTRUCTOR );

		myCommandExecutors = new Hashtable<String, TestStepCommandExecutor>();
		myScriptExecutors = new Hashtable<String, TestStepScriptExecutor>();
		mySetExecutor = new TestStepSetExecutor();
	}

	public TestStepResult execute(TestStep aStep, File aScriptDir, File aLogDir)
	{
		if ( aStep.getStepType().equals( StepType.set ) )
		{
			return mySetExecutor.execute(aStep, aScriptDir, aLogDir);
		}// else
		
		if ( aStep.getClass().equals(TestStepScript.class) )
		{
			return executeScript(aStep, aScriptDir, aLogDir);
		}//else
		else if ( aStep.getClass().equals(TestStepCommand.class) )
		{
			return executeCommand(aStep, aScriptDir, aLogDir);
		}//else

		throw new Error( "Don't know how to execute " + aStep.getClass().getSimpleName() );
	}

	/**
	 * @param aStep
	 * @param aScriptDir
	 * @param aLogDir
	 * @return
	 */
	private TestStepResult executeScript( TestStep aStep,
	                                      File aScriptDir,
	                                      File aLogDir )
	{
		TestStepResult result;
		TestStepScript step = (TestStepScript) aStep;

		if ( myScriptExecutors.containsKey( step.getScriptType() ) )
		{
			TestStepScriptExecutor executor = myScriptExecutors.get( step.getScriptType() );
			result = executor.execute(step, aScriptDir, aLogDir);
		}
		else
		{
			result = new TestStepResult( step );
			
			if ( aStep.getStepType().equals(TestStep.StepType.check) )
			{
				result.setResult(TestResult.FAILED);
			}
			else
			{
				result.setResult(TestResult.ERROR);
			}
			String message = "Cannot execute step scripts of type '" + step.getScriptType() + "'\n"
				+ "Trying to continue, but this may affect further execution...";
			result.addComment(message);
			Warning.println(message);
			Trace.println(Trace.ALL, "Cannot execute " + aStep.toString());
		}
		return result;
	}

	/**
	 * @param aStep
	 * @param aScriptDir
	 * @param aLogDir
	 * @return
	 */
	private TestStepResult executeCommand(TestStep aStep, File aScriptDir,
											File aLogDir)
	{
		TestStepResult result;
		TestStepCommand step = (TestStepCommand) aStep;

		if ( myCommandExecutors.containsKey( step.getCommand() ) )
		{
			TestStepCommandExecutor executor = myCommandExecutors.get( step.getCommand() );
			result = executor.execute(step, aScriptDir, aLogDir);
		}
		else
		{
			result = new TestStepResult( step );
			
			if ( aStep.getStepType().equals(TestStep.StepType.check) )
			{
				result.setResult(TestResult.FAILED);
			}
			else
			{
				result.setResult(TestResult.ERROR);
			}
			String message = "Cannot execute steps with command '" + step.getCommand() + "'\n"
				+ "Trying to continue, but this may affect further execution...";
			result.addComment(message);
			Warning.println(message);
			Trace.println(Trace.ALL, "Cannot execute " + aStep.toString());
		}
		return result;
	}

	public void addCommandExecutor(TestStepCommandExecutor aTestStepExecutor)
	{
		myCommandExecutors.put(aTestStepExecutor.getCommand(), aTestStepExecutor);		
	}

	public void addScriptExecutor(TestStepScriptExecutor aTestStepExecutor)
	{
		myScriptExecutors.put(aTestStepExecutor.getType(), aTestStepExecutor);		
	}
	
	public ArrayList<String> getKeywords()
	{
		
		ArrayList<String> keywords = new ArrayList<String>();
		
	    for (Enumeration<String> commands = myCommandExecutors.keys(); commands.hasMoreElements();)
	    {
	    	keywords.add( commands.nextElement() );
	    }

		return keywords;
	}
}
