package net.sf.testium.executor;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;

import net.sf.testium.systemundertest.SutInterface;

import org.testtoolinterfaces.testresult.TestResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testresult.TestStepResultList;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestStepScript;
import org.testtoolinterfaces.testsuite.TestStepCommand;
import org.testtoolinterfaces.testsuite.TestStepSelection;
import org.testtoolinterfaces.testsuite.TestStepSequence;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.Warning;


public class TestStepMetaExecutor
{
	private SupportedInterfaceList mySutInterfaces;
	private Hashtable<String, TestStepScriptExecutor> myScriptExecutors;
	private TestStepSetExecutor mySetExecutor;

	public TestStepMetaExecutor()
	{
		Trace.println( Trace.CONSTRUCTOR );

		mySutInterfaces = new SupportedInterfaceList();
		myScriptExecutors = new Hashtable<String, TestStepScriptExecutor>();
		mySetExecutor = new TestStepSetExecutor(this);
	}

	public TestStepResult execute(TestStep aStep, File aScriptDir, File aLogDir, RunTimeData aRTData)
	{
		if ( aStep.getClass().equals(TestStepSequence.class) )
		{
			return mySetExecutor.execute(aStep, aScriptDir, aLogDir);
		}// else
		
		if ( aStep.getClass().equals(TestStepScript.class) )
		{
			return executeScript(aStep, aScriptDir, aLogDir);
		}//else

		if ( aStep.getClass().equals(TestStepCommand.class) )
		{
			return executeCommand(aStep, aRTData, aLogDir);
		}//else

		if ( aStep instanceof TestStepSelection ) {
			return executeSelection( (TestStepSelection) aStep, aScriptDir, aLogDir, aRTData);
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
			String message = "Cannot execute step scripts of type '" + step.getScriptType() + "'\n"
			+ "Trying to continue, but this may affect further execution...";

			result = reportError(step, message);
		}
		return result;
	}

	/**
	 * @param aStep
	 * @param aLogDir
	 * @return
	 */
	private TestStepResult executeCommand( TestStep aStep,
	                                       RunTimeData aRtData,
	                                       File aLogDir )
	{
		TestStepResult result;
		TestStepCommand step = (TestStepCommand) aStep;
		String command = step.getCommand();

		String errorMsg = "Cannot execute steps with command '" + step.getCommand() + "'\n";

		SutInterface iface = (SutInterface) step.getInterface();
		if ( iface == null || ! iface.hasCommand(command) )
		{
			result = reportError(step, errorMsg);
		}
		else
		{
			TestStepCommandExecutor executor = iface.getCommandExecutor(command);
			try
			{
				result = executor.execute(step, aRtData, aLogDir);
			}
			catch (TestSuiteException tse)
			{
				String message = errorMsg + tse.getMessage();
				result = reportError(step, message);
			}
		}

		return result;
	}
	
	private TestStepResult executeSelection( TestStepSelection selectionStep, File aScriptDir, File aLogDir, RunTimeData aRTData ) {
		TestStepResult result = new TestStepResult(selectionStep);

		TestStep ifStep = selectionStep.getIfStep();
		boolean negator = selectionStep.getNegator();
		TestStepResult ifResult = this.execute(ifStep, aScriptDir, aLogDir, aRTData);

		TestStepResultList subStepResults = new TestStepResultList();
// TODO: If error or unknown, we break!
		if ( ifResult.getResult().equals( negator ? VERDICT.FAILED : VERDICT.PASSED) ) {
			TestStepSequence thenSteps = selectionStep.getThenSteps();
			this.mySetExecutor.execute_alt(thenSteps, subStepResults, aScriptDir, aLogDir, aRTData);
		} else {
			TestStepSequence elseSteps = selectionStep.getElseSteps();
			this.mySetExecutor.execute_alt(elseSteps, subStepResults, aScriptDir, aLogDir, aRTData);
		}
		
		Iterator<TestStepResult> subResultItr = subStepResults.iterator();
		while ( subResultItr.hasNext() ) {
			result.addSubStep( subResultItr.next() );
		}
		
		return result;
	}

	public void addSutInterface(SutInterface aSutInterface)
	{
		mySutInterfaces.add(aSutInterface);
	}

	public void addScriptExecutor(TestStepScriptExecutor aTestStepExecutor)
	{
		myScriptExecutors.put(aTestStepExecutor.getType(), aTestStepExecutor);		
	}
	
	public SupportedInterfaceList getInterfaces()
	{
		return mySutInterfaces;
	}

	/**
	 * @param step
	 * @param message
	 * @return
	 */
	private TestStepResult reportError(TestStep aStep, String message)
	{
		TestStepResult result = new TestStepResult( aStep );
		result.setResult(TestResult.ERROR);
		result.addComment(message);

		Warning.println(message);
		Trace.println(Trace.ALL, "Cannot execute " + aStep.toString());
		return result;
	}
}
