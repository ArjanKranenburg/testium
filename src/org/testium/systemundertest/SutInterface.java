/**
 * 
 */
package org.testium.systemundertest;

import java.util.ArrayList;

/**
 * @author Arjan Kranenburg
 */
public interface SutInterface
{
	public String getInterfaceName();
	
	ArrayList<SutIfCommand> getCommands();
}