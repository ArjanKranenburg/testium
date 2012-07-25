package net.sf.testium.configuration;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sf.testium.executor.CustomInterface;
import net.sf.testium.executor.TestStepMetaExecutor;

import org.testtoolinterfaces.testsuite.TestInterfaceList;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.GenericTagAndStringXmlHandler;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.TTIException;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @author Arjan Kranenburg 
 * 
 *  <CustomStepDefinitions>
 *    <CustomStepDefinitionsLink>...</CustomStepDefinitionsLink>
 *    <CustomStep>...</CustomStep>
 *  </CustomStepDefinitions>
 * 
 */

public class CustomStepDefinitionsXmlHandler extends XmlHandler
{
	private static final String START_ELEMENT = "CustomStepDefinitions";

	private static final String CUSTOMSTEP_DEFINITIONS_LINK_ELEMENT = "CustomStepDefinitionsLink";

	private CustomStepXmlHandler myCustomStepXmlHandler;
	private GenericTagAndStringXmlHandler myCustomStepDefinitionsLinkXmlHandler;

	private final RunTimeData myRtData;
	private final CustomInterface myInterface;
    private final TestInterfaceList myInterfaceList;
    private final TestStepMetaExecutor myTestStepMetaExecutor;
	
	public CustomStepDefinitionsXmlHandler( XMLReader anXmlReader, 
                                      RunTimeData anRtData,
                                      CustomInterface anInterface,
                                      TestInterfaceList anInterfaceList,
                                      TestStepMetaExecutor aTestStepMetaExecutor )
	{
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);

		myRtData = anRtData;
		myInterface = anInterface;
		myInterfaceList = anInterfaceList;
		myTestStepMetaExecutor = aTestStepMetaExecutor;
		
		myCustomStepXmlHandler = new CustomStepXmlHandler(anXmlReader, anInterfaceList, aTestStepMetaExecutor);
		this.addElementHandler(myCustomStepXmlHandler);

		myCustomStepDefinitionsLinkXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, CUSTOMSTEP_DEFINITIONS_LINK_ELEMENT);
		this.addElementHandler(myCustomStepDefinitionsLinkXmlHandler);
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
		throws TTIException
	{
	    Trace.println(Trace.UTIL, "handleReturnFromChildElement( " + 
		    	      aQualifiedName + " )", true);
		    
		if (aQualifiedName.equalsIgnoreCase( CUSTOMSTEP_DEFINITIONS_LINK_ELEMENT ))
    	{
			String fileName = myCustomStepDefinitionsLinkXmlHandler.getValue();
			myCustomStepDefinitionsLinkXmlHandler.reset();

			fileName = myRtData.substituteVars(fileName);
			try {
				CustomStepDefinitionsXmlHandler.loadElementDefinitions( new File( fileName ),
																  myRtData,
																  myInterface,
																  myInterfaceList,
																  myTestStepMetaExecutor );
			} catch (ConfigurationException ce) {
				throw new TTIException( "Failed to load element Definitions from file: " + fileName, ce );
			}
    	}
		else if ( aQualifiedName.equalsIgnoreCase(myCustomStepXmlHandler.getStartElement()) )
    	{
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
		// else ignore
	}

	public static void loadElementDefinitions( File aFile, 
											   RunTimeData anRtData,
											   CustomInterface anInterface,
											   TestInterfaceList anInterfaceList,
											   TestStepMetaExecutor aTestStepMetaExecutor )
			throws ConfigurationException {
		Trace.println(Trace.UTIL, "loadElementDefinitions( " + aFile.getName()
				+ " )", true);
		// create a parser
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(false);
		SAXParser saxParser;
		try {
			saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();

			// create a handler
			CustomStepDefinitionsXmlHandler handler = new CustomStepDefinitionsXmlHandler( xmlReader,
																			   anRtData,
																			   anInterface,
																			   anInterfaceList,
																			   aTestStepMetaExecutor );

			// assign the handler to the parser
			xmlReader.setContentHandler(handler);

			// parse the document
			xmlReader.parse(aFile.getAbsolutePath());
		} catch (ParserConfigurationException e) {
			Trace.print(Trace.UTIL, e);
			throw new ConfigurationException(e);
		} catch (SAXException e) {
			Trace.print(Trace.UTIL, e);
			throw new ConfigurationException(e);
		} catch (IOException e) {
			Trace.print(Trace.UTIL, e);
			throw new ConfigurationException(e);
		}
	}
}
