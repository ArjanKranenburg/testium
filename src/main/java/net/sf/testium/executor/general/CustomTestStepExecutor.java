package net.sf.testium.executor.general;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import net.sf.testium.executor.CustomInterface;
import net.sf.testium.executor.TestStepMetaExecutor;

import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.TestStepResultBase;
import org.testtoolinterfaces.testresult.impl.TestStepCommandResultImpl;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestStepCommand;
import org.testtoolinterfaces.testsuite.TestStepSequence;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;

public class CustomTestStepExecutor extends GenericCommandExecutor
{
	private String myDescription;
	private TestStepSequence mySteps;

	private TestStepMetaExecutor myTestStepExecutor;
	private ArrayList<String> myReturnParameters;

	public CustomTestStepExecutor( String aCommand,
	                               String aDescription,
	                               CustomInterface anInterface,
	                               ArrayList<SpecifiedParameter> aParameterSpecs,
	                               TestStepSequence aSteps,
	                               TestStepMetaExecutor aTestStepMetaExecutor )
	{
		this( aCommand, aDescription, anInterface, aParameterSpecs, aSteps, aTestStepMetaExecutor, new ArrayList<String>());
	}

	public CustomTestStepExecutor( String aCommand,
								   String aDescription,
								   CustomInterface anInterface,
								   ArrayList<SpecifiedParameter> aParameterSpecs,
								   TestStepSequence aSteps,
								   TestStepMetaExecutor aTestStepMetaExecutor,
								   ArrayList<String> aReturnParameters )
	{
		super(aCommand, anInterface, new ArrayList<SpecifiedParameter>());
		for ( SpecifiedParameter parameterSpec : aParameterSpecs )
		{
			this.addParamSpec(parameterSpec);
		}

		myDescription = aDescription;
		mySteps = aSteps;
		myTestStepExecutor = aTestStepMetaExecutor;
		
		myReturnParameters = aReturnParameters;
	}

	/**
	 *  Overrides to call a different doExecute()
	 *
	 * @see net.sf.testium.executor.general.GenericCommandExecutor#execute(org.testtoolinterfaces.testsuite.TestStep, org.testtoolinterfaces.utils.RunTimeData, java.io.File)
	 */
	public TestStepResult execute( TestStepCommand aStep,
								   RunTimeData aVariables,
								   File aLogDir )
				throws TestSuiteException
	{
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);
		
		TestStepCommandResult result = new TestStepCommandResultImpl( aStep );

		if ( ! myDescription.isEmpty() )
		{
			aStep.setDescription(myDescription);
		}

		try
		{
			doExecute(aVariables, parameters, result, aLogDir);
			result.setResult( VERDICT.PASSED );
		}
		catch (Exception e)
		{
			failTest(aLogDir, result, e);
		}
		
		return result;
	}
	
	protected void doExecute( RunTimeData aParentVars,
							  ParameterArrayList parameters,
							  TestStepCommandResult result,
							  File aLogDir )
			throws Exception
	{

	    RunTimeData rtVars = new RunTimeData( aParentVars );
		Iterator<TestStep> stepsItr = mySteps.iterator();
		while(stepsItr.hasNext())
		{
		    TestStep step = stepsItr.next();
		    for ( SpecifiedParameter parameterSpec : this.getParameters() )
		    {
		    	Object param = this.obtainValue(aParentVars, parameters, parameterSpec);
		    	if ( param == null )
		    	{
		    		param = parameterSpec.getDefaultValue();
		    	}
		    	RunTimeVariable var = new RunTimeVariable(parameterSpec.getName(), param);
		    	rtVars.add(var);
		    }

			TestStepResultBase tsResult = myTestStepExecutor.execute(step, new File( "" ), aLogDir, rtVars);
			result.addSubStep(tsResult);
		} 

	    for ( String paramName : myReturnParameters )
	    {
	    	if ( ! rtVars.containsKey(paramName) )
	    	{
	    		throw new Error( "Return Parameter \"" + paramName + "\" is not set." );
	    	}
    		aParentVars.add( rtVars.get(paramName) );
	    }
	}

	@Override
	protected void doExecute( RunTimeData aVariables,
							  ParameterArrayList parameters,
							  TestStepCommandResult result) throws Exception {
		throw new Error( "Method should not have been called" );
	}
}
