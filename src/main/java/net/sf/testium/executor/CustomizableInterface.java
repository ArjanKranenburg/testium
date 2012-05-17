/**
 * 
 */
package net.sf.testium.executor;

/**
 * @author Arjan
 *
 */
public interface CustomizableInterface
{
	public String getInterfaceName();

	/**
	 * Adds a TestStep Command Executor to the interface
	 * @param aCommandExecutor
	 */
	public void add( TestStepCommandExecutor aCommandExecutor );
}
