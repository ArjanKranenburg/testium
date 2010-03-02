/**
 * 
 */
package org.testium.systemundertest;

import java.io.File;
import java.util.ArrayList;

import org.testium.executor.runTimeData;

/**
 * @author Arjan Kranenburg
 */
public interface SutIfCommand
{
	public String getName();
	
	public boolean doAction(runTimeData aVariables, File aLogDir);

	public boolean verifyParameters(runTimeData aVariables);
	
	public ArrayList<Parameter> getParameters();
}