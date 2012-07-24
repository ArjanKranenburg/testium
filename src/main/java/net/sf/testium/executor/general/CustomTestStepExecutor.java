package net.sf.testium.executor.general;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import net.sf.testium.executor.CustomInterface;
import net.sf.testium.executor.TestStepMetaExecutor;

import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestStepSequence;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;

public class CustomTestStepExecutor extends GenericCommandExecutor
{
	private String myDescription;
	private ArrayList<SpecifiedParameter> myParameterSpecs;
	private TestStepSequence mySteps;

	private TestStepMetaExecutor myTestStepExecutor;

	public CustomTestStepExecutor( String aCommand,
	                               String aDescription,
	                               CustomInterface anInterface,
	                               ArrayList<SpecifiedParameter> aParameterSpecs,
	                               TestStepSequence aSteps,
	                               TestStepMetaExecutor aTestStepMetaExecutor )
	{
		super(aCommand, anInterface, new ArrayList<SpecifiedParameter>());
		for ( SpecifiedParameter parameterSpec : aParameterSpecs )
		{
			this.addParamSpec(parameterSpec);
		}

		myDescription = aDescription;
		mySteps = aSteps;
		myParameterSpecs = aParameterSpecs;
		myTestStepExecutor = aTestStepMetaExecutor;
	}

	public TestStepResult execute( TestStep aStep,
								   RunTimeData aVariables,
								   File aLogDir )
				throws TestSuiteException
	{
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);
		
		TestStepResult result = new TestStepResult( aStep );
		
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
	
//	public TestStepResult execute( TestStep aStep,
//	                               RunTimeData aVariables,
//	                               File aLogDir ) throws TestSuiteException
//	{
//		ParameterArrayList parameters = aStep.getParameters();
//		verifyParameters(parameters);
//
//		if( aStep.getDescription().isEmpty() )
//		{
//			aStep.setDescription( myDescription );
//		}
//
//		TestStepResult result = new TestStepResult( aStep );
//
//		Iterator<TestStep> stepsItr = mySteps.iterator();
//		while(stepsItr.hasNext())
//		{
//		    TestStep step = stepsItr.next();
//			TestStepResult tsResult = myTestStepExecutor.execute(step, new File( "" ), aLogDir, aVariables);
//			result.addSubStep(tsResult);
//			
//			if ( result.getResult().equals(VERDICT.ERROR) )
//			{
//				return result;
//			}
//		} 
//
//		// TODO Auto-generated method stub
//		return result;
//	}


	protected void doExecute( RunTimeData aVariables,
							  ParameterArrayList parameters,
							  TestStepResult result,
							  File aLogDir )
			throws Exception {

		Iterator<TestStep> stepsItr = mySteps.iterator();
		while(stepsItr.hasNext())
		{
		    TestStep step = stepsItr.next();
			TestStepResult tsResult = myTestStepExecutor.execute(step, new File( "" ), aLogDir, aVariables);
			result.addSubStep(tsResult);
			
//			if ( result.getResult().equals(VERDICT.ERROR) )
//			{
//				throw new TestSuiteException( "Method should not have been called" );
//			}
		} 
	}

	@Override
	protected void doExecute( RunTimeData aVariables,
							  ParameterArrayList parameters,
							  TestStepResult result) throws Exception {
		throw new Error( "Method should not have been called" );
	}
}
