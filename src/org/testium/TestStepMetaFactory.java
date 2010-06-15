package org.testium;

import java.util.Hashtable;

import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestStepFactory;
import org.testtoolinterfaces.testsuite.TestStep.ActionType;
import org.testtoolinterfaces.utils.Trace;


public class TestStepMetaFactory implements TestStepFactory
{
	private Hashtable<String, TestStepFactory> myFactories;

	public TestStepMetaFactory( Hashtable<String, TestStepFactory> aTsFactories )
	{
		Trace.println( Trace.CONSTRUCTOR,
		               "TestStepMetaFactory( " + aTsFactories.size() + " TestStepFactory )",
		               true );

		myFactories = aTsFactories;
	}
	
	public TestStep create( ActionType anActionType,
							String aType,
	                        int aSequence,
	                        String aDescription,
	                        String aCommand,
	                        ParameterArrayList aParameters )
	{
		Trace.println(Trace.SUITE, "create( " + anActionType + ", " 
		              						  + aType + ", "
					   			  			  + aSequence + ", "
					   			  			  + aDescription + ", "
					   			  			  + aCommand + ", "
					   			  			  + aParameters.size() + " )", true );


		TestStepFactory factory;
		if (myFactories.containsKey( aType ))
		{
			factory = myFactories.get( aType );
		}
		else
		{
			factory = myFactories.get( "standard" );
		}
		return factory.create( anActionType,
		                       aType,
							   aSequence,
							   aDescription,
							   aCommand,
							   aParameters );
	}
}
