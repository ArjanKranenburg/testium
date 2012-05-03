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
import org.testtoolinterfaces.utils.RunTimeVariable;


public class GetListItemCommand implements TestStepCommandExecutor
{
	private static final String COMMAND = "getListItem";
	private static final String PAR_LIST = "list";
	private static final String PAR_OUTPUT = "output";
	private static final String PAR_INDEX = "index";
	
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

		ParameterImpl indexPar = (ParameterImpl) parameters.get(PAR_INDEX);
		int index = indexPar.getValueAsInt();
		if ( list.size() <= index )
		{
			throw new TestSuiteException( "The index " + index + " is larger than the size of the List: " + list.size(),
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		ParameterVariable outputPar = (ParameterVariable) parameters.get(PAR_OUTPUT);
		String outputName = outputPar.getVariableName();

		RunTimeVariable outputVariable = new RunTimeVariable( outputName, list.get(index) );
		aVariables.add(outputVariable);

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

		// Check the output Parameter
		Parameter outputPar = aParameters.get(PAR_OUTPUT);
		if ( outputPar == null )
		{
			throw new TestSuiteException( "Parameter " + PAR_OUTPUT + " is not set",
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		if ( ! outputPar.getClass().equals( ParameterVariable.class ) )
		{
			throw new TestSuiteException( "Parameter " + PAR_OUTPUT + " is not defined as a variable",
			                              DefaultInterface.NAME + "." + COMMAND );
		}
		
		if ( ((ParameterVariable) outputPar).getVariableName().isEmpty() )
		{
			throw new TestSuiteException( "Variable name of " + PAR_OUTPUT + " cannot be empty",
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		// Check the index Parameter
		Parameter indexPar_tmp = aParameters.get(PAR_INDEX);
		if ( indexPar_tmp == null )
		{
			throw new TestSuiteException( "Parameter " + PAR_INDEX + " is not set",
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		if ( ! ParameterImpl.class.isInstance( indexPar_tmp ) )
		{
			throw new TestSuiteException( "Parameter " + PAR_INDEX + " is not a value",
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		ParameterImpl indexPar = (ParameterImpl) indexPar_tmp;
		if ( ! indexPar.getValueType().equals( Integer.class ) )
		{
			throw new TestSuiteException( "Parameter " + PAR_INDEX + " must be an integer",
			                              DefaultInterface.NAME + "." + COMMAND );
		}

		if ( indexPar.getValueAsInt() < 0 )
		{
			throw new TestSuiteException( "Parameter " + PAR_INDEX + " must be non-negative",
			                              DefaultInterface.NAME + "." + COMMAND );
		}
		
		return true;
	}
}
