package org.testium.executor.general;

import java.io.File;

import org.testium.executor.DefaultInterface;
import org.testium.executor.TestStepCommandExecutor;
import org.testtoolinterfaces.testresult.TestResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.Parameter;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterImpl;
import org.testtoolinterfaces.testsuite.ParameterVariable;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;


public class CheckVariableCommand implements TestStepCommandExecutor
{
	private static final String COMMAND = "checkVariable";
	private static final String PAR_VARIABLE = "VARIABLE";
	private static final String PAR_VALUE = "VALUE";
	
	public TestStepResult execute( TestStep aStep,
	                               RunTimeData anRTData,
	                               File aLogDir ) throws TestSuiteException
	{
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);

		TestStepResult result = new TestStepResult( aStep );
		
		ParameterVariable variablePar = (ParameterVariable) parameters.get(PAR_VARIABLE);
		String variableName = variablePar.getVariableName();
		ParameterImpl valuePar = (ParameterImpl) parameters.get(PAR_VALUE);

		RunTimeVariable rtVariable = anRTData.get( variableName );
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
		
//anRTData.print();
		if ( rtVariable.getValue().equals( valuePar.getValue() ) )
		{
			result.setResult(TestResult.PASSED);
		}
		else
		{
			result.setResult(TestResult.FAILED);
			result.setComment( "Variable has value " + rtVariable.getValue().toString()
			                   + ". Expected " + valuePar.getValue().toString() );
// TODO This comment does not end up as comment in the result file
// TODO The ParameterResult must contain the real and expected parameter.
//System.out.println( "Variable has value " + rtVariable.getValue().toString()
//                    + ". Expected " + valuePar.getValue().toString() );
		}

		return result;
	}

	@Override
	public String getCommand()
	{
		return COMMAND;
	}

	@Override
	public boolean verifyParameters( ParameterArrayList aParameters ) throws TestSuiteException
	{
		// Check the Variable Parameter
		Parameter variablePar = aParameters.get(PAR_VARIABLE);
		if ( variablePar == null )
		{
			throw new TestSuiteException( "Parameter " + PAR_VARIABLE + " is not set",
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		if ( ! ParameterVariable.class.isInstance( variablePar ) )
		{
			throw new TestSuiteException( "Parameter " + PAR_VARIABLE + " is not defined as a variable",
			                              DefaultInterface.NAME + "." + COMMAND );
		}
		
		if ( ((ParameterVariable) variablePar).getVariableName().isEmpty() )
		{
			throw new TestSuiteException( "Variable name of " + PAR_VARIABLE + " cannot be empty",
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		// Check the Value Parameter
		Parameter valuePar = aParameters.get(PAR_VALUE);
		if ( valuePar == null )
		{
			throw new TestSuiteException( "Parameter " + PAR_VALUE + " is not set",
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		if ( ! ParameterImpl.class.isInstance( valuePar ) )
		{
			throw new TestSuiteException( "Parameter " + valuePar.getName() + " is not a value",
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		return true;
	}
}
