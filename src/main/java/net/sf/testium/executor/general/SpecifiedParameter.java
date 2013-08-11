package net.sf.testium.executor.general;

public class SpecifiedParameter
{
	private String myName;
	private Class<?> myType;
//	private Class<?>[] myTypes;
	private String description;
	private boolean myOptional;
	private boolean myValue;
	private boolean myVariable;
	private boolean myEmpty;
	private Object myDefaultValue;
	
	public SpecifiedParameter( String aName,
	                           Class<?> aType,
//	                           Class<?>[] aTypes,
	                           String aDescription,
	                           boolean optional,
	                           boolean value,
	                           boolean variable,
	                           boolean empty )
	{
		myName = aName;
		myType = aType;
//		myTypes = aTypes;
		this.description = aDescription;
		myOptional = optional;
		myValue = value;
		myVariable = variable;
		myEmpty = empty;
		
		if (myType.equals( String.class )) {
			myDefaultValue = new String();
		} else if (myType.equals( Boolean.class )) {
			myDefaultValue = new Boolean( false );
		} else if (myType.equals( Integer.class )) {
			myDefaultValue = new Integer( 0 );
		} else {
			myDefaultValue = null;
		}
	}

	@Deprecated
	public SpecifiedParameter( String aName,
            Class<?> aType,
//            Class<?>[] aTypes,
            boolean optional,
            boolean value,
            boolean variable,
            boolean empty ) {
		this(aName, aType, "", optional, value, variable, empty);
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
	 * Sets the default value if optional
	 * @return this object
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

	public String getDescription() {
		return this.description;
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
