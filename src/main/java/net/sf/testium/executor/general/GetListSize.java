/**
 * 
 */
package net.sf.testium.executor.general;

import java.util.ArrayList;
import java.util.List;

import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;

import net.sf.testium.executor.general.GenericCommandExecutor;

import net.sf.testium.executor.DefaultInterface;

import net.sf.testium.executor.general.SpecifiedParameter;

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
			PAR_LIST, List.class, false, false, true, false );

	private static final SpecifiedParameter PARSPEC_VARIABLE = new SpecifiedParameter( 
			PAR_VARIABLE, String.class, false, true, false, false );

	public GetListSize( DefaultInterface defInterface )
	{
		super( COMMAND, defInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_LIST );
		this.addParamSpec( PARSPEC_VARIABLE );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepResult result)
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
