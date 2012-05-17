package net.sf.testium.plugins;

import net.sf.testium.MetaTestCaseResultWriter;
import net.sf.testium.MetaTestGroupResultWriter;
import net.sf.testium.MetaTestRunResultWriter;
import net.sf.testium.executor.DefaultInterface;
import net.sf.testium.executor.SupportedInterfaceList;
import net.sf.testium.executor.TestCaseExecutor;
import net.sf.testium.executor.TestCaseExecutorImpl;
import net.sf.testium.executor.TestCaseMetaExecutor;
import net.sf.testium.executor.TestGroupExecutor;
import net.sf.testium.executor.TestGroupExecutorImpl;
import net.sf.testium.executor.TestGroupMetaExecutor;
import net.sf.testium.executor.TestStepMetaExecutor;
import net.sf.testium.executor.TestStepScriptExecutor;
import net.sf.testium.systemundertest.DummySutControl;
import net.sf.testium.systemundertest.SutControl;
import net.sf.testium.systemundertest.SutInterface;

//import net.sf.testium.executor.TestStepCommandExecutor;
//import net.sf.testium.executor.WaitCommand;
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
