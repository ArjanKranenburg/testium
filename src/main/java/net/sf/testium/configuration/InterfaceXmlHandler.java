package net.sf.testium.configuration;

import net.sf.testium.executor.CustomInterface;
import net.sf.testium.executor.CustomizableInterface;
import net.sf.testium.executor.SupportedInterfaceList;
import net.sf.testium.executor.TestStepMetaExecutor;

import org.testtoolinterfaces.testsuite.TestInterface;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.TTIException;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;


public class InterfaceXmlHandler extends XmlHandler
{
	public static final String START_ELEMENT = "Interface";

	private static final String	ATTR_NAME			= "name";

	private SupportedInterfaceList myInterfaceList;
	private CustomStepXmlHandler myCustomStepXmlHandler;

	private TestInterface myInterface;
	
	public InterfaceXmlHandler(XMLReader anXmlReader, SupportedInterfaceList anInterfaceList, TestStepMetaExecutor aTestStepMetaExecutor)
	{
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);

	    myInterfaceList = anInterfaceList;
	    
	    myCustomStepXmlHandler = new CustomStepXmlHandler(anXmlReader, anInterfaceList, aTestStepMetaExecutor);
		this.addElementHandler(CustomStepXmlHandler.START_ELEMENT, myCustomStepXmlHandler);

	    reset();
	}

	@Override
	public void handleStartElement(String aQualifiedName)
	{
		// nop
	}

	@Override
	public void handleCharacters(String aValue)
	{
		// nop
	}

	@Override
	public void handleEndElement(String aQualifiedName)
	{
		// nop
	}

	@Override
	public void processElementAttributes(String aQualifiedName, Attributes att)
	{
		Trace.print(Trace.SUITE, "processElementAttributes( " 
		            + aQualifiedName, true );
	     	if (aQualifiedName.equalsIgnoreCase(START_ELEMENT))
	    	{
			    for (int i = 0; i < att.getLength(); i++)
			    {
		    		Trace.append( Trace.SUITE, ", " + att.getQName(i) + "=" + att.getValue(i) );
			    	if (att.getQName(i).equalsIgnoreCase(ATTR_NAME))
			    	{
			        	String name = att.getValue(i);
			    		if ( name.isEmpty() )
			    		{
			    			return;
			    		}
			    		
			    		myInterface = myInterfaceList.getInterface(name);
			    		if ( myInterface == null )
			    		{
			    			// Create new interface
			    			myInterface = new CustomInterface( name );
			    			myInterfaceList.add(myInterface);
			    		}
			    	} // else ignore
			    	else
			    	{
						throw new Error( "The attribute '" + att.getQName(i) 
						                 + "' is not supported for configuration of the Selenium Plugin, element " + START_ELEMENT );
			    	}
			    }
	    	}
			Trace.append( Trace.SUITE, " )\n" );
	}

	@Override
	public void handleGoToChildElement(String aQualifiedName)
	{
		// nop
	}

	@Override
	public void handleReturnFromChildElement(String aQualifiedName, XmlHandler aChildXmlHandler) throws TTIException
	{
	    Trace.println(Trace.UTIL, "handleReturnFromChildElement( " + 
		    	      aQualifiedName + " )", true);
		    
		if (aQualifiedName.equalsIgnoreCase(CustomStepXmlHandler.START_ELEMENT))
    	{
    		if ( myInterface == null )
    		{
    			throw new TTIException( "The interface is not defined. Unable to add a step to an unknown interface" );
    		}
    		
    		if ( ! CustomInterface.class.isInstance(myInterface) )
    		{
    			throw new TTIException( "The " + myInterface.getInterfaceName() + " interface is not customizable. "
    			                        + "Unable to add a step to it." );
    		}

			try
			{
				myCustomStepXmlHandler.addTestStepExecutor( (CustomInterface) myInterface );
			}
			catch (TestSuiteException e)
			{
    			throw new TTIException( "Unable to add a step: " + e.getMessage(), e );
			}
			myCustomStepXmlHandler.reset();
    	}
		else
    	{ // Programming fault
			throw new Error( "Child XML Handler returned, but not recognized. The handler was probably defined " +
			                 "in the Constructor but not handled in handleReturnFromChildElement()");
		}
	}

//	/**
//	 * 
//	 */
//	public TestInterface getInterface()
//	{
//		return myInterface;
//	}

	public void reset()
	{
		myInterface = null;
	}
}
