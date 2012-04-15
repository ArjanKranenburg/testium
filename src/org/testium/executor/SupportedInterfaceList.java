/**
 * 
 */
package org.testium.executor;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.testtoolinterfaces.testsuite.TestInterface;
import org.testtoolinterfaces.testsuite.TestInterfaceList;

/**
 * @author Arjan
 *
 */
public class SupportedInterfaceList implements TestInterfaceList
{
	Hashtable<String, TestInterface> myList;
	
	/**
	 * 
	 */
	public SupportedInterfaceList()
	{
		myList = new Hashtable<String, TestInterface>();
	}

	/* (non-Javadoc)
	 * @see org.testtoolinterfaces.testsuiteinterface.TestInterfaceList#getInterface(java.lang.String)
	 */
	@Override
	public TestInterface getInterface(String anInterfaceName)
	{
		return myList.get(anInterfaceName);
	}

	/**
	 * Adds a TestInterface to the list of supported interfaces
	 * 
	 * @param anInterface
	 */
	public void add( TestInterface anInterface )
	{
		myList.put(anInterface.getInterfaceName(), anInterface);
	}
	
	public Enumeration<String> getInterfaceNames()
	{
		return myList.keys();
	}
	
	public String toString()
	{
		String allInterfaces = "";
		Iterator<TestInterface> iFaceItr = this.iterator();
		while(iFaceItr.hasNext())
		{
			TestInterface iFace = iFaceItr.next();
			if ( ! allInterfaces.isEmpty() )
			{
				allInterfaces += ", ";
			}
			allInterfaces += iFace.getInterfaceName();
		}
		return allInterfaces;
	}

	@Override
	public Iterator<TestInterface> iterator()
	{
		return myList.values().iterator();
	}
}
