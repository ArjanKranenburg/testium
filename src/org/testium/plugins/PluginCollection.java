package org.testium.plugins;

import java.util.ArrayList;
import java.util.Hashtable;

import org.testium.TestCaseMetaFactory;
import org.testium.TestGroupMetaFactory;
import org.testium.TestStepMetaFactory;
import org.testium.executor.TestCaseExecutor;
import org.testium.executor.TestCaseExecutorImpl;
import org.testium.executor.TestCaseScriptExecutor;
import org.testium.executor.TestCaseScriptMetaExecutor;
import org.testium.executor.TestGroupExecutor;
import org.testium.executor.TestGroupExecutorImpl;
import org.testium.executor.TestGroupLinkExecutor;
import org.testium.executor.TestGroupLinkExecutorImpl;
import org.testium.executor.TestGroupMetaExecutor;
import org.testium.executor.TestStepExecutor;
import org.testium.executor.TestStepMetaExecutor;
import org.testium.executor.TestStepWaitExecutor;
import org.testium.executor.TestSuiteExecutor;
import org.testium.executor.TestSuiteExecutorImpl;
import org.testium.systemundertest.DummySutControl;
import org.testium.systemundertest.SutControl;
import org.testtoolinterfaces.testresultinterface.TestRunResultWriter;
import org.testtoolinterfaces.testsuite.TestCaseFactory;
import org.testtoolinterfaces.testsuite.TestCaseFactoryImpl;
import org.testtoolinterfaces.testsuite.TestGroupFactory;
import org.testtoolinterfaces.testsuite.TestGroupFactoryImpl;
import org.testtoolinterfaces.testsuite.TestStepFactory;
import org.testtoolinterfaces.testsuite.TestStepFactoryImpl;
import org.testtoolinterfaces.testsuiteinterface.TestGroupReader;


public class PluginCollection
{
	private TestGroupReader	myTestGroupReader;
	private ArrayList<TestRunResultWriter>	myTestRunResultWriters;
	
	private Hashtable<String, TestStepFactory>	myTestStepFactories;
	private Hashtable<String, TestCaseFactory>	myTestCaseFactories;
	private Hashtable<String, TestGroupFactory>	myTestGroupFactories;

	private TestStepFactory	myTestStepFactory;
	private TestCaseFactory	myTestCaseFactory;
	private TestGroupFactory	myTestGroupFactory;

	private Hashtable<String, TestStepExecutor>	myTestStepExecutors;
	private Hashtable<String, TestCaseScriptExecutor>	myTestCaseScriptExecutors;

	private TestStepExecutor	myTestStepExecutor;
	private TestCaseExecutor	myTestCaseExecutor;
	private TestCaseScriptExecutor	myTestCaseScriptExecutor;
	private TestGroupExecutor	myTestGroupExecutor;
	private TestGroupLinkExecutor myTestGroupLinkExecutor;
	private TestSuiteExecutor	myTestSuiteExecutor;

	private SutControl	mySutControl;
	
	/**
	 * 
	 */
	public PluginCollection()
	{
		myTestStepFactories = new Hashtable<String, TestStepFactory>();
		myTestCaseFactories = new Hashtable<String, TestCaseFactory>();
		myTestGroupFactories = new Hashtable<String, TestGroupFactory>();

		myTestStepExecutors = new Hashtable<String, TestStepExecutor>();
		myTestCaseScriptExecutors = new Hashtable<String, TestCaseScriptExecutor>();

		myTestRunResultWriters = new ArrayList<TestRunResultWriter>();
		
		// Default SUT Control
		mySutControl = new DummySutControl( );

		// Default Factories
		addTestStepFactory("standard", new TestStepFactoryImpl( ));
		myTestStepFactory = new TestStepMetaFactory( myTestStepFactories );
		
		addTestCaseFactory("standard", new TestCaseFactoryImpl( myTestStepFactory ));
		myTestCaseFactory = new TestCaseMetaFactory( myTestCaseFactories, myTestStepFactory );

		addTestGroupFactory("standard", new TestGroupFactoryImpl( myTestCaseFactory ));
		myTestGroupFactory = new TestGroupMetaFactory( myTestGroupFactories, myTestCaseFactory );
		
		// Default Readers
		myTestGroupReader = new TestGroupReader( myTestGroupFactory );

		// Default Executors
		addTestStepExecutor(new TestStepWaitExecutor());
		myTestStepExecutor = new TestStepMetaExecutor( myTestStepExecutors );
		
		myTestCaseExecutor = new TestCaseExecutorImpl( myTestStepExecutor );
		myTestCaseScriptExecutor = new TestCaseScriptMetaExecutor( myTestCaseScriptExecutors );
		
		TestGroupExecutor testGroupExecutor = new TestGroupExecutorImpl( myTestStepExecutor,
														 myTestCaseScriptExecutor );
		testGroupExecutor.setTestCaseExecutor( myTestCaseExecutor );

		myTestGroupLinkExecutor = new TestGroupLinkExecutorImpl( myTestGroupReader, testGroupExecutor );
		testGroupExecutor.setTestGroupLinkExecutor(myTestGroupLinkExecutor);

		myTestGroupExecutor = new TestGroupMetaExecutor( testGroupExecutor,
		                                                 myTestGroupLinkExecutor );
		
		myTestSuiteExecutor = new TestSuiteExecutorImpl( myTestGroupExecutor, mySutControl );
	}

	/**
	 * @return the myTestRunResultXmlWriter
	 */
	public ArrayList<TestRunResultWriter> getTestRunResultWriters()
	{
		return myTestRunResultWriters;
	}

