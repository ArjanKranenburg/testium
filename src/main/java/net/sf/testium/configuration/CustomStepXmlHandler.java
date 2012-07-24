package net.sf.testium.configuration;

import java.util.ArrayList;

import net.sf.testium.executor.CustomInterface;
import net.sf.testium.executor.TestStepMetaExecutor;
import net.sf.testium.executor.general.CustomTestStepExecutor;
import net.sf.testium.executor.general.SpecifiedParameter;

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
 *  <parameters>...</parameters>
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
    private ArrayList<SpecifiedParameter> myParameterSpecs;

	private GenericTagAndStringXmlHandler myDescriptionXmlHandler;
	private TestStepSequenceXmlHandler myExecutionXmlHandler;
	private ParameterSpecificationXmlHandler myParameterSpecXmlHandler;

	private TestStepMetaExecutor myTestStepExecutor;

	public CustomStepXmlHandler(XMLReader anXmlReader, TestInterfaceList anInterfaceList, TestStepMetaExecutor aTestStepMetaExecutor )
	{
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);

		myTestStepExecutor = aTestStepMetaExecutor;

	    myDescriptionXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, DESCRIPTION_ELEMENT);
		this.addElementHandler(myDescriptionXmlHandler);

//	    ArrayList<TestStep.StepType> execAllowedTypes = new ArrayList<TestStep.StepType>();
//	    execAllowedTypes.add( TestStep.StepType.action );
//	    execAllowedTypes.add( TestStep.StepType.check );
//	    execAllowedTypes.add( TestStep.StepType.set );
		myExecutionXmlHandler = new TestStepSequenceXmlHandler( anXmlReader,
		                                                        EXECUTE_ELEMENT,
//		                                                        execAllowedTypes,
		                                                        anInterfaceList,
		                                                        true );
		this.addElementHandler(myExecutionXmlHandler);

		myParameterSpecXmlHandler = new ParameterSpecificationXmlHandler(anXmlReader);
		this.addElementHandler(myParameterSpecXmlHandler);
		
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
    	else if (aQualifiedName.equalsIgnoreCase(ParameterSpecificationXmlHandler.START_ELEMENT))
    	{
			SpecifiedParameter parameter = myParameterSpecXmlHandler.getParameterSpec();
			myParameterSpecs.add(parameter);
    		
			myParameterSpecXmlHandler.reset();
    	}
		else
    	{ // Programming fault
			throw new Error( "Child XML Handler returned, but not recognized. The handler was probably defined " +
			                 "in the Constructor but not handled in handleReturnFromChildElement()");
		}
	}

	/**
	 * @param  anInterface	the CustomInterface where steps can be added to
     * @throws TestSuiteException 
     */
    public void addTestStepExecutor( CustomInterface anInterface ) throws TestSuiteException
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
		                                                                      myParameterSpecs,
		                                                                      myExecutionSteps,
		                                                                      myTestStepExecutor );
		
		anInterface.add(testStepExecutor);
    }

	public void reset()
	{
		myCommand = "";
		myDescription = "";
	    myExecutionSteps = new TestStepSequence();
	    myParameterSpecs = new ArrayList<SpecifiedParameter>();
	}
}
