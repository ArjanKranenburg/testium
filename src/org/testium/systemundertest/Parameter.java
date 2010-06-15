package org.testium.systemundertest;

import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan Kranenburg
 *
 */
public class Parameter
{
	public static enum DIRECTION { IN, OUT, INOUT }

	private String myName;
	private Class myType;
	private DIRECTION myDirection;
	
	/**
	 * @param aParameter
	 * @param aValue
	 */
	public Parameter(String aName, Class aType)
	{
		Trace.println(Trace.CONSTRUCTOR, "Parameter( " + aName + ", " 
													   + aType.getSimpleName() + " )", true);
		myDirection = DIRECTION.IN;

	    myName = aName;
	    myType = aType;
	}

	public Parameter(String aParameter, DIRECTION aDirection, Class aType)
	{
		this(aParameter, aType);
		Trace.println(Trace.CONSTRUCTOR, "Parameter( " + aParameter + ", " 
													   + aDirection.toString() + ", "
													   + aType.getSimpleName() + " )", true);
		myDirection = aDirection;
	}

	public String getName()
	{
		Trace.println(Trace.GETTER);
	    return myName;
	}

	public Class getType()
	{
		Trace.println(Trace.GETTER);
	    return myType;
	}

	public boolean isIn()
	{
		Trace.println(Trace.UTIL);
	    return ((myDirection == DIRECTION.IN) || (myDirection == DIRECTION.INOUT));
	}

	public boolean isOut()
	{
		Trace.println(Trace.UTIL);
	    return ((myDirection == DIRECTION.OUT) || (myDirection == DIRECTION.INOUT));
	}
}
