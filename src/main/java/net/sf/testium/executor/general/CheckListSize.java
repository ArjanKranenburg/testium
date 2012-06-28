package net.sf.testium.executor.general;

import java.util.ArrayList;
import java.util.List;

import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;

import net.sf.testium.executor.general.GenericCommandExecutor;


import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.systemundertest.SutInterface;
public class CheckListSize extends GenericCommandExecutor
{
	private static final String COMMAND = "checkListSize";

	private static final String PAR_LIST = "list";
	private static final String PAR_SIZE = "size";

	private static final SpecifiedParameter PARSPEC_LIST = new SpecifiedParameter( 
			PAR_LIST, List.class, false, false, true, false );
	private static final SpecifiedParameter PARSPEC_SIZE = new SpecifiedParameter( 
			PAR_SIZE, Integer.class, false, true, false, false );

	/**
	 *
	 */
	public CheckListSize( SutInterface aSutInterface )
	{
		super( COMMAND, aSutInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_LIST );
		this.addParamSpec( PARSPEC_SIZE );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepResult result)
			throws Exception 
	{
		@SuppressWarnings("unchecked")
		List<Object> list = (List<Object>) this.obtainValue(aVariables, parameters, PARSPEC_LIST);
		int expectedSize = (Integer) this.obtainOptionalValue(aVariables, parameters, PARSPEC_SIZE);

		if ( list.size() != expectedSize )
		{
			throw new TestSuiteException( "List size was " + list.size() + ". Expected " + expectedSize );
		}
	}
}
