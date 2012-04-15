/**
 * 
 */
package org.testium.executor.general;

import java.io.File;
import java.util.Enumeration;

import org.testium.executor.TestStepCommandExecutor;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;

/**
 * Prints all variables (for debug purposes)
 * 
 * @author Arjan Kranenburg
 *
 */
public class PrintVars implements TestStepCommandExecutor {
	private static final String COMMAND = "printVars";

	public PrintVars() {}

	@Override
	public TestStepResult execute( TestStep aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		TestStepResult result = new TestStepResult( aStep );

    	result.addComment("Current Variables:" );
	    for (Enumeration<String> keys = aVariables.keys(); keys.hasMoreElements();) {
	    	String key = keys.nextElement();
	    	RunTimeVariable rtVar = aVariables.get(key);
	    	if ( rtVar == null )
	    	{
	    		// This is only possible when rtData.put( key, null ) was used in stead of rtData.add( aVariable )
	    		continue;
	    	}

	    	Object valueObj = rtVar.getValue();
	    	String value = "null";
	    	if ( valueObj != null )	{
	    		value = valueObj.toString();
	    	}

	    	result.addComment(key + " -> ("
					+ rtVar.getType().getCanonicalName() + ") " + value);
	    }
    	result.addComment("Note: In addition, variables in the parentscope may exist" );

	    result.setResult(VERDICT.PASSED);
		return result;
	}

	@Override
	public String getCommand() {
		return COMMAND;
	}

	@Override
	public boolean verifyParameters(ParameterArrayList aParameters) {
		return true;
	}
}
