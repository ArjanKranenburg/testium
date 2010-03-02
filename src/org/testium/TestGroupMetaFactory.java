package org.testium;

import java.util.ArrayList;
import java.util.Hashtable;

import org.testtoolinterfaces.testsuite.TestCaseFactory;
import org.testtoolinterfaces.testsuite.TestEntryArrayList;
import org.testtoolinterfaces.testsuite.TestGroup;
import org.testtoolinterfaces.testsuite.TestGroupFactory;
import org.testtoolinterfaces.testsuite.TestScript;
import org.testtoolinterfaces.testsuite.TestStepArrayList;
import org.testtoolinterfaces.utils.Trace;


public class TestGroupMetaFactory implements TestGroupFactory
{
	private Hashtable<String, TestGroupFactory> myFactories;
	private TestCaseFactory myTestCaseFactory;

	public TestGroupMetaFactory( Hashtable<String, TestGroupFactory> aTgFactories, TestCaseFactory aTestCaseFactory )
	{
		myFactories = aTgFactories;
		myTestCaseFactory = aTestCaseFactory;
	}
	
	/* (non-Javadoc)
	 * @see org.testtoolinterfaces.testsuite.TestGroupFactory#create(java.lang.String, java.lang.String, int, java.lang.String, java.util.ArrayList, org.testtoolinterfaces.testsuite.TestStepArrayList, org.testtoolinterfaces.testsuite.TestEntryArrayList, java.io.File, org.testtoolinterfaces.testsuite.TestStepArrayList)
	 */
	public TestGroup create( String anId,
							 String aType,
							 int aSequence,
							 String aDescription,
							 ArrayList<String> aRequirementIds, 
							 TestStepArrayList anInitializationSteps, 
							 TestEntryArrayList aTestEntries,
							 TestScript aTestGroupScript, 
							 TestStepArrayList aRestoreSteps )
	{
		Trace.println( Trace.SUITE,
				   "create( " + anId + ", " 
				   			  + aType + ", "
				   			  + aSequence + ", "
				   			  + aDescription + ", "
				   			  + aRequirementIds.hashCode() + ", "
				   			  + anInitializationSteps + ", "
				   			  + aTestEntries + ", "
				   			  + aTestGroupScript + ", "
				   			  + aRestoreSteps + " )",
				   true );

		TestGroupFactory factory;
		if (myFactories.containsKey( aType ))
		{
			factory = myFactories.get( aType );
		}
		else
		{
			factory = myFactories.get( "standard" );
		}
		return factory.create( anId,
							   aType,
							   aSequence,
							   aDescription,
							   aRequirementIds,
							   anInitializationSteps,
							   aTestEntries,
							   aTestGroupScript,
							   aRestoreSteps
					  		 );
	}

	public TestCaseFactory getTestCaseFactory()
	{
		return myTestCaseFactory;
	}
}
