/**
 * 
 */
package net.sf.testium.executor.general;

import java.util.ArrayList;

import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;

import net.sf.testium.executor.general.GenericCommandExecutor;

import net.sf.testium.executor.DefaultInterface;

import net.sf.testium.executor.general.SpecifiedParameter;

/**
 * 
 * @author Arjan Kranenburg
 *
 */
public class Substract extends GenericCommandExecutor {
	private static final String COMMAND = "substract";

	private static final String PAR_INT1 = "int1";
	private static final String PAR_INT2 = "int2";
	private static final String PAR_RESULT = "result";

	public static final SpecifiedParameter PARSPEC_INT1 = new SpecifiedParameter( 
			PAR_INT1, Integer.class, false, true, true, false );

	public static final SpecifiedParameter PARSPEC_INT2 = new SpecifiedParameter( 
			PAR_INT2, Integer.class, false, true, true, false );

	private static final SpecifiedParameter PARSPEC_RESULT = new SpecifiedParameter( 
			PAR_RESULT, String.class, false, true, false, false );

	public Substract( DefaultInterface defInterface )
	{
		super( COMMAND, defInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_INT1 );
		this.addParamSpec( PARSPEC_INT2 );
		this.addParamSpec( PARSPEC_RESULT );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepResult result)
			throws Exception
	{
		Integer int1 = (Integer) obtainValue( aVariables, parameters, PARSPEC_INT1 );
		Integer int2 = (Integer) obtainValue( aVariables, parameters, PARSPEC_INT2 );

		String varName = (String) obtainValue(aVariables, parameters, PARSPEC_RESULT);
		int sum = int1 - int2;
		result.setDisplayName( result.getDisplayName() + " " +  varName + " = " + int1 + " - " + int2 );
		
		RunTimeVariable rtVariable = new RunTimeVariable( varName, sum );
		aVariables.add(rtVariable);
	}
}
