package net.sf.testium.executor.general;

import java.util.ArrayList;

import net.sf.testium.systemundertest.SutInterface;

import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;


public class SetVariable extends GenericCommandExecutor
{
	private static final String COMMAND = "setVariable";

	private static final SpecifiedParameter PARSPEC_NAME = new SpecifiedParameter (
	        "name", String.class, false, true, false, false );
	private static final SpecifiedParameter PARSPEC_VALUE = new SpecifiedParameter (
	        "value", String.class, false, true, false, false );

	public SetVariable(SutInterface anInterface)
	{
		super(COMMAND, anInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec(PARSPEC_NAME);
		this.addParamSpec(PARSPEC_VALUE);
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepResult result)
			throws Exception
	{
		String variableName = (String) this.obtainValue(aVariables, parameters, PARSPEC_NAME);
		String variableValue = (String) this.obtainValue(aVariables, parameters, PARSPEC_VALUE);
		
		result.setDisplayName( result.getDisplayName() + " " + variableValue + " -> " + variableName );
		RunTimeVariable rtVariable = new RunTimeVariable( variableName, variableValue );
		aVariables.add(rtVariable);
	}
}
