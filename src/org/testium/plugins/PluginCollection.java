package org.testium.plugins;

import org.testium.MetaTestCaseResultWriter;
import org.testium.MetaTestGroupResultWriter;
import org.testium.MetaTestRunResultWriter;
import org.testium.executor.DefaultInterface;
import org.testium.executor.SupportedInterfaceList;
import org.testium.executor.TestCaseExecutor;
import org.testium.executor.TestCaseExecutorImpl;
import org.testium.executor.TestCaseMetaExecutor;
import org.testium.executor.TestGroupExecutor;
import org.testium.executor.TestGroupExecutorImpl;
import org.testium.executor.TestGroupMetaExecutor;
//import org.testium.executor.TestStepCommandExecutor;
import org.testium.executor.TestStepMetaExecutor;
import org.testium.executor.TestStepScriptExecutor;
//import org.testium.executor.WaitCommand;
import org.testium.systemundertest.DummySutControl;
import org.testium.systemundertest.SutControl;
import org.testium.systemundertest.SutInterface;
import org.testtoolinterfaces.testresultinterface.TestCaseResultWriter;
import org.testtoolinterfaces.testresultinterface.TestGroupResultWriter;
import org.testtoolinterfaces.testresultinterface.TestRunResultWriter;
import org.testtoolinterfaces.testsuiteinterface.TestGroupReader;

public class PluginCollection
{
	private TestGroupReader	myTestGroupReader;

	private MetaTestRunResultWriter myTestRunResultWriter;
	private MetaTestGroupResultWriter myTestGroupResultWriter;
	private MetaTestCaseResultWriter myTestCaseResultWriter;

	private TestStepMetaExecutor	myTestStepExecutor;
	private TestCaseMetaExecutor	myTestCaseExecutor;
	private TestGroupMetaExecutor	myTestGroupExecutor;

	private SutControl	mySutControl;

	/**
	 * 
	 */
	public PluginCollection()
	{
		// Default Writers
		myTestRunResultWriter = new MetaTestRunResultWriter();
		myTestGroupResultWriter = new MetaTestGroupResultWriter();
		myTestCaseResultWriter = new MetaTestCaseResultWriter();

		// Default SUT Control
		mySutControl = new DummySutControl( );

		// Default Executors
		myTestStepExecutor = new TestStepMetaExecutor();
		addSutInterface(new DefaultInterface());
//		addStepCommandExecutor(new WaitCommand());
		
		addSutInterface(mySutControl);

		myTestCaseExecutor = new TestCaseMetaExecutor();
		addTestCaseExecutor(new TestCaseExecutorImpl( myTestStepExecutor, myTestCaseResultWriter ));
		
		myTestGroupExecutor = new TestGroupMetaExecutor();

		TestGroupExecutorImpl testGroupExecutor = new TestGroupExecutorImpl( myTestStepExecutor,
		                                                                     myTestCaseExecutor,
		                                                                     myTestGroupExecutor,
		                                                                     myTestGroupResultWriter );
		addTestGroupExecutor(testGroupExecutor);

		// Default Readers
		myTestGroupReader = new TestGroupReader(myTestStepExecutor.getInterfaces(), true);
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
	}

	// Executors
	/**
	 * @return the myTestStepExecutor
	 */
	public TestStepMetaExecutor getTestStepExecutor()
	{
		return myTestStepExecutor;
	}

	public SupportedInterfaceList getInterfaces()
	{
		return myTestStepExecutor.getInterfaces();
	}

//	/**
//	 * @param aTestStepCommandExecutor the TestStepCommandExecutor to add
//	 */
//	public void addStepCommandExecutor(TestStepCommandExecutor aTestStepCommandExecutor)
//	{
//		myTestStepExecutor.addCommandExecutor(aTestStepCommandExecutor);
//	}

	/**
	 * @param aSutInterface the SutInterface to add
	 */
	public void addSutInterface(SutInterface aSutInterface)
	{
		myTestStepExecutor.addSutInterface(aSutInterface);
	}

	/**
	 * @param aTestStepScriptExecutor the TestStepScriptExecutor to add
	 */
	public void addStepScriptExecutor(TestStepScriptExecutor aTestStepScriptExecutor)
	{
		myTestStepExecutor.addScriptExecutor(aTestStepScriptExecutor);
	}

	/**
	 * @return the myTestCaseExecutor
	 */
	public TestCaseMetaExecutor getTestCaseExecutor()
	{
		return myTestCaseExecutor;
	}

	/**
	 * @param aTestCaseExecutor the TestCaseExecutor to add
	 */
	public void addTestCaseExecutor(TestCaseExecutor aTestCaseExecutor)
	{
		myTestCaseExecutor.put(aTestCaseExecutor.getType(), aTestCaseExecutor);
	}

	/**
	 * @return the myTestGroupExecutor
	 */
	public TestGroupMetaExecutor getTestGroupExecutor()
	{
		return myTestGroupExecutor;
	}

	/**
	 * @param aTestGroupExecutor the TestGroupExecutor to add
	 */
	public void addTestGroupExecutor(TestGroupExecutor aTestGroupExecutor)
	{
		myTestGroupExecutor.put(aTestGroupExecutor.getType(), aTestGroupExecutor);
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
	}

	public TestCaseResultWriter getTestCaseResultWriter()
	{
		return myTestCaseResultWriter;
	}

	public void addTestCaseResultWriter(TestCaseResultWriter aCaseResultWriter)
	{
		myTestCaseResultWriter.add(aCaseResultWriter);		
	}

	/**
	 * @return the myTestGroupResultWriter
	 */
	public TestGroupResultWriter getTestGroupResultWriter()
	{
		return myTestGroupResultWriter;
	}

	public void addTestGroupResultWriter(TestGroupResultWriter aGroupResultWriter)
	{
		myTestGroupResultWriter.add( aGroupResultWriter );
	}

	/**
	 * @return the myTestRunResultWriter
	 */
	public TestRunResultWriter getTestRunResultWriter()
	{
		return myTestRunResultWriter;
	}

	public void addTestRunResultWriter(TestRunResultWriter aRunResultWriter)
	{
		myTestRunResultWriter.add( aRunResultWriter );
	}
}
