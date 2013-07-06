package net.sf.testium.executor.general;

import java.util.ArrayList;
import java.util.List;

import net.sf.testium.systemundertest.SutInterface;

import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;

public class CheckInteger extends GenericCommandExecutor
{
	private static final String COMMAND = "checkInteger";

	private static final String PAR_INTEGER = "integer";
	private static final String PAR_SIZE = "size";
	private static final String PAR_MATCH = "match";

	private static final String MATCH_EXACT = "exact";
	private static final String MATCH_LESS_THAN = "lessThan";
	private static final String MATCH_GREATER_THAN = "greaterThan";
	
	private static final SpecifiedParameter PARSPEC_INTEGER = new SpecifiedParameter( 
			PAR_INTEGER, List.class, false, false, true, false );
	private static final SpecifiedParameter PARSPEC_SIZE = new SpecifiedParameter( 
			PAR_SIZE, Integer.class, false, true, true, false );
	private static final SpecifiedParameter PARSPEC_MATCH = new SpecifiedParameter( 
			PAR_MATCH, String.class, true, true, true, false )
			.setDefaultValue(MATCH_EXACT);

	/**
	 *
	 */
	public CheckInteger( SutInterface aSutInterface )
	{
		super( COMMAND, aSutInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_INTEGER );
		this.addParamSpec( PARSPEC_SIZE );
		this.addParamSpec( PARSPEC_MATCH );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception 
	{
		@SuppressWarnings("unchecked")
		Integer integerToCheck = (Integer) this.obtainValue(aVariables, parameters, PARSPEC_INTEGER);
		int expectedSize = (Integer) this.obtainValue(aVariables, parameters, PARSPEC_SIZE);
		String match = (String) this.obtainOptionalValue(aVariables, parameters, PARSPEC_MATCH);

		String listName = parameters.get(PAR_INTEGER).getName();
		result.setDisplayName( this.toString() + " " + listName + " " + expectedSize );

		
		
		if ( match.equalsIgnoreCase( MATCH_EXACT ) )
		{
			checkExact(integerToCheck, expectedSize,
					"Integer is " + integerToCheck + ". Expected " + expectedSize );
			return;
		}
		else if ( match.equalsIgnoreCase( MATCH_LESS_THAN ) )
		{
			checkLessThan(integerToCheck, expectedSize,
					"Integer is " + integerToCheck + ". Expected less than " + expectedSize );
			return;
		}
		else if ( match.equalsIgnoreCase( MATCH_GREATER_THAN ) )
		{
			checkGreaterThan(integerToCheck, expectedSize,
					"Integer is " + integerToCheck + ". Expected more than " + expectedSize );
			return;
		}
		else
		{
			throw new Exception( "match criteria \"" + match + "\" is not supported. Only " + MATCH_EXACT + ", " 
					+ MATCH_LESS_THAN + ", and " + MATCH_GREATER_THAN );
		}

	}

	/**
	 * @param integerToCheck
	 * @param expectedSize
	 * @param message
	 * @throws TestSuiteException
	 */
	private void checkExact(Integer integerToCheck, int expectedSize, String message)
			throws TestSuiteException {
		if ( integerToCheck != expectedSize ) {
			throw new TestSuiteException( message );
		}
	}

	private void checkLessThan(Integer integerToCheck, int referenceSize, String message)
			throws TestSuiteException {
		if ( ! ( integerToCheck < referenceSize ) ) {
			throw new TestSuiteException( message );
		}
	}

	private void checkGreaterThan(Integer integerToCheck, int referenceSize, String message)
		throws TestSuiteException {
	if ( ! (integerToCheck > referenceSize) ) {
		throw new TestSuiteException( message );
	}
	}
}