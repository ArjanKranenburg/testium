package net.sf.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testsuite.TestStepScript;
import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan Kranenburg
 *
 */
public abstract class TestStepScriptAbstractExecutor implements TestStepScriptExecutor
{
	abstract public File executeScript( TestStepScript aStep,
	                                    File aShellScript,
	                                    TestStepResult aResult,
	                                    File aLogDir );

	/* (non-Javadoc)
	 * @see net.sf.testium.Executor.TestStepExecutor#execute(org.testtoolinterfaces.testsuite.TestStepScript, java.io.File, java.io.File)
	 */
	public TestStepResult execute( TestStepScript aStep, File aScriptDir, File aLogDir )
	{
		TestStepResult result = new TestStepResult( aStep );
		if ( !aScriptDir.isDirectory() )
		{
			result.addComment( "Execution Failed: Script Directory does not exist: " + aScriptDir.getAbsolutePath() );
			result.setResult(VERDICT.ERROR);
			return result;
		}

		if ( !aLogDir.isDirectory() )
		{
			result.addComment( "Execution Failed: Log Directory does not exist: " + aLogDir.getAbsolutePath() );
			result.setResult(VERDICT.ERROR);
			return result;
		}

		Trace.println(Trace.EXEC_PLUS, "execute( "
				+ aStep.getDisplayName() + ", "
				+ aScriptDir.getAbsolutePath() + ", "
	            + aLogDir.getAbsolutePath() + " )", true );

    	File tsShellScript = new File( aStep.getScript() );
    	if ( ! tsShellScript.isAbsolute() )
    	{
    		tsShellScript = new File(aScriptDir, aStep.getScript() );
    	}

    	executeScript(aStep, tsShellScript, result, aLogDir);

		return result;
	}
}
