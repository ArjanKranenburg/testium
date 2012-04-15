package org.testium.executor.general;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.testium.executor.TestStepCommandExecutor;
import org.testium.systemundertest.SutInterface;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.Parameter;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterImpl;
import org.testtoolinterfaces.testsuite.ParameterVariable;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;

public abstract class GenericCommandExecutor implements TestStepCommandExecutor {

	private ArrayList<SpecifiedParameter> myParameterSpecs;
	private String myCommand;
	private SutInterface myInterface;

	
	/**
	 * @param aVariables
	 * @param parameters
	 * @param result
	 * @throws TestSuiteException
	 */
	abstract protected void doExecute( RunTimeData aVariables,
	                                   ParameterArrayList parameters,
	                                   TestStepResult result) throws Exception;

	/**
	 * @param command
	 * @param parameterSpecs
	 */
	public GenericCommandExecutor( String aCommand,
	                               SutInterface anInterface,
								   ArrayList<SpecifiedParameter> parameterSpecs )
	{
		myCommand = aCommand;
		myInterface = anInterface;
		myParameterSpecs = parameterSpecs;
	}
	
	@Override
	public String getCommand()
	{
		return myCommand;
	}

	public String getInterfaceName()
	{
		return myInterface.getInterfaceName();
	}

	protected void addParamSpec(SpecifiedParameter specifiedParameter)
	{
		myParameterSpecs.add(specifiedParameter);
	}


	@Override
	public TestStepResult execute( TestStep aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);

		TestStepResult result = new TestStepResult( aStep );

		try
		{
			doExecute(aVariables, parameters, result);
			result.setResult( VERDICT.PASSED );
		}
		catch (Exception e)
		{
			failTest(aLogDir, result, e);
		}

		return result;
	}

	protected void failTest( File aLogDir, TestStepResult aResult, String aMessage )
	{
		aResult.setResult( VERDICT.FAILED );
		if ( ! aMessage.isEmpty() )
		{
			aResult.addComment(aMessage);
		}	
	}

	protected void failTest( File aLogDir, TestStepResult aResult, Exception e )
	{
		failTest( aLogDir, aResult, e.getClass().toString() + ": " + e.getMessage() );
	}

	/**
	 * @param aVariables
	 * @param parameters
	 * @param aParSpec
	 * 
	 * @return the value as an Object (can be null, if optional).
	 * If the parameter is a String, variables in the value are substituted as well.
	 * E.g. '{startdate}_more' becomes '20120406_more'
	 * @throws Exception when not a value or variable or when mandatory parameter is not found
	 */
	protected Object obtainValue( RunTimeData aVariables,
								  ParameterArrayList parameters,
								  SpecifiedParameter aParSpec) throws Exception
	{

		String parName = aParSpec.getName();
		Class<?> parType = aParSpec.getType();
		
		Parameter par = parameters.get(parName);
		Object value;
		
		if ( par == null )
		{
			if ( aParSpec.isOptional() )
			{
				return null;
			}
			throw new TestSuiteException( "Mandatory parameter " + parName + " is not set",
					toString() );
		}
		else if ( par instanceof ParameterImpl )
		{
			// Note: It is already verified (in the verifiedParameters() function) if it is allowed
			//       for a parameter to be a value or variable.

			ParameterImpl parVal = (ParameterImpl) par;
			value = parVal.getValueAs(parType);
			if ( parType == String.class )
			{
				value = aVariables.substituteVars((String) value);
			}
		}
		else if ( par instanceof ParameterVariable )
		{
			ParameterVariable parVar = (ParameterVariable) par;
			value = aVariables.getValueAs(parType, parVar.getVariableName());
		}
		else
		{
			throw new TestSuiteException( "Parameter \"" + parName + "\" is not a value or variable",
					toString() );
		}
		
		return value;
	}

	/**
	 * @return
	 */
	public String toString()
	{
		return this.getInterfaceName() + "." + this.getCommand();
	}

	@Override
	public boolean verifyParameters( ParameterArrayList aParameters)
				   throws TestSuiteException
	{
		Iterator<SpecifiedParameter> paramSpecItr = myParameterSpecs.iterator();
		
		while ( paramSpecItr.hasNext() )
		{
			SpecifiedParameter paramSpec = paramSpecItr.next();
			Parameter par = aParameters.get( paramSpec.getName() );

			// 1. Check that the parameter exists
			if ( par == null )
			{
				if ( paramSpec.isOptional() )
				{
					continue;
				} //else
				throw new TestSuiteException( "Mandatory parameter " + paramSpec.getName() + " is not set",
						toString() );
			}

			// 2. Check that the parameter is correctly a value or variable
			if ( par.getClass().equals( ParameterVariable.class ) )
			{
				verifyParameterVariable(par, paramSpec);
			}
			else // it's a value
			{
				verifyParameterValue(par, paramSpec);
			}
		}	
		
		return true;
	}

	/**
	 * @param aPar
	 * @param aParSpec
	 * @throws TestSuiteException
	 */
	protected void verifyParameterValue(Parameter aPar, SpecifiedParameter aParSpec) throws TestSuiteException
	{
		String parName = aParSpec.getName();
		if ( ! aParSpec.isValue() )
		{
			throw new TestSuiteException( "Parameter " + parName + " is not allowed to be a value",
			                              toString() );
		}

		if ( ! ParameterImpl.class.isInstance( aPar ) )
		{
			throw new TestSuiteException( "Parameter " + parName + " is not a value",
			                              toString() );
		}

		ParameterImpl parameter = (ParameterImpl) aPar;
		Class<? extends Object> paramType = aParSpec.getType();
		if ( ! parameter.getValueType().equals(paramType) )
		{
			throw new TestSuiteException( "Parameter " + parName + " must be a " + paramType.getSimpleName(),
			                              toString() );
		}

		if ( paramType.equals(String.class) )
		{
			if ( ! aParSpec.isEmpty() && parameter.getValueAsString().isEmpty() )
			{
				throw new TestSuiteException( "Parameter " + parName + " cannot be empty",
				                              toString() );
			}
		}
	}

	/**
	 * @param aPar
	 * @param aParSpec
	 * @throws TestSuiteException
	 */
	protected void verifyParameterVariable( Parameter aPar, SpecifiedParameter aParSpec ) throws TestSuiteException
	{
		String parName = aParSpec.getName();
		if ( ! aParSpec.isVariable() )
		{
			throw new TestSuiteException( "Parameter " + parName + " is not allowed to be variable",
			                              toString() );
		}

		if ( ! ParameterVariable.class.isInstance(aPar) )
		{
			throw new TestSuiteException( "Parameter " + parName + " is not defined as a variable",
										  toString() );
		}

		if ( ((ParameterVariable) aPar).getVariableName().isEmpty() )
		{
			throw new TestSuiteException( "Variable name of " + parName + " cannot be empty",
										  toString() );
		}
	}
}
