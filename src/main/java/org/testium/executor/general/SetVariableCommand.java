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


public class SetVariableCommand implements TestStepCommandExecutor
{
	private static final String COMMAND = "setVariable";
	private static final String PAR_VARIABLE = "VARIABLE";
	private static final String PAR_VALUE = "VALUE";
	
	public TestStepResult execute( TestStep aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);

		TestStepResult result = new TestStepResult( aStep );
		
		ParameterVariable variablePar = (ParameterVariable) parameters.get(PAR_VARIABLE);
		String variableName = variablePar.getVariableName();
		ParameterImpl valuePar = (ParameterImpl) parameters.get(PAR_VALUE);

		RunTimeVariable rtVariable = new RunTimeVariable( variableName, valuePar.getValue() );
		aVariables.add(rtVariable);

		result.setResult(TestResult.PASSED);

		return result;
	}

	public String getCommand()
	{
		return COMMAND;
	}

	public boolean verifyParameters( ParameterArrayList aParameters ) throws TestSuiteException
	{
		// Check the Variable Parameter
		Parameter variablePar = aParameters.get(PAR_VARIABLE);
		if ( variablePar == null )
		{
			throw new TestSuiteException( "Parameter " + PAR_VARIABLE + " is not set",
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		if ( ! variablePar.getClass().equals( ParameterVariable.class ) )
		{
			throw new TestSuiteException( "Parameter " + PAR_VARIABLE + " is not defined as a variable",
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
			throw new TestSuiteException( "Parameter " + PAR_VALUE + " is not a value",
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		return true;
	}
}
