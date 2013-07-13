package net.sf.testium.executor.general;

import java.util.ArrayList;

import net.sf.testium.systemundertest.SutInterface;

import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;


public class CheckString extends GenericCommandExecutor
{
	public enum MATCH {
		EXACT,
		CONTAINS,
		STARTSWITH,
		ENDSWITH;
		
		public String toString()
		{
			return super.toString().toLowerCase();
		}

		//overriding valueOf gives a compile error
		public static MATCH enumOf(String strValue) throws TestSuiteException
		{
			MATCH value = MATCH.EXACT;
			if ( strValue == null ) {
				throw new TestSuiteException( "String is null. Allowed values are " + valuesString() );
			}
			else if ( strValue.isEmpty() )	{
				throw new TestSuiteException( "String is empty. Allowed values are " + valuesString() );
			} else {
				try	{
					value = Enum.valueOf( MATCH.class, strValue.toUpperCase() );
				}
				catch( IllegalArgumentException iae ) {
					throw new TestSuiteException( "\"" + strValue + "\" is not allowed. Only " + valuesString() );
				}
			}

			return value;
		}

		public static String valuesString() {
			String allValues = "";
			for( MATCH supportedValues : MATCH.values() ) {
				allValues = (allValues.isEmpty() ? supportedValues.toString() : allValues + ", " + supportedValues);
			}
			return allValues;
		}
	}

	private static final String COMMAND = "checkVariable";
	
	private static final String PAR_VARIABLE = "variable";
	private static final String PAR_VALUE = "value";
	public static final String PAR_MATCH = "match";
	public static final String PAR_CASE = "case";

	private static final SpecifiedParameter PARSPEC_VARIABLE = new SpecifiedParameter( 
			PAR_VARIABLE, String.class, false, false, true, false );
	private static final SpecifiedParameter PARSPEC_VALUE = new SpecifiedParameter( 
			PAR_VALUE, String.class, false, true, true, true );
	public static final SpecifiedParameter PARSPEC_MATCH = new SpecifiedParameter( 
			PAR_MATCH, String.class, true, true, true, false )
				.setDefaultValue(MATCH.EXACT);
	public static final SpecifiedParameter PARSPEC_CASE = new SpecifiedParameter( 
			PAR_CASE, Boolean.class, true, true, true, false )
				.setDefaultValue( true );

	/**
	 *
	 */
	public CheckString( SutInterface aSutInterface )
	{
		super( COMMAND, aSutInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_VARIABLE );
		this.addParamSpec( PARSPEC_VALUE );
		this.addParamSpec( PARSPEC_MATCH );
		this.addParamSpec( PARSPEC_CASE );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception 
	{
		String stringToCheck = (String) this.obtainValue(aVariables, parameters, PARSPEC_VARIABLE);
		String expectedValue = (String) this.obtainValue(aVariables, parameters, PARSPEC_VALUE);
		String matchStr = (String) this.obtainOptionalValue(aVariables, parameters, PARSPEC_MATCH);
		MATCH match = MATCH.valueOf(matchStr);
		boolean caseSensitive = (Boolean) this.obtainOptionalValue(aVariables, parameters, PARSPEC_CASE);

		String stringName = parameters.get(PAR_VARIABLE).getName();
		result.setDisplayName( this.toString() + " " + stringName + " " + expectedValue );

		checkString(stringToCheck, expectedValue, match, caseSensitive, "Actual Text");
	}

	/**
	 * @param stringToCheck
	 * @param expectedValue
	 * @param match
	 * @param caseSensitive
	 * @throws Exception
	 */
	public void checkString(String stringToCheck, String expectedValue,
			MATCH match, boolean caseSensitive, String msgPrefix) throws Exception {
		if ( match.equals( MATCH.EXACT ))	{
			checkExact(expectedValue, stringToCheck, caseSensitive,
					msgPrefix + ": \"" + stringToCheck + "\" is not equal to: \"" + expectedValue + "\"");
		}
		else if ( match.equals( MATCH.CONTAINS ) ) {
			checkContains(expectedValue, stringToCheck, caseSensitive,
					msgPrefix + ": \"" + stringToCheck + "\" does not contain: \"" + expectedValue + "\"");
		}
		else if ( match.equals( MATCH.STARTSWITH ) ) {
			checkStartsWith(expectedValue, stringToCheck, caseSensitive,
					msgPrefix + ": \"" + stringToCheck + "\" does not start with: \"" + expectedValue + "\"" );
		}
		else if ( match.equals( MATCH.ENDSWITH ) ) {
			checkEndsWith(expectedValue, stringToCheck, caseSensitive,
					msgPrefix + ": \"" + stringToCheck + "\" does not end with: \"" + expectedValue + "\"" );
		}
		else {
			throw new Exception( "match criteria \"" + match + "\" is not supported. Only " + MATCH.valuesString() );
		}
	}

	/**
	 * @param expectedText
	 * @param actualText
	 * @param caseSensitive
	 * @param message
	 * @throws Exception
	 */
	public static void checkExact(String expectedText, String actualText,
			boolean caseSensitive, String message) throws Exception {
		if ( caseSensitive ) {
			if ( ! actualText.equals(expectedText) ) {
				throw new Exception( message );
			}
		} else {
			if ( ! actualText.equalsIgnoreCase(expectedText) ) {
				throw new Exception( message + " (ignoring case)" );
			}
		}
	}
	
	/**
	 * @param expectedText
	 * @param actualText
	 * @param caseSensitive
	 * @param message
	 * @throws Exception
	 */
	public static void checkContains(String expectedText, String actualText,
			boolean caseSensitive, String message) throws Exception {
		if ( caseSensitive ) {
			if ( ! actualText.contains(expectedText) ) {
				throw new Exception( message );
			}
		} else {
			String expectedText_lowerCase = expectedText.toLowerCase();
			String actualText_lowerCase = actualText.toLowerCase();
			if ( ! actualText_lowerCase.contains(expectedText_lowerCase) ) {
				throw new Exception( message + " (ignoring case)" );
			}
		}
	}

	/**
	 * @param expectedText
	 * @param actualText
	 * @param caseSensitive
	 * @param message
	 * @throws Exception
	 */
	public static void checkStartsWith(String expectedText, String actualText,
			boolean caseSensitive, String message) throws Exception {
		if ( caseSensitive ) {
			if ( ! actualText.startsWith(expectedText) ) {
				throw new Exception( message );
			}
		} else {
			String expectedText_lowerCase = expectedText.toLowerCase();
			String actualText_lowerCase = actualText.toLowerCase();
			if ( ! actualText_lowerCase.startsWith(expectedText_lowerCase) ) {
				throw new Exception( message + " (ignoring case)" );
			}
		}
	}

	/**
	 * @param expectedText
	 * @param actualText
	 * @param caseSensitive
	 * @param message
	 * @throws Exception
	 */
	public static void checkEndsWith(String expectedText, String actualText,
			boolean caseSensitive, String message) throws Exception {
		if ( caseSensitive ) {
			if ( ! actualText.endsWith(expectedText) ) {
				throw new Exception( message );
			}
		} else {
			String expectedText_lowerCase = expectedText.toLowerCase();
			String actualText_lowerCase = actualText.toLowerCase();
			if ( ! actualText_lowerCase.endsWith(expectedText_lowerCase) ) {
				throw new Exception( message + " (ignoring case)" );
			}
		}
	}
}
