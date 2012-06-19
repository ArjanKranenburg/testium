/**
 * 
 */
package net.sf.testium.systemundertest;

import java.io.File;

import net.sf.testium.executor.TestStepCommandExecutor;

import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;


/**
 * @author arjan.kranenburg
 *
 * Simple class for starting the System Under Test.
 */
public final class DummySutCommand implements TestStepCommandExecutor
{
	private String myAction;

	/**
	 * @param sutControl
	 */
	public DummySutCommand( String anAction )
	{
		myAction = anAction;
	}

	public TestStepResult execute( TestStep aStep,
	                               RunTimeData aVariables,
	                               File aLogDir )
	{
		Trace.println( Trace.EXEC );
		TestStepResult result = new TestStepResult( aStep );
		result.setResult(VERDICT.PASSED);

		return result;
	}

	public String getCommand()
	{
		return myAction;
	}

	public boolean verifyParameters( ParameterArrayList aParameters )
	{
		Trace.println( Trace.EXEC_PLUS );
		return true;
	}
}
