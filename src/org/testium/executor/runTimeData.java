package org.testium.executor;

import java.util.Enumeration;
import java.util.Hashtable;

public class runTimeData extends Hashtable<String, runTimeVariable>
{
	private static final long	serialVersionUID	= -896399275664941954L;

	private runTimeData myParentScope = null;
	
	public runTimeData( runTimeData aParentScope )
	{
		myParentScope = aParentScope;
	}
	
	public runTimeVariable get( String aName )
	{
		runTimeVariable var = super.get( aName );
		if ( var == null )
		{
			if ( myParentScope != null )
			{
				return myParentScope.get(aName);
			}
		}
		
		return var;
	}
	
	public void add( runTimeVariable aVariable )
	{
		this.put(aVariable.getName(), aVariable);
	}
	
	public void print()
	{
	    for (Enumeration<String> keys = this.keys(); keys.hasMoreElements();)
	    {
	    	String key = keys.nextElement();
	    	System.out.println( "(" + this.get(key).getType().getCanonicalName() + ")" + key );
	    }
	}
}
