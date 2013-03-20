package net.sf.testium.executor.general;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.testium.systemundertest.SutInterface;

import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.Parameter;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterImpl;
import org.testtoolinterfaces.testsuite.ParameterVariable;
import org.testtoolinterfaces.utils.RunTimeData;


public class RemoveFromList extends GenericCommandExecutor
{
	private static final String COMMAND = "removeFromList";
	private static final String PAR_LIST  = "list";

	private static final SpecifiedParameter PARSPEC_LIST = new SpecifiedParameter (
			PAR_LIST, List.class, false, false, true, false );
	private static final SpecifiedParameter PARSPEC_VALUE = new SpecifiedParameter (
			SetList.PAR_VALUE, String.class, false, true, true, true );

	public RemoveFromList(SutInterface anInterface)
	{
		super(COMMAND, anInterface, new ArrayList<SpecifiedParameter>() );

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
		String value = (String) this.obtainValue(aVariables, parameters, PARSPEC_VALUE);

		list.remove(value);
	}
	
}