	/**
	 * @param aTestRunResultXmlWriter the TestRunResultXmlWriter to set
	 */
	public void addTestRunResultWriter( TestRunResultWriter aTestRunResultWriter )
	{
		myTestRunResultWriters.add( aTestRunResultWriter );
	}

	/**
	 * @return the myTestSuiteReader
	 */
	public TestGroupReader getTestGroupReader()
	{
		return myTestGroupReader;
	}

	/**
	 * @param aTestSuiteReader the TestSuiteReader to set
	 */
	public void setTestGroupReader(TestGroupReader aTestGroupReader)
	{
		myTestGroupReader = aTestGroupReader;
		if (myTestGroupLinkExecutor.getClass().isInstance(new TestGroupLinkExecutorImpl( myTestGroupReader, myTestGroupExecutor )))
		{
			((TestGroupLinkExecutorImpl) myTestGroupLinkExecutor).setTestGroupReader(aTestGroupReader);
		}
	}

	// Factories
	/**
	 * @return the myTestStepFactory
	 */
	public TestStepFactory getTestStepFactory()
	{
		return myTestStepFactory;
	}

	/**
	 * @param aTestStepFactory the TestStepFactory to add
	 */
	public void addTestStepFactory(String aType, TestStepFactory aTestStepFactory)
	{
		myTestStepFactories.put(aType, aTestStepFactory);
	}

	/**
	 * @return the myTestCaseFactory
	 */
	public TestCaseFactory getTestCaseFactory()
	{
		return myTestCaseFactory;
	}

	/**
	 * @param aTestCaseFactory the TestCaseFactory to add
	 */
	public void addTestCaseFactory(String aType, TestCaseFactory aTestCaseFactory)
	{
		myTestCaseFactories.put(aType, aTestCaseFactory);
	}

	/**
	 * @return the myTestGroupFactory
	 */
	public TestGroupFactory getTestGroupFactory()
	{
		return myTestGroupFactory;
	}

	/**
	 * @param aTestGroupFactory the TestGroupFactory to add
	 */
	public void addTestGroupFactory(String aType, TestGroupFactory aTestGroupFactory)
	{
		myTestGroupFactories.put(aType, aTestGroupFactory);
	}

	// Executors
	/**
	 * @return the myTestStepExecutor
	 */
	public TestStepExecutor getTestStepExecutor()
	{
		return myTestStepExecutor;
	}

	/**
	 * @param aTestStepExecutor the TestStepExecutor to add
	 */
	public void addTestStepExecutor(TestStepExecutor aTestStepExecutor)
	{
		myTestStepExecutors.put(aTestStepExecutor.getCommand(), aTestStepExecutor);
	}

	/**
	 * @return the myTestCaseExecutor
	 */
	public TestCaseExecutor getTestCaseExecutor()
	{
		return myTestCaseExecutor;
	}

	/**
	 * @param aTestCaseExecutor the TestCaseExecutor to set
	 */
	public void setTestCaseExecutor(TestCaseExecutor aTestCaseExecutor)
	{
		myTestCaseExecutor = aTestCaseExecutor;
		myTestGroupExecutor.setTestCaseExecutor( aTestCaseExecutor );
	}

	/**
	 * @return the myTestCaseScriptExecutor
	 */
	public TestCaseScriptExecutor getTestCaseScriptExecutor()
	{
		return myTestCaseScriptExecutor;
	}

	/**
	 * @param aTestStepExecutor the TestCaseScriptExecutor to add
	 */
	public void addTestCaseScriptExecutor(TestCaseScriptExecutor aTestCaseScriptExecutor)
	{
		myTestCaseScriptExecutors.put(aTestCaseScriptExecutor.getScriptType(), aTestCaseScriptExecutor);
	}

	/**
	 * @return the myTestGroupExecutor
	 */
	public TestGroupExecutor getTestGroupExecutor()
	{
		return myTestGroupExecutor;
	}

	/**
	 * @param aTestCaseExecutor the TestCaseExecutor to set
	 */
	public void setTestGroupExecutor(TestGroupExecutor aTestGroupExecutor)
	{
		myTestGroupExecutor = aTestGroupExecutor;
		myTestSuiteExecutor.setTestGroupExecutor( aTestGroupExecutor );
	}

	/**
	 * @return the myTestCaseScriptExecutor
	 */
	public TestGroupLinkExecutor getTestGroupLinkExecutor()
	{
		return myTestGroupLinkExecutor;
	}

	/**
	 * @param aTestGroupLinkExecutor the TestGroupLinkExecutor to set
	 */
	public void setTestGroupLinkExecutor(TestGroupLinkExecutor aTestGroupLinkExecutor)
	{
		myTestGroupLinkExecutor = aTestGroupLinkExecutor;
		myTestGroupExecutor.setTestGroupLinkExecutor( aTestGroupLinkExecutor );
	}

	/**
	 * @return the myTestSuiteExecutor
	 */
	public TestSuiteExecutor getTestSuiteExecutor()
	{
		return myTestSuiteExecutor;
	}

	/**
	 * @param aTestSuiteExecutor the TestSuiteExecutor to set
	 */
	public void setTestSuiteExecutor(TestSuiteExecutor aTestSuiteExecutor)
	{
		myTestSuiteExecutor = aTestSuiteExecutor;
	}
	
	/**
	 * @return the mySutControl
	 */
	public SutControl getSutControl()
	{
		return mySutControl;
	}

	/**
	 * @param aSutControl the SutControl to set
	 */
	public void setSutControl(SutControl aSutControl)
	{
		mySutControl = aSutControl;
		myTestSuiteExecutor.setSutControl(aSutControl);
	}
}
