/**
 * 
 */
package net.sf.testium.executor.general;

import java.io.File;
import java.lang.reflect.Proxy;
import java.util.Enumeration;

import net.sf.testium.executor.TestStepCommandExecutor;

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

	public TestStepResult execute( TestStep aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		TestStepResult result = new TestStepResult( aStep );

    	result.addComment("Current Variables:" );
	    addVarsToComment(aVariables, result);

	    result.setResult(VERDICT.PASSED);
		return result;
	}

	private void addVarsToComment(RunTimeData aVariables, TestStepResult result) {
		for (Enumeration<String> keys = aVariables.keys(); keys.hasMoreElements();)
	    {
	    	try
	    	{
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
		    		if ( ! (valueObj instanceof Proxy) ) {
			    		value = "proxied";
		    		}
		    		value = valueObj.toString();
		    	}
	
		    	result.addComment(key + " -> ("
						+ rtVar.getType().getCanonicalName() + ") " + value);
	    	}
	    	catch (Exception e)
	    	{
	    		System.out.println( "Exception " + e.getMessage() );
	    	}
	    }
		
    	RunTimeData parentScope = aVariables.getParentScope();
		if ( parentScope != null ) {
		    addVarsToComment(parentScope, result);
		}
	}

	public String getCommand() {
		return COMMAND;
	}

	public boolean verifyParameters(ParameterArrayList aParameters) {
		return true;
	}
}
