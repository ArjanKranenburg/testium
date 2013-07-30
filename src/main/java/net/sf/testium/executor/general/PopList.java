package net.sf.testium.executor.general;

import java.util.ArrayList;
import java.util.List;

import net.sf.testium.systemundertest.SutInterface;

import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;


public class PopList extends GenericCommandExecutor
{
	private static final String COMMAND = "popList";
	private static final String PAR_LIST  = "list";
	private static final String PAR_VARIABLE = "variable";

	private static final SpecifiedParameter PARSPEC_LIST = new SpecifiedParameter (
			PAR_LIST, List.class, false, false, true, false );
	private static final SpecifiedParameter PARSPEC_VARIABLE = new SpecifiedParameter (
			PAR_VARIABLE, String.class, true, true, true, false );

	public PopList(SutInterface anInterface)
	{
		super(COMMAND, "Removes the last element of the list and optionally keeps it in a variable.",
				anInterface, new ArrayList<SpecifiedParameter>());

		this.addParamSpec(PARSPEC_LIST);
		this.addParamSpec(PARSPEC_VARIABLE);
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception
	{
		@SuppressWarnings("unchecked")
		List<Object> list = (List<Object>) this.obtainValue(aVariables, parameters, PARSPEC_LIST);
		String varName = (String) this.obtainValue(aVariables, parameters, PARSPEC_VARIABLE);

		Object obj = list.remove( list.size()-1 );

		if ( varName != null ) {
			RunTimeVariable rtVariable = new RunTimeVariable( varName, obj );
			aVariables.add(rtVariable);

			result.setDisplayName( this.toString() + " " + varName );
		}
	}
}
