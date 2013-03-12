/**
 * 
 */
package net.sf.testium.executor.general;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Enumeration;

import net.sf.testium.systemundertest.SutInterface;

import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;

/**
 * Prints all variables (for debug purposes)
 * 
 * @author Arjan Kranenburg
 *
 */
public class PrintVars extends GenericCommandExecutor
{
	private static final String COMMAND = "printVars";
	private static final String PAR_TOSCREEN = "toScreen";

	private static final SpecifiedParameter PARSPEC_TOSCREEN = new SpecifiedParameter( 
			PAR_TOSCREEN, Boolean.class, true, true, false, false )
				.setDefaultValue( true );

	public PrintVars( SutInterface aSutInterface  )
	{
		super( COMMAND, aSutInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_TOSCREEN );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception
	{
    	result.addComment("Current Variables:" );
	    addVarsToComment(aVariables, result);
	    
	    boolean toScreen = this.obtainOptionalValue(aVariables, parameters, PARSPEC_TOSCREEN);
	    if ( toScreen ) {
	    	aVariables.print();
	    }
	}

	private void addVarsToComment(RunTimeData aVariables, TestStepResult result) {
		for (Enumeration<String> keys = aVariables.keys(); keys.hasMoreElements();)
	    {
	    	String key = keys.nextElement();
	    	try
	    	{
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
		    	result.addComment(key + " -> Error ( " + e.getMessage() + " )" );
	    	}
	    }
		
    	RunTimeData parentScope = aVariables.getParentScope();
		if ( parentScope != null ) {
		    addVarsToComment(parentScope, result);
		}
	}
}
