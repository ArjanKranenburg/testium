package org.testium.executor;

import java.io.File;
import java.util.Hashtable;

import org.testtoolinterfaces.testresult.TestCaseResult;
import org.testtoolinterfaces.testresult.TestResult;
import org.testtoolinterfaces.testsuite.TestCaseLink;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.Warning;


public class TestCaseScriptMetaExecutor implements TestCaseScriptExecutor
{
	private static String SCRIPT_TYPE = "";
	
	private Hashtable<String, TestCaseScriptExecutor> myExecutors;

	public TestCaseScriptMetaExecutor( Hashtable<String, TestCaseScriptExecutor> aTestCaseScriptExecutors )
	{
		myExecutors = aTestCaseScriptExecutors;
	}

	public TestCaseResult execute(TestCaseLink aTestCaseLink, File aScriptDir, File aLogDir)
	{
		TestCaseResult result;
		if ( myExecutors.containsKey( aTestCaseLink.getScriptType() ) )
		{
			TestCaseScriptExecutor executor = myExecutors.get( aTestCaseLink.getScriptType() );
			result = executor.execute(aTestCaseLink, aScriptDir, aLogDir);
		}
		else
		{
			result = new TestCaseResult( aTestCaseLink );
			result.setResult(TestResult.FAILED);

			String message = "Cannot execute test case scripts of type " + aTestCaseLink.getScriptType() + "\n";
			result.addComment(message);
			Warning.println(message);
			Trace.print(Trace.ALL, "Cannot execute " + aTestCaseLink.toString());
		}
		
		return result;
	}

	public String getScriptType()
	{
		return SCRIPT_TYPE;
	}
}
