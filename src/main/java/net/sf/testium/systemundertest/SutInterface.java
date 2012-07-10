/**
 * 
 */
package net.sf.testium.systemundertest;

import java.util.ArrayList;

import net.sf.testium.executor.TestStepCommandExecutor;

import org.testtoolinterfaces.testsuite.TestInterface;

/**
 * @author Arjan Kranenburg
 */
public interface SutInterface extends TestInterface
{
	public String getInterfaceName();
	
	ArrayList<TestStepCommandExecutor> getCommandExecutors();

	TestStepCommandExecutor getCommandExecutor( String aCommand );
}