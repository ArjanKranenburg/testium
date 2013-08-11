/**
 * 
 */
package net.sf.testium.executor.general;

import java.util.ArrayList;
import java.util.List;

import net.sf.testium.executor.DefaultInterface;

import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;

/**
 * 
 * @author Arjan Kranenburg
 *
 */
public class GetListSize extends GenericCommandExecutor {
	private static final String COMMAND = "getListSize";

	private static final String PAR_LIST = "list";
	private static final String PAR_VARIABLE = "variable";

	public static final SpecifiedParameter PARSPEC_LIST = new SpecifiedParameter( 
			PAR_LIST, List.class, "The list", false, false, true, false );

	private static final SpecifiedParameter PARSPEC_VARIABLE = new SpecifiedParameter( 
			PAR_VARIABLE, String.class, "A variableName that will be used to store the size of the list",
			false, true, false, false );

	public GetListSize( DefaultInterface defInterface )
	{
		super( COMMAND, "Gets the size of a list and stores it in a variable",
				defInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_LIST );
		this.addParamSpec( PARSPEC_VARIABLE );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception
	{
		@SuppressWarnings("unchecked")
		List<Object> list = (List<Object>) obtainValue( aVariables, parameters, PARSPEC_LIST );

		String varName = (String) obtainValue(aVariables, parameters, PARSPEC_VARIABLE);
		int size = list.size();
		
		RunTimeVariable rtVariable = new RunTimeVariable( varName, size );
		aVariables.add(rtVariable);

		result.setDisplayName( this.toString() + " " + varName + " = " + size );
	}
}
