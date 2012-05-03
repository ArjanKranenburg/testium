package org.testium.executor.general;

import java.util.ArrayList;
import java.util.List;

import org.testium.executor.DefaultInterface;
import org.testium.systemundertest.SutInterface;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterImpl;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;


public class CheckListSizeCommand_New extends GenericCommandExecutor
{
	private static final String COMMAND = "checkListSize";
	
	private static final SpecifiedParameter PARSPEC_LIST = new SpecifiedParameter (
	        "list", String.class, false, false, true, false );
	private static final SpecifiedParameter PARSPEC_SIZE = new SpecifiedParameter (
	        "size", Integer.class, false, true, false, false );
	
	public CheckListSizeCommand_New(SutInterface anInterface)
	{
		super(COMMAND, anInterface, new ArrayList<SpecifiedParameter>() );
		this.addParamSpec(PARSPEC_LIST);
		this.addParamSpec(PARSPEC_SIZE);
	}

	@Override
	public String getCommand()
	{
		return COMMAND;
	}

	@Override
	public boolean verifyParameters( ParameterArrayList aParameters ) throws TestSuiteException
	{
		super.verifyParameters(aParameters);

		ParameterImpl sizePar = (ParameterImpl) aParameters.get(PARSPEC_SIZE.getName());
		if ( sizePar.getValueAsInt() < 0 )
		{
			throw new TestSuiteException( "Parameter " + PARSPEC_SIZE.getName() + " must be non-negative",
			                              DefaultInterface.NAME + "." + COMMAND );
		}
		
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doExecute(RunTimeData aVariables,
								ParameterArrayList parameters,
								TestStepResult result) throws Exception
	{
		List<Object> list = (List<Object>) this.obtainValue(aVariables, parameters, PARSPEC_LIST);
		int expectedSize = (Integer) this.obtainValue(aVariables, parameters, PARSPEC_SIZE);

		if ( list.size() != expectedSize )
		{
			throw new Exception( "List size was " + list.size() + ". expected " + expectedSize );
		}
	}
}
