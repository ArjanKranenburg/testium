package org.testium.executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.util.ArrayList;

import org.testtoolinterfaces.testresult.TestCaseResult;
import org.testtoolinterfaces.testresult.TestCaseResultLink;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresultinterface.TestCaseResultWriter;
import org.testtoolinterfaces.testsuite.TestCase;
import org.testtoolinterfaces.testsuite.TestCaseLink;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuiteinterface.TestCaseReader;
import org.testtoolinterfaces.utils.Trace;


public class TestCaseExecutorImpl implements TestCaseExecutor
{
	private static final String		TYPE = "TTI";
	
	private TestStepMetaExecutor	myTestStepExecutor;
	private TestCaseReader			myTestCaseReader;

	private TestCaseResultWriter myTestCaseResultWriter;

	public TestCaseExecutorImpl( TestStepMetaExecutor aTestStepExecutor, TestCaseResultWriter aTcResultWriter )
	{
		Trace.println(Trace.CONSTRUCTOR);

		myTestStepExecutor = aTestStepExecutor;
		myTestCaseResultWriter = aTcResultWriter;

		myTestCaseReader = new TestCaseReader();
	}

	@Override
	public TestCaseResultLink execute(TestCaseLink aTestCaseLink, File aLogDir)
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
		TestCase testCase = myTestCaseReader.readTcFile(testCaseFile);
		TestCaseResult result = new TestCaseResult( testCase );

		File logFile = new File(aLogDir, testCase.getId() + "_log.xml");
		myTestCaseResultWriter.write(result, logFile);

    	ArrayList<TestStep> prepareSteps = testCase.getPrepareSteps();
    	executePrepareSteps(prepareSteps, result, scriptDir, aLogDir);

    	ArrayList<TestStep> execSteps = testCase.getExecutionSteps();
    	executeExecSteps(execSteps, result, scriptDir, aLogDir);

    	ArrayList<TestStep> restoreSteps = testCase.getRestoreSteps();
    	executeRestoreSteps(restoreSteps, result, scriptDir, aLogDir);

//    	myTestCaseResultWriter.update( result );
		
    	TestCaseResultLink tcResultLink = new TestCaseResultLink( aTestCaseLink,
    	                                                          result.getResult(),
    	                                                          logFile );

    	return tcResultLink;
	}

	public void executePrepareSteps(ArrayList<TestStep> anPrepareSteps, TestCaseResult aResult, File aScriptDir, File aLogDir)
	{
		for (int key = 0; key < anPrepareSteps.size(); key++)
    	{
    		TestStep step = anPrepareSteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir);
			aResult.addInitialization(tsResult);

//	    	myTestCaseResultWriter.update( aResult );
    	}
	}

	public void executeExecSteps(ArrayList<TestStep> anExecSteps, TestCaseResult aResult, File aScriptDir, File aLogDir)
	{
		for (int key = 0; key < anExecSteps.size(); key++)
    	{
			TestStep step = anExecSteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir);
			aResult.addExecution(tsResult);

//	    	myTestCaseResultWriter.update( aResult );
    	}
	}

	public void executeRestoreSteps(ArrayList<TestStep> aRestoreSteps, TestCaseResult aResult, File aScriptDir, File aLogDir)
	{
		for (int key = 0; key < aRestoreSteps.size(); key++)
    	{
    		TestStep step = aRestoreSteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir);
			aResult.addRestore(tsResult);

//	    	myTestCaseResultWriter.update( aResult );
    	}
	}

	@Override
	public String getType()
	{
		return TYPE;
	}
}
