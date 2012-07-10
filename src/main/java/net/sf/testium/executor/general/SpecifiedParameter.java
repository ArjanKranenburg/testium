package net.sf.testium.executor.general;

public class SpecifiedParameter
{
	private String myName;
	private Class<?> myType;
//	private Class<?>[] myTypes;
	private boolean myOptional;
	private boolean myValue;
	private boolean myVariable;
	private boolean myEmpty;
	private Object myDefaultValue;
	
	public SpecifiedParameter( String aName,
	                           Class<?> aType,
//	                           Class<?>[] aTypes,
	                           boolean optional,
	                           boolean value,
	                           boolean variable,
	                           boolean empty )
	{
		myName = aName;
		myType = aType;
//		myTypes = aTypes;
		myOptional = optional;
		myValue = value;
		myVariable = variable;
		myEmpty = empty;
		myDefaultValue = null;
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

//	/**
//	 * @return the types
//	 */
//	public Class<?>[] getTypes()
//	{
//		return myTypes;
//	}
//
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
	 * @return if an empty flag is allowed
	 */
	public SpecifiedParameter setDefaultValue( Object defaultValue )
	{
		myDefaultValue = defaultValue;
		return this;
	}
	
	/**
	 * @return the default value
	 */
	public Object getDefaultValue()
	{
		return myDefaultValue;
	}
	
//	/**
//	 * @return sets the optional-flag, i.e. the parameter is optional
//	 */
//	public SpecifiedParameter optional()
//	{
//		this.myOptional = true;
//		return this;
//	}
//
//	/**
//	 * @return sets the optional-flag t, i.e. the parameter is mandatory
//	 */
//	public SpecifiedParameter mandatory()
//	{
//		this.myOptional = false;
//		return this;
//	}
}
