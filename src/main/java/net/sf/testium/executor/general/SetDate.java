/**
 * 
 */
package net.sf.testium.executor.general;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import net.sf.testium.systemundertest.SutInterface;

import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;

/**
 * Sets a variable with a specific date in a defined format.
 * 
 * @author Arjan Kranenburg
 *
 */
public class SetDate extends GenericCommandExecutor {
	private static final String COMMAND = "setDate";

	private static final String TODAY 		= "today";
	private static final String TOMORROW 	= "tomorrow";
	private static final String NEXT_WEEK 	= "next_week";

	private static final SpecifiedParameter PARSPEC_NAME = new SpecifiedParameter( 
			"name", String.class, false, true, false, false );
	public static final SpecifiedParameter PARSPEC_DAY = new SpecifiedParameter( 
			"day", String.class, true, true, false, false )
		.setDefaultValue(TODAY);
	private static final SpecifiedParameter PARSPEC_FORMAT = new SpecifiedParameter( 
			"format", String.class, true, true, false, false )
		.setDefaultValue("dd-MM-yyyy");
	

	public SetDate( SutInterface aSutInterface ) {
		super( COMMAND, aSutInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_NAME );
		this.addParamSpec( PARSPEC_DAY );
		this.addParamSpec( PARSPEC_FORMAT );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {

		String varName = (String) this.obtainValue(aVariables, parameters, PARSPEC_NAME);
		String day = (String) this.obtainOptionalValue(aVariables, parameters, PARSPEC_DAY);
		String format = (String) this.obtainOptionalValue(aVariables, parameters, PARSPEC_FORMAT);
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat( format );

		Date date;
		if( day.equalsIgnoreCase(TODAY) ) {
			date = new Date();
		} else {
			Calendar cal = Calendar.getInstance();
			if (day.equalsIgnoreCase(TOMORROW)) {
				cal.add(Calendar.DAY_OF_YEAR, 1);
			} else if (day.equalsIgnoreCase(NEXT_WEEK)) {
				cal.add(Calendar.DAY_OF_YEAR, 7);
			} else {
				throw new TestSuiteException( "Unknown day: " + day + ". " 
					+ "It must be one of '" + TODAY + "', '" + TOMORROW + "', or '" + NEXT_WEEK + "'" );
			}
			date = cal.getTime();
		}

		String dateString = dateFormatter.format( date );
		result.setDisplayName( this.toString() + " " + varName + "=\"" + dateString + "\"" );

		RunTimeVariable dateVar = new RunTimeVariable(varName, dateString);
		aVariables.add(dateVar);
	}
}
