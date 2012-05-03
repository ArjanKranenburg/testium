package org.testium.configuration;

import org.testium.executor.SupportedInterfaceList;
import org.testium.executor.TestStepMetaExecutor;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;


public class InterfacesXmlHandler extends XmlHandler
{
	public static final String START_ELEMENT = "Interfaces";

	private InterfaceXmlHandler myInterfaceXmlHandler;
	public InterfacesXmlHandler(XMLReader anXmlReader, SupportedInterfaceList anInterfaceList, TestStepMetaExecutor aTestStepMetaExecutor)
	{
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);

	    myInterfaceXmlHandler = new InterfaceXmlHandler( anXmlReader, anInterfaceList, aTestStepMetaExecutor );
		this.addElementHandler(InterfaceXmlHandler.START_ELEMENT, myInterfaceXmlHandler);

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
		// nop
	}

	@Override
	public void handleGoToChildElement(String aQualifiedName)
	{
		// nop
	}

	@Override
	public void handleReturnFromChildElement(String aQualifiedName, XmlHandler aChildXmlHandler)
	{
	    Trace.println(Trace.UTIL, "handleReturnFromChildElement( " + 
		    	      aQualifiedName + " )", true);
		    
		if (aQualifiedName.equalsIgnoreCase(InterfaceXmlHandler.START_ELEMENT))
    	{
			// The interfaceList is already updated
			myInterfaceXmlHandler.reset();
    	}
		else
    	{ // Programming fault
			throw new Error( "Child XML Handler returned, but not recognized. The handler was probably defined " +
			                 "in the Constructor but not handled in handleReturnFromChildElement()");
		}
	}

	public void reset()
	{
		myInterfaceXmlHandler.reset();
	}
}
