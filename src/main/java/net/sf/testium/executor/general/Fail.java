/**
 * 
 */
package net.sf.testium.executor.general;

import java.util.ArrayList;

import net.sf.testium.systemundertest.SutInterface;

import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Sets a variable with a specific date in a defined format.
 * 
 * @author Arjan Kranenburg
 *
 */
public class Fail extends GenericCommandExecutor {
	private static final String COMMAND = "fail";

	private static final SpecifiedParameter PARSPEC_MESSAGE = new SpecifiedParameter( 
			"message", String.class, "A message that explains why it had to fail at this point.",
			true, true, true, false );

	public Fail( SutInterface aSutInterface ) {
		super( COMMAND, "Sets the result to Failed", aSutInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_MESSAGE );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {

		String message = (String) this.obtainValue(aVariables, parameters, PARSPEC_MESSAGE);
		
		throw new TestSuiteException( message );
	}	
}
