package org.testium.configuration;

import org.testium.executor.CustomTestStepExecutor;
import org.testium.executor.CustomizableInterface;
import org.testium.executor.TestStepMetaExecutor;
import org.testtoolinterfaces.testsuite.Parameter;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestInterfaceList;
import org.testtoolinterfaces.testsuite.TestStepSequence;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.testsuiteinterface.ParameterXmlHandler;
import org.testtoolinterfaces.testsuiteinterface.TestStepSequenceXmlHandler;
import org.testtoolinterfaces.utils.GenericTagAndStringXmlHandler;
import org.testtoolinterfaces.utils.TTIException;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

/**
 * @author Arjan Kranenburg 
 * 
 * <customstep command="...">
 *  <description>...</description>
 *  <execute>
 *   ...
 *  </execute>
 * </customstep>
 */

public class CustomStepXmlHandler extends XmlHandler
{
	public static final String START_ELEMENT = "customstep";

	private static final String	ATTR_COMMAND			= "command";
	private static final String DESCRIPTION_ELEMENT 	= "description";
	private static final String EXECUTE_ELEMENT 		= "execute";

	private String myCommand;
	private String myDescription;
    private TestStepSequence myExecutionSteps;
	private ParameterArrayList myParameters;

	private GenericTagAndStringXmlHandler myDescriptionXmlHandler;
	private TestStepSequenceXmlHandler myExecutionXmlHandler;
	private ParameterXmlHandler myParameterXmlHandler;

	private TestStepMetaExecutor myTestStepExecutor;

	public CustomStepXmlHandler(XMLReader anXmlReader, TestInterfaceList anInterfaceList, TestStepMetaExecutor aTestStepMetaExecutor )
	{
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);

		myTestStepExecutor = aTestStepMetaExecutor;

	    myDescriptionXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, DESCRIPTION_ELEMENT);
		this.addElementHandler(DESCRIPTION_ELEMENT, myDescriptionXmlHandler);

//	    ArrayList<TestStep.StepType> execAllowedTypes = new ArrayList<TestStep.StepType>();
//	    execAllowedTypes.add( TestStep.StepType.action );
//	    execAllowedTypes.add( TestStep.StepType.check );
//	    execAllowedTypes.add( TestStep.StepType.set );
		myExecutionXmlHandler = new TestStepSequenceXmlHandler( anXmlReader,
		                                                        EXECUTE_ELEMENT,
//		                                                        execAllowedTypes,
		                                                        anInterfaceList,
		                                                        true );
		this.addElementHandler(EXECUTE_ELEMENT, myExecutionXmlHandler);

		myParameterXmlHandler = new ParameterXmlHandler(anXmlReader);
		this.addElementHandler(ParameterXmlHandler.START_ELEMENT, myParameterXmlHandler);
		
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
			    	if (att.getQName(i).equalsIgnoreCase(ATTR_COMMAND))
			    	{
			        	myCommand = att.getValue(i);
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
		    
		if (aQualifiedName.equalsIgnoreCase(DESCRIPTION_ELEMENT))
    	{
    		myDescription  = myDescriptionXmlHandler.getValue();
        	myDescriptionXmlHandler.reset();
    	}
    	else if (aQualifiedName.equalsIgnoreCase(EXECUTE_ELEMENT))
    	{
    		myExecutionSteps = myExecutionXmlHandler.getSteps();
    		myExecutionXmlHandler.reset();
    	}
    	else if (aQualifiedName.equalsIgnoreCase(ParameterXmlHandler.START_ELEMENT))
    	{
			// Note: No interface is set, so creation is done by the defaultInterface
			Parameter parameter = myParameterXmlHandler.getParameter();
			myParameters.add(parameter);
    		
    		myParameterXmlHandler.reset();
    	}
		else
    	{ // Programming fault
			throw new Error( "Child XML Handler returned, but not recognized. The handler was probably defined " +
			                 "in the Constructor but not handled in handleReturnFromChildElement()");
		}
	}

	/**
	 * @param  anInterface	the CustomizableInterface where steps can be added to
     * @throws TestSuiteException 
     */
    public void addTestStepExecutor( CustomizableInterface anInterface ) throws TestSuiteException
    {
		Trace.println(Trace.SUITE);

		if ( myCommand.isEmpty() )
		{
			throw new TestSuiteException("Unknown TestStep Command");
		}

		if ( myExecutionSteps.isEmpty() )
		{
			throw new TestSuiteException("No Execution Steps found", myCommand);
		}

		CustomTestStepExecutor testStepExecutor = new CustomTestStepExecutor( myCommand,
		                                                                      myDescription,
		                                                                      anInterface,
		                                                                      myExecutionSteps,
		                                                                      myParameters,
		                                                                      myTestStepExecutor );
		
		anInterface.add(testStepExecutor);
    }

	public void reset()
	{
		myCommand = "";
		myDescription = "";
	    myExecutionSteps = new TestStepSequence();
	    myParameters = new ParameterArrayList();
	}
}
