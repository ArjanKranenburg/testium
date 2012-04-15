package org.testium.executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.testtoolinterfaces.testresult.TestCaseResult;
import org.testtoolinterfaces.testresult.TestCaseResultLink;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testresultinterface.TestCaseResultWriter;
import org.testtoolinterfaces.testsuite.TestCase;
import org.testtoolinterfaces.testsuite.TestCaseImpl;
import org.testtoolinterfaces.testsuite.TestCaseLink;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestStepSequence;
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
                                         aTestCaseLink.getDescription(),
	                                     0,
	                                     new ArrayList<String>(),
	                                     new TestStepSequence(),
	                                     new TestStepSequence(),
	                                     new TestStepSequence(),
	                                     new Hashtable<String, String>(),
	                                     new Hashtable<String, String>() );
			
			result = new TestCaseResult( testCase );
    		result.addComment( e.getLocalizedMessage() );
    		result.addComment( e.getStackTrace().toString() );
			result.setResult(VERDICT.ERROR);
		}
		
		File logFile = new File(aLogDir, testCase.getId() + "_log.xml");
		myTestCaseResultWriter.write(result, logFile);

    	TestStepSequence prepareSteps = testCase.getPrepareSteps();
    	executePrepareSteps(prepareSteps, result, scriptDir, aLogDir, aRTData);

    	TestStepSequence execSteps = testCase.getExecutionSteps();
    	executeExecSteps(execSteps, result, scriptDir, aLogDir, aRTData);

    	TestStepSequence restoreSteps = testCase.getRestoreSteps();
    	executeRestoreSteps(restoreSteps, result, scriptDir, aLogDir, aRTData);

		TestCaseResultLink tcResultLink = new TestCaseResultLink( aTestCaseLink,
    	                                       result.getResult(),
    	                                       logFile );

    	tcResultLink.addComment( result.getComment() );

    	return tcResultLink;
	}

	public void executePrepareSteps( TestStepSequence aPrepareSteps,
	                                 TestCaseResult aResult,
	                                 File aScriptDir,
	                                 File aLogDir,
	                                 RunTimeData aRTData )
	{
		Iterator<TestStep> stepsItr = aPrepareSteps.iterator();
		while(stepsItr.hasNext())
		{
		    TestStep step = stepsItr.next();
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir, aRTData);
			aResult.addInitialization(tsResult);
    	}
	}

	public void executeExecSteps( TestStepSequence anExecSteps,
	                              TestCaseResult aResult,
	                              File aScriptDir,
	                              File aLogDir,
	                              RunTimeData aRTData )
	{
		Iterator<TestStep> stepsItr = anExecSteps.iterator();
		while(stepsItr.hasNext())
		{
		    TestStep step = stepsItr.next();
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir, aRTData);
			aResult.addExecution(tsResult);
			
			if ( aResult.getResult().equals(VERDICT.ERROR) )
			{
				return;
			}
    	}
	}

	public void executeRestoreSteps( TestStepSequence aRestoreSteps,
	                                 TestCaseResult aResult,
	                                 File aScriptDir,
	                                 File aLogDir,
	                                 RunTimeData aRTData )
	{
		Iterator<TestStep> stepsItr = aRestoreSteps.iterator();
		while(stepsItr.hasNext())
		{
		    TestStep step = stepsItr.next();
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
