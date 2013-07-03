/**
 * 
 */
package net.sf.testium.executor.general;

import java.util.ArrayList;

import net.sf.testium.systemundertest.SutInterface;

import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;

/**
 * Sets a variable with a specific date in a defined format.
 * 
 * @author Arjan Kranenburg
 *
 */
public class SubString extends GenericCommandExecutor {
	private static final String COMMAND = "subString";

	private static final SpecifiedParameter PARSPEC_STRING = new SpecifiedParameter( 
			"string", String.class, false, true, true, false );
	public static final SpecifiedParameter PARSPEC_INDEX_BEGIN = new SpecifiedParameter( 
			"beginIndex", Integer.class, false, true, true, false );
	public static final SpecifiedParameter PARSPEC_INDEX_END = new SpecifiedParameter( 
			"endIndex", Integer.class, true, true, true, false ).setDefaultValue( new Integer(0) );
	private static final SpecifiedParameter PARSPEC_VAR_NAME = new SpecifiedParameter( 
			"varName", String.class, false, true, true, false );	

	public SubString( SutInterface aSutInterface ) {
		super( COMMAND, aSutInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_STRING );
		this.addParamSpec( PARSPEC_INDEX_BEGIN );
		this.addParamSpec( PARSPEC_INDEX_END );
		this.addParamSpec( PARSPEC_VAR_NAME );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {

		String origString = (String) this.obtainValue(aVariables, parameters, PARSPEC_STRING);
		int beginIndex = (Integer) this.obtainValue(aVariables, parameters, PARSPEC_INDEX_BEGIN);
		int endIndex = (Integer) this.obtainOptionalValue(aVariables, parameters, PARSPEC_INDEX_END);
		String varName = (String) this.obtainOptionalValue(aVariables, parameters, PARSPEC_VAR_NAME);

		String resultString;
		if ( endIndex >= beginIndex ) {
			resultString = origString.substring(beginIndex, endIndex);
			result.setDisplayName( this.toString() + " " + varName + "= substring( \"" + origString + "\", " + beginIndex + ", " + endIndex );
		} else {
			resultString = origString.substring(beginIndex);
			result.setDisplayName( this.toString() + " " + varName + "= substring( \"" + origString + "\", " + beginIndex );
		}

		RunTimeVariable resultVar = new RunTimeVariable(varName, resultString);
		aVariables.add(resultVar);
	}
}
