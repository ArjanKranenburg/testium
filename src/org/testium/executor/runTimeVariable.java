package org.testium.executor;

import org.testtoolinterfaces.utils.Trace;

public class runTimeVariable
{
	private String myName;
	private Class myType;
	private Object myValue;
	
	public runTimeVariable( String aName, Class aType, Object aValue )
	{
		myName = aName;
		myType = aType;
		myValue = aValue;
	}

	public runTimeVariable( String aName, Object aValue )
	{
		this( aName, aValue.getClass(), aValue );
	}

	public runTimeVariable( String aName, Class aType )
	{
		this( aName, aType, null );
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return myName;
	}

	/**
	 * @return the type
	 */
	public Class getType()
	{
		return myType;
	}

	/**
	 * @return the value
	 */
	public Object getValue()
	{
		return myValue;
	}

	public void setValue( Object aValue )
	{
		Trace.println(Trace.SETTER, "setValue( " + aValue.toString() + " )", true);
		if ( myType == aValue.getClass() )
	    {
			myValue = aValue;
	    }
	}
}
