package org.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testsuite.Parameter;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterHash;
import org.testtoolinterfaces.testsuite.ParameterVariable;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestStepArrayList;
import org.testtoolinterfaces.testsuite.TestStepSimple;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;

public class CustomTestStepExecutor implements TestStepCommandExecutor
{
	private String myCommand;
	private String myDescription;
	private CustomizableInterface myInterface;
	private TestStepArrayList mySteps;
	private ParameterArrayList myParameters;

	private TestStepMetaExecutor myTestStepExecutor;

	public CustomTestStepExecutor( String aCommand,
	                               String aDescription,
	                               CustomizableInterface anInterface,
	                               TestStepArrayList aSteps,
	                               ParameterArrayList aParameters,
	                               TestStepMetaExecutor aTestStepMetaExecutor )
	{
		myCommand = aCommand;
		myDescription = aDescription;
		myInterface = anInterface;
		mySteps = aSteps;
		myParameters = aParameters;
		myTestStepExecutor = aTestStepMetaExecutor;
	}

	@Override
	public String getCommand()
	{
		return myCommand;
	}

	@Override
	public TestStepResult execute( TestStepSimple aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);

		if( aStep.getDescription().isEmpty() )
		{
			aStep.setDescription( myDescription );
		}

		TestStepResult result = new TestStepResult( aStep );
		
		for (int key = 0; key < mySteps.size(); key++)
    	{
			TestStep step = mySteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, new File( "" ), aLogDir, aVariables);
			result.addSubStep(tsResult);
			
			if ( result.getResult().equals(VERDICT.ERROR) )
			{
				return result;
			}
    	}
		// TODO Auto-generated method stub
		return result;
	}

	@Override
	public boolean verifyParameters( ParameterArrayList aParameters )
																	throws TestSuiteException
	{
		for (int key = 0; key < myParameters.size(); key++)
    	{
			Parameter requiredParam = myParameters.get(key);
			String reqParamName = requiredParam.getName();
			
			// Check that parameter in the parameters parsed as arguments
			Parameter parsedPar = aParameters.get( reqParamName );
			if ( parsedPar == null )
			{
				throw new TestSuiteException( "Parameter " + reqParamName + " is not set",
				                              getInterfaceName() + "." + myCommand );
			}

			// Check the parameter type
			if ( ! parsedPar.getClass().equals( requiredParam.getClass() ) )
			{
				String paramType = requiredParam.getClass().getSimpleName();
				if ( requiredParam.getClass().equals( ParameterVariable.class ) )
				{
					paramType = "Variable";
				}
				else if ( requiredParam.getClass().equals( ParameterHash.class ) )
				{
					paramType = "Hash";
				}

				throw new TestSuiteException( "Parameter " + reqParamName + " is not defined as a " + paramType,
				                              getInterfaceName() + "." + myCommand );
			}

			// In case the parameter is a Variable, check that the variable name is not empty 
			if ( requiredParam.getClass().equals( ParameterVariable.class ) )
			{
				if ( ((ParameterVariable) parsedPar).getVariableName().isEmpty() )
				{
					throw new TestSuiteException( "Variable name of " + reqParamName + " cannot be empty",
					                              getInterfaceName() + "." + myCommand );
				}
			}


    	}

		return true;
	}

	public String getInterfaceName()
	{
		return myInterface.getInterfaceName();
	}
}
