package net.sf.testium.executor.general;

public class SpecifiedParameter
{
	private String myName;
	private Class<?> myType;
	private boolean myOptional;
	private boolean myValue;
	private boolean myVariable;
	private boolean myEmpty;
	
	public SpecifiedParameter( String aName,
	                           Class<?> aType,
	                           boolean optional,
	                           boolean value,
	                           boolean variable,
	                           boolean empty )
	{
		myName = aName;
		myType = aType;
		myOptional = optional;
		myValue = value;
		myVariable = variable;
		myEmpty = empty;
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
	public Class<?> getType()
	{
		return myType;
	}

	/**
	 * @return if this parameter is optional
	 */
	public boolean isOptional()
	{
		return myOptional;
	}

	/**
	 * @return if a value is allowed
	 */
	public boolean isValue()
	{
		return myValue;
	}

	/**
	 * @return if a variable is allowed
	 */
	public boolean isVariable()
	{
		return myVariable;
	}

	/**
	 * @return if an empty flag is allowed
	 */
	public boolean isEmpty()
	{
		return myEmpty;
	}
	
	/**
	 * @return a copy of this ParamSpec, but with the optional-flag set, i.e. the parameter is optional
	 */
	public SpecifiedParameter optional()
	{
		return new SpecifiedParameter( myName, myType, true, myValue, myVariable, myEmpty);
	}

	/**
	 * @return a copy of this ParamSpec, but with the optional-flag NOT set, i.e. the parameter is mandatory
	 */
	public SpecifiedParameter mandatory()
	{
		return new SpecifiedParameter( myName, myType, false, myValue, myVariable, myEmpty);
	}
}
