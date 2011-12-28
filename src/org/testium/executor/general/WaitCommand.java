package org.testium.executor.general;

import java.io.File;

import org.testium.executor.DefaultInterface;
import org.testium.executor.TestStepCommandExecutor;
import org.testtoolinterfaces.testresult.TestResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.Parameter;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterImpl;
import org.testtoolinterfaces.testsuite.TestStepSimple;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;


public class WaitCommand implements TestStepCommandExecutor
{
	private static final String COMMAND = "wait";
	private static final String PAR_TIME = "TIME";
	
	public TestStepResult execute( TestStepSimple aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);

		TestStepResult result = new TestStepResult( (TestStepSimple) aStep );
		
		ParameterImpl timePar = (ParameterImpl) parameters.get(PAR_TIME);
		int time = timePar.getValueAsInt();
	    long sleeptime = new Long(time * 1000);
		try
		{
			Thread.sleep( sleeptime );
		}
		catch (InterruptedException e)
		{
			throw new TestSuiteException( "Test Step " + COMMAND + " was interrupted", aStep, e );
		}
		result.setResult(TestResult.PASSED);
		
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
		Parameter timePar_tmp = aParameters.get(PAR_TIME);
		if ( timePar_tmp == null )
		{
			throw new TestSuiteException( "Parameter " + PAR_TIME + " is not set",
			                              DefaultInterface.NAME + "." + COMMAND );
		}
		
		if ( ! ParameterImpl.class.isInstance( timePar_tmp ) )
		{
			throw new TestSuiteException( "Parameter " + timePar_tmp.getName() + " is not a value",
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		ParameterImpl timePar = (ParameterImpl) timePar_tmp;
		if ( ! timePar.getValueType().equals( Integer.class ) )
		{
			throw new TestSuiteException( "Parameter " + PAR_TIME + " must be an integer",
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		if ( timePar.getValueAsInt() == 0 )
		{
			throw new TestSuiteException( "Parameter " + PAR_TIME + " must be positive",
			                              DefaultInterface.NAME + "." + COMMAND );
		}
		
		return true;
	}
}
