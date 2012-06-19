/**
 * 
 */
package net.sf.testium.executor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;

import net.sf.testium.systemundertest.SutInterface;

import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterImpl;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.testsuiteinterface.DefaultParameterCreator;
import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan
 *
 */
public class CustomInterface implements SutInterface, CustomizableInterface
{
	public String myName;

	private Hashtable<String, TestStepCommandExecutor> myCommandExecutors;
	/**
	 * 
	 */
	public CustomInterface( String aName )
	{
		Trace.println(Trace.CONSTRUCTOR);
		myName = aName;
		
		myCommandExecutors = new Hashtable<String, TestStepCommandExecutor>();
	}

	/* (non-Javadoc)
	 * @see net.sf.testium.systemundertest.SutInterface#getCommands()
	 */
	public ArrayList<TestStepCommandExecutor> getCommandExecutors()
	{
		Trace.println( Trace.GETTER );
		Collection<TestStepCommandExecutor> executorCollection = myCommandExecutors.values();
		return new ArrayList<TestStepCommandExecutor>( executorCollection );
	}

	/* (non-Javadoc)
	 * @see net.sf.testium.systemundertest.SutInterface#getInterfaceName()
	 */
	public String getInterfaceName()
	{
		return myName;
	}

	public ArrayList<String> getCommands()
	{
		Trace.println( Trace.GETTER );
		return Collections.list(myCommandExecutors.keys());
	}

	public boolean hasCommand(String aCommand)
	{
		Trace.println( Trace.UTIL );
		ArrayList<String> commands = getCommands();
		return commands.contains(aCommand);
	}

	public boolean verifyParameters( String aCommand,
	                                 ParameterArrayList aParameters )
		   throws TestSuiteException
	{
		TestStepCommandExecutor executor = this.getCommandExecutor(aCommand);
		return executor.verifyParameters(aParameters);
	}

	public TestStepCommandExecutor getCommandExecutor(String aCommand)
	{
		Trace.println( Trace.GETTER );
		return myCommandExecutors.get(aCommand);
	}

	public void add( TestStepCommandExecutor aCommandExecutor )
	{
		Trace.println( Trace.UTIL );
		String command = aCommandExecutor.getCommand();
		myCommandExecutors.put(command, aCommandExecutor);
	}

	public ParameterImpl createParameter( String aName,
	                                  String aType,
	                                  String aValue )
			throws TestSuiteException
	{
		return DefaultParameterCreator.createParameter(aName, aType, aValue);
	}

	@Override
	public String toString()
	{
		return this.getInterfaceName();
	}

	public void destroy()
	{
		// NOP	
	}
}
