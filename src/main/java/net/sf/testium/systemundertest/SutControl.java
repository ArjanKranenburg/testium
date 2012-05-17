/**
 * 
 */
package net.sf.testium.systemundertest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;

import net.sf.testium.executor.TestStepCommandExecutor;

import org.testtoolinterfaces.testresult.SutInfo;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterImpl;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.testsuiteinterface.DefaultParameterCreator;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;


/**
 * @author Arjan Kranenburg
 *
 */
public abstract class SutControl implements SutInterface
{
	public abstract String getSutName();
	
	private Hashtable<String, TestStepCommandExecutor> myCommandExecutors;

	public SutControl()
	{
		Trace.println(Trace.CONSTRUCTOR);

		myCommandExecutors = new Hashtable<String, TestStepCommandExecutor>();
	}

	/* (non-Javadoc)
	 * @see net.sf.testium.systemundertest.SutInterface#getInterfaceName()
	 */
	public String getInterfaceName()
	{
		Trace.println( Trace.GETTER );
		return "SutControl";
	}
	
	public SutInfo getSutInfo(File aLogDir, RunTimeData aParentRtData)
	{
		Trace.println( Trace.GETTER );
		return new SutInfo( this.getSutName() );
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

	public ArrayList<TestStepCommandExecutor> getCommandExecutors()
	{
		Trace.println( Trace.GETTER );
		Collection<TestStepCommandExecutor> executorsCollection = myCommandExecutors.values();
		
		return new ArrayList<TestStepCommandExecutor>( executorsCollection );
	}

	protected void add( TestStepCommandExecutor aCommandExecutor )
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
}
