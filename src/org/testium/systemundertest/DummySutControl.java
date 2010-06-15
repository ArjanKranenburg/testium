package org.testium.systemundertest;

import java.util.ArrayList;

import org.testtoolinterfaces.utils.Trace;



/**
 * @author Arjan Kranenburg
 *
 */
public final class DummySutControl extends SutControl
{
	private ArrayList<SutIfCommand> myCommands;

	public DummySutControl()
	{
		Trace.println(Trace.CONSTRUCTOR);

		DummySutCommand startCmd = new DummySutCommand( "start" );
		DummySutCommand stopCmd = new DummySutCommand( "stop" );
		DummySutCommand restartCmd = new DummySutCommand( "restart" );
		DummySutCommand getVersionCmd = new DummySutCommand( "getVersion" );
		DummySutCommand getVersionLongCmd = new DummySutCommand( "getLongVersion" );

		myCommands = new ArrayList<SutIfCommand>();
		myCommands.add(startCmd);
		myCommands.add(stopCmd);
		myCommands.add(restartCmd);
		myCommands.add(getVersionCmd);
		myCommands.add(getVersionLongCmd);
}
	
	public String getName()
	{
		return "DummySUT";
	}

	public ArrayList<SutIfCommand> getCommands()
	{
		return new ArrayList<SutIfCommand>();
	}
}
