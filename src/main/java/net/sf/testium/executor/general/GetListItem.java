package net.sf.testium.executor.general;

import java.util.ArrayList;
import java.util.List;

import net.sf.testium.systemundertest.SutInterface;

import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;

public class GetListItem extends GenericCommandExecutor
{
	private static final String COMMAND = "getListItem";

	private static final String PAR_LIST = "list";
	private static final String PAR_INDEX = "index";
	private static final String PAR_OUTPUT = "output";

	private static final SpecifiedParameter PARSPEC_LIST = new SpecifiedParameter( 
			PAR_LIST, List.class, "List to take item from", false, false, true, false );
	private static final SpecifiedParameter PARSPEC_INDEX = new SpecifiedParameter( 
			PAR_INDEX, Integer.class, "Index that specifies which item from the list to get",
			false, true, true, false );
	private static final SpecifiedParameter PARSPEC_OUTPUT = new SpecifiedParameter( 
			PAR_OUTPUT, String.class, "Name of the variable that will get the value of the list",
			false, true, false, false );

	/**
	 *
	 */
	public GetListItem( SutInterface aSutInterface )
	{
		super( COMMAND, "Gets one item from a list and stores it in a variable",
				aSutInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_LIST );
		this.addParamSpec( PARSPEC_INDEX );
		this.addParamSpec( PARSPEC_OUTPUT );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception
	{
		@SuppressWarnings("unchecked")
		List<Object> list = (List<Object>) this.obtainValue(aVariables, parameters, PARSPEC_LIST);
		int index = (Integer) this.obtainValue(aVariables, parameters, PARSPEC_INDEX);
		String outputName = (String) obtainValue(aVariables, parameters, PARSPEC_OUTPUT);

//		if ( list == null )
//		{
//			throw new TestSuiteException( "Variable " + listName + " is not a List",
//			                              this.toString() );
//		}

		String listName = parameters.get(PAR_LIST).getName();
		result.setDisplayName( this.toString() + " " + listName + " " + index + " -> " + outputName );

		if ( list.size() <= index )
		{
			throw new TestSuiteException( "Index " + index + " is not present in a List of size " + list.size(),
					this.toString() );
		}


		RunTimeVariable outputVariable = new RunTimeVariable( outputName, list.get(index) );
		aVariables.add(outputVariable);
	}
}
