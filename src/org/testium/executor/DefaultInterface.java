/**
 * 
 */
package org.testium.executor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;

import org.testium.executor.general.CheckListSizeCommand;
import org.testium.executor.general.CheckVariableCommand;
import org.testium.executor.general.GetListItemCommand;
import org.testium.executor.general.SetVariableCommand;
import org.testium.executor.general.WaitCommand;
import org.testium.systemundertest.SutInterface;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterImpl;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.testsuiteinterface.DefaultParameterCreator;
import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan
 *
 */
public class DefaultInterface implements SutInterface
{
	public final static String NAME			 = "Default";

	private Hashtable<String, TestStepCommandExecutor> myCommandExecutors;
	/**
	 * 
	 */
	public DefaultInterface()
	{
		Trace.println(Trace.CONSTRUCTOR);

		myCommandExecutors = new Hashtable<String, TestStepCommandExecutor>();

		add(new WaitCommand());
		add(new CheckVariableCommand());
		add(new SetVariableCommand());
		add(new GetListItemCommand());
		add(new CheckListSizeCommand());
	}

	/* (non-Javadoc)
	 * @see org.testium.systemundertest.SutInterface#getCommands()
	 */
	@Override
	public ArrayList<TestStepCommandExecutor> getCommandExecutors()
	{
		Trace.println( Trace.GETTER );
		Collection<TestStepCommandExecutor> executorCollection = myCommandExecutors.values();
		return new ArrayList<TestStepCommandExecutor>( executorCollection );
	}

	/* (non-Javadoc)
	 * @see org.testium.systemundertest.SutInterface#getInterfaceName()
	 */
	@Override
	public String getInterfaceName()
	{
		return NAME;
	}

	@Override
	public ArrayList<String> getCommands()
	{
		Trace.println( Trace.GETTER );
		return Collections.list(myCommandExecutors.keys());
	}

	@Override
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

	@Override
	public TestStepCommandExecutor getCommandExecutor(String aCommand)
	{
		Trace.println( Trace.GETTER );
		return myCommandExecutors.get(aCommand);
	}

	private void add( TestStepCommandExecutor aCommandExecutor )
	{
		Trace.println( Trace.UTIL );
		String command = aCommandExecutor.getCommand();
		myCommandExecutors.put(command, aCommandExecutor);
	}

	@Override
	public ParameterImpl createParameter( String aName,
	                                  String aType,
	                                  String aValue )
			throws TestSuiteException
	{
		return DefaultParameterCreator.createParameter(aName, aType, aValue);
	}
}
