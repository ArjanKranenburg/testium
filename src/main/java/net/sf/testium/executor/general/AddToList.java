package net.sf.testium.executor.general;

import java.util.ArrayList;
import java.util.List;

import net.sf.testium.systemundertest.SutInterface;

import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;


public class AddToList extends GenericCommandExecutor
{
	private static final String COMMAND = "addToList";
	private static final String PAR_LIST  = "list";

	private static final SpecifiedParameter PARSPEC_LIST = new SpecifiedParameter (
			PAR_LIST, List.class, "The list to be added to", false, false, true, false );
	private static final SpecifiedParameter PARSPEC_VALUE = new SpecifiedParameter (
			SetList.PAR_VALUE, String.class, "The value to be added to the list", false, true, true, true );

	public AddToList(SutInterface anInterface)
	{
		super(COMMAND, "Adds a value to a list", anInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec(PARSPEC_LIST);
		this.addParamSpec(PARSPEC_VALUE);
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception
	{
		@SuppressWarnings("unchecked")
		List<Object> list = (List<Object>) this.obtainValue(aVariables, parameters, PARSPEC_LIST);

		SetList.addValuesToList(list, parameters, aVariables);
	}
}
