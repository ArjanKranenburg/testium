package org.testium.executor.general;

import java.io.File;
import java.util.List;

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


public class CheckListSizeCommand implements TestStepCommandExecutor
{
	private static final String COMMAND = "checkListSize";
	private static final String PAR_LIST = "list";
	private static final String PAR_SIZE = "size";
	
	@SuppressWarnings("unchecked")
	public TestStepResult execute( TestStep aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);

		TestStepResult result = new TestStepResult( aStep );
		
		ParameterVariable listPar = (ParameterVariable) parameters.get(PAR_LIST);
		String listName = listPar.getVariableName();

		if ( ! aVariables.containsKey( listName ) )
		{
			throw new TestSuiteException( "Variable " + listName + " is not set",
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		List<Object> list = aVariables.getValueAs( List.class, listName);
		if ( list == null )
		{
			throw new TestSuiteException( "Variable " + listName + " is not a List",
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		ParameterImpl sizePar = (ParameterImpl) parameters.get(PAR_SIZE);
		int expectedSize = sizePar.getValueAsInt();
		if ( list.size() != expectedSize )
		{
			result.setResult(TestResult.FAILED);
			result.addComment( "List size was " + list.size() + ". expected " + expectedSize );
		}

		result.setResult(TestResult.PASSED);

		return result;
	}

	public String getCommand()
	{
		return COMMAND;
	}

	public boolean verifyParameters( ParameterArrayList aParameters ) throws TestSuiteException
	{
		// Check the list variable Parameter
		Parameter listPar = aParameters.get(PAR_LIST);
		if ( listPar == null )
		{
			throw new TestSuiteException( "Parameter " + PAR_LIST + " is not set",
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		if ( ! listPar.getClass().equals( ParameterVariable.class ) )
		{
			throw new TestSuiteException( "Parameter " + PAR_LIST + " is not defined as a variable",
			                              DefaultInterface.NAME + "." + COMMAND );
		}
		
		if ( ((ParameterVariable) listPar).getVariableName().isEmpty() )
		{
			throw new TestSuiteException( "Variable name of " + PAR_LIST + " cannot be empty",
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		// Check the size Parameter
		Parameter sizePar_tmp = aParameters.get(PAR_SIZE);
		if ( sizePar_tmp == null )
		{
			throw new TestSuiteException( "Parameter " + PAR_SIZE + " is not set",
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		if ( ! ParameterImpl.class.isInstance( sizePar_tmp ) )
		{
			throw new TestSuiteException( "Parameter " + sizePar_tmp.getName() + " is not a value",
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		ParameterImpl sizePar = (ParameterImpl) sizePar_tmp;
		if ( ! sizePar.getValueType().equals( Integer.class ) )
		{
			throw new TestSuiteException( "Parameter " + PAR_SIZE + " must be an integer",
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		if ( sizePar.getValueAsInt() < 0 )
		{
			throw new TestSuiteException( "Parameter " + PAR_SIZE + " must be non-negative",
			                              DefaultInterface.NAME + "." + COMMAND );
		}
		
		return true;
	}
}
