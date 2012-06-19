package net.sf.testium.systemundertest;

import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan Kranenburg
 *
 */
public final class DummySutControl extends SutControl
{
	private final static String NAME			 = "DummySUT";

	public DummySutControl()
	{
		Trace.println(Trace.CONSTRUCTOR);

		add( new DummySutCommand( "start" ) );
		add( new DummySutCommand( "stop" ) );
		add( new DummySutCommand( "restart" ) );
		add( new DummySutCommand( "getVersion" ) );
		add( new DummySutCommand( "getLongVersion" ) );
	}
	
	/* (non-Javadoc)
	 * @see net.sf.testium.systemundertest.SutControl#getSutName()
	 */
	@Override
	public String getSutName()
	{
		Trace.println( Trace.GETTER );
		return NAME;
	}
	
	public void destroy()
	{
		// NOP	
	}
}
