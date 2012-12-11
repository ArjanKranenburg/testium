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
//		if ( aStep.getClass().equals(TestStepSequence.class) )
//		{
////			return mySetExecutor.execute(aStep, aScriptDir, aLogDir);
//			TestStepResultList subStepResults = new TestStepResultList();
//			return mySetExecutor.execute_alt(aStep, subStepResults, aScriptDir, aLogDir, aRTData);
//		}// else
		
		if ( aStep instanceof TestStepScript )
		{
			return executeScript( (TestStepScript) aStep, aScriptDir, aLogDir);
		}//else

		if ( aStep instanceof TestStepCommand )
		{
			return executeCommand( (TestStepCommand) aStep, aRTData, aLogDir);
		}//else

		if ( aStep instanceof TestStepSelection ) {
			return executeSelection( (TestStepSelection) aStep, aScriptDir, aLogDir, aRTData);
		}//else
		
		throw new Error( "Don't know how to execute " + aStep.getClass().getSimpleName() );
	}

	/**
	 * @param aStepScript
	 * @param aScriptDir
	 * @param aLogDir
	 * @return
	 */
	private TestStepResult executeScript( TestStepScript aStepScript,
	                                      File aScriptDir,
	                                      File aLogDir )
	{
		TestStepResult result;

		if ( myScriptExecutors.containsKey( aStepScript.getScriptType() ) )
		{
			TestStepScriptExecutor executor = myScriptExecutors.get( aStepScript.getScriptType() );
			result = executor.execute(aStepScript, aScriptDir, aLogDir);
		}
		else
		{
			String message = "Cannot execute step scripts of type '" + aStepScript.getScriptType() + "'\n"
			+ "Trying to continue, but this may affect further execution...";

			result = reportError(aStepScript, message);
		}
		return result;
	}

	/**
	 * @param aStep
	 * @param aLogDir
	 * @return
	 */
	private TestStepResult executeCommand( TestStepCommand aStepCommand,
	                                       RunTimeData aRtData,
	                                       File aLogDir )
	{
		TestStepResult result;
		String command = aStepCommand.getCommand();

		String errorMsg = "Cannot execute steps with command '" + aStepCommand.getCommand() + "'\n";

		SutInterface iface = (SutInterface) aStepCommand.getInterface();
		if ( iface == null || ! iface.hasCommand(command) )
		{
			result = reportError(aStepCommand, errorMsg);
		}
		else
		{
			TestStepCommandExecutor executor = iface.getCommandExecutor(command);
			try
			{
				result = executor.execute(aStepCommand, aRtData, aLogDir);
			}
			catch (TestSuiteException tse)
			{
				String message = errorMsg + tse.getMessage();
				result = reportError(aStepCommand, message);
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
		if ( ifResult.getResult().equals(VERDICT.ERROR)
			 || ifResult.getResult().equals(VERDICT.UNKNOWN) ) {
			 // NOP, we don't execute the then or else!
			
		} else {
			if ( ifResult.getResult().equals( negator ? VERDICT.FAILED : VERDICT.PASSED) ) {
				TestStepSequence thenSteps = selectionStep.getThenSteps();
				this.mySetExecutor.execute(thenSteps, subStepResults, aScriptDir, aLogDir, aRTData);

			} else {
				TestStepSequence elseSteps = selectionStep.getElseSteps();
				this.mySetExecutor.execute(elseSteps, subStepResults, aScriptDir, aLogDir, aRTData);
			}

			VERDICT origVerdict = ifResult.getResult();
			String origComment = ifResult.getComment();
			ifResult = cloneResultWithoutVerdict(ifStep, ifResult);
			ifResult.setResult( VERDICT.PASSED );
			
			String comment = "";
			if (origVerdict.equals(VERDICT.FAILED)){
				comment = "Result set to passed. ";
			}
			if ( ! origComment.isEmpty() ) {
				comment += "Comment was: " + origComment;
			}
			
			ifResult.setComment(comment);
		}

		result.addSubStep( ifResult );

		Iterator<TestStepResult> subResultItr = subStepResults.iterator();
		while ( subResultItr.hasNext() ) {
			result.addSubStep( subResultItr.next() );
		}
		
		return result;
	}

	/**
	 * @param ifStep
	 * @param ifOriginalResult
	 * @return
	 */
	private TestStepResult cloneResultWithoutVerdict( TestStep ifStep,
													  TestStepResult originalIfResult) {
		TestStepResult result = new TestStepResult(ifStep);
		result.addComment( originalIfResult.getComment() );
		result.setDisplayName( originalIfResult.getDisplayName() );
		result.setParameterResults(originalIfResult.getParameterResults());
		
		Iterator<TestStepResult> subResultItr = originalIfResult.getSubSteps().iterator();
		while ( subResultItr.hasNext() ) {
			result.addSubStep( subResultItr.next() );
		}

		Hashtable<String, String> logs = originalIfResult.getLogs();
		Iterator<String> logKeys = logs.keySet().iterator();
		while ( logKeys.hasNext() ) {
			String key = logKeys.next();
			result.addTestLog( key, logs.get(key) );
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
