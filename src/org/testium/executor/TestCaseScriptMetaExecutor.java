package org.testium.executor;

import java.io.File;
import java.util.Hashtable;

import org.testtoolinterfaces.testresult.TestCaseResult;
import org.testtoolinterfaces.testresult.TestResult;
import org.testtoolinterfaces.testresultinterface.TestRunResultWriter;
import org.testtoolinterfaces.testsuite.TestCaseLink;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.Warning;


public class TestCaseScriptMetaExecutor implements TestCaseScriptExecutor
{
	private static String SCRIPT_TYPE = "";
	
	private Hashtable<String, TestCaseScriptExecutor> myExecutors;
	private TestRunResultWriter myTestResultWriter;

	public TestCaseScriptMetaExecutor( Hashtable<String, TestCaseScriptExecutor> aTestCaseScriptExecutors,
									   TestRunResultWriter aTestRunResultWriter )
	{
		myExecutors = aTestCaseScriptExecutors;
		myTestResultWriter = aTestRunResultWriter;
	}

	public void execute( TestCaseLink aTestCaseLink,
						 File aScriptDir,
						 File aLogDir,
						 TestCaseResult aResult )
	{
		if ( myExecutors.containsKey( aTestCaseLink.getScriptType() ) )
		{
			TestCaseScriptExecutor executor = myExecutors.get( aTestCaseLink.getScriptType() );
			executor.execute(aTestCaseLink, aScriptDir, aLogDir, aResult);
		}
		else
		{
			aResult.setResult(TestResult.FAILED);

			String message = "Cannot execute test case scripts of type " + aTestCaseLink.getScriptType() + "\n";
			aResult.addComment(message);
			Warning.println(message);
			Trace.print(Trace.ALL, "Cannot execute " + aTestCaseLink.toString());

	    	myTestResultWriter.intermediateWrite();
		}
	}

	public String getScriptType()
	{
		return SCRIPT_TYPE;
	}
}
