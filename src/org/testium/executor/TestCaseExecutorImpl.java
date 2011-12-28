package org.testium.executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.util.ArrayList;
import java.util.Hashtable;

import org.testtoolinterfaces.testresult.TestCaseResult;
import org.testtoolinterfaces.testresult.TestCaseResultLink;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testresultinterface.TestCaseResultWriter;
import org.testtoolinterfaces.testsuite.TestCase;
import org.testtoolinterfaces.testsuite.TestCaseImpl;
import org.testtoolinterfaces.testsuite.TestCaseLink;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestStepArrayList;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.testsuiteinterface.TestCaseReader;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;


public class TestCaseExecutorImpl implements TestCaseExecutor
{
	private static final String		TYPE = "tti";
	
	private TestStepMetaExecutor	myTestStepExecutor;
	private TestCaseReader			myTestCaseReader;

	private TestCaseResultWriter myTestCaseResultWriter;

	public TestCaseExecutorImpl( TestStepMetaExecutor aTestStepExecutor,
	                             TestCaseResultWriter aTcResultWriter )
	{
		Trace.println(Trace.CONSTRUCTOR);

		myTestStepExecutor = aTestStepExecutor;
		myTestCaseResultWriter = aTcResultWriter;

		myTestCaseReader = new TestCaseReader( myTestStepExecutor.getInterfaces(), true );
	}

	@Override
	public TestCaseResultLink execute(TestCaseLink aTestCaseLink, File aLogDir, RunTimeData aRTData)
	{
		if ( !aLogDir.isDirectory() )
		{
			FileNotFoundException exc = new FileNotFoundException("Directory does not exist: " + aLogDir.getAbsolutePath());
			throw new IOError( exc );
		}
		
		Trace.println(Trace.EXEC, "execute( " 
				+ aTestCaseLink.getId() + ", "
	            + aLogDir.getPath() + " )", true );


		File testCaseFile = aTestCaseLink.getLink();
		File scriptDir = testCaseFile.getParentFile();

		TestCase testCase;
		TestCaseResult result;
		
    	try
		{
			testCase = myTestCaseReader.readTcFile(testCaseFile);
			result = new TestCaseResult( testCase );
		}
		catch (TestSuiteException e)
		{
			testCase = new TestCaseImpl( aTestCaseLink.getId(),
			                                      new Hashtable<String, String>(),
			                                      aTestCaseLink.getDescription(), 
			                                      new ArrayList<String>(),
			                                      new TestStepArrayList(),
			                                      new TestStepArrayList(),
			                                      new TestStepArrayList(),
			                                      new Hashtable<String, String>() );
			
			result = new TestCaseResult( testCase );
    		result.addComment( e.getLocalizedMessage() );
    		result.addComment( e.getStackTrace().toString() );
			result.setResult(VERDICT.ERROR);
		}
		
		File logFile = new File(aLogDir, testCase.getId() + "_log.xml");
		myTestCaseResultWriter.write(result, logFile);

    	ArrayList<TestStep> prepareSteps = testCase.getPrepareSteps();
    	executePrepareSteps(prepareSteps, result, scriptDir, aLogDir, aRTData);

    	ArrayList<TestStep> execSteps = testCase.getExecutionSteps();
    	executeExecSteps(execSteps, result, scriptDir, aLogDir, aRTData);

    	ArrayList<TestStep> restoreSteps = testCase.getRestoreSteps();
    	executeRestoreSteps(restoreSteps, result, scriptDir, aLogDir, aRTData);

		TestCaseResultLink tcResultLink = new TestCaseResultLink( aTestCaseLink,
    	                                       result.getResult(),
    	                                       logFile );

    	tcResultLink.addComment( result.getComment() );

    	return tcResultLink;
	}

	public void executePrepareSteps( ArrayList<TestStep> anPrepareSteps,
	                                 TestCaseResult aResult,
	                                 File aScriptDir,
	                                 File aLogDir,
	                                 RunTimeData aRTData )
	{
		for (int key = 0; key < anPrepareSteps.size(); key++)
    	{
    		TestStep step = anPrepareSteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir, aRTData);
			aResult.addInitialization(tsResult);
    	}
	}

	public void executeExecSteps( ArrayList<TestStep> anExecSteps,
	                              TestCaseResult aResult,
	                              File aScriptDir,
	                              File aLogDir,
	                              RunTimeData aRTData )
	{
		for (int key = 0; key < anExecSteps.size(); key++)
    	{
			TestStep step = anExecSteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir, aRTData);
			aResult.addExecution(tsResult);
			
			if ( aResult.getResult().equals(VERDICT.ERROR) )
			{
				return;
			}
    	}
	}

	public void executeRestoreSteps( ArrayList<TestStep> aRestoreSteps,
	                                 TestCaseResult aResult,
	                                 File aScriptDir,
	                                 File aLogDir,
	                                 RunTimeData aRTData )
	{
		for (int key = 0; key < aRestoreSteps.size(); key++)
    	{
    		TestStep step = aRestoreSteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir, aRTData);
			aResult.addRestore(tsResult);
    	}
	}

	@Override
	public String getType()
	{
		return TYPE;
	}
}
