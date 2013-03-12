package net.sf.testium.executor.general;

import java.util.ArrayList;

import net.sf.testium.executor.DefaultInterface;
import net.sf.testium.systemundertest.SutInterface;

import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterImpl;
import org.testtoolinterfaces.testsuite.ParameterVariable;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;


public class CheckVariable extends GenericCommandExecutor
{
	private static final String COMMAND = "checkVariable";
	
	private static final String PAR_VARIABLE = "variable";
	private static final String PAR_VALUE = "value";

	private static final SpecifiedParameter PARSPEC_VARIABLE = new SpecifiedParameter( 
			PAR_VARIABLE, String.class, false, false, true, false );
	private static final SpecifiedParameter PARSPEC_VALUE = new SpecifiedParameter( 
			PAR_VALUE, String.class, false, true, true, true );

	/**
	 *
	 */
	public CheckVariable( SutInterface aSutInterface )
	{
		super( COMMAND, aSutInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_VARIABLE );
		this.addParamSpec( PARSPEC_VALUE );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception 
	{

		ParameterVariable variablePar = (ParameterVariable) parameters.get(PAR_VARIABLE);
		String variableName = variablePar.getVariableName();
		ParameterImpl valuePar = (ParameterImpl) parameters.get(PAR_VALUE);

		result.setDisplayName( this.toString() + " " + variableName + " " + valuePar.getValue().toString() );
		RunTimeVariable rtVariable = aVariables.get( variableName );
		if ( rtVariable == null )
		{
			throw new TestSuiteException( "Variable " + variableName + " is not set",
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		if ( ! valuePar.getValueType().equals( rtVariable.getType() ) )
		{
			throw new TestSuiteException( "Parameter " + PAR_VALUE + " of type " + valuePar.getValueType()
			                              + " does not have the same type as Parameter "
			                              + PAR_VARIABLE + " of type " + rtVariable.getType(),
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		if ( ! rtVariable.getValue().equals( valuePar.getValue() ) )
		{
			throw new TestSuiteException( "Variable has value " + rtVariable.getValue().toString()
			                   + ". Expected " + valuePar.getValue().toString() );
// TODO The ParameterResult must contain the real and expected parameter.
		}
	}
}
