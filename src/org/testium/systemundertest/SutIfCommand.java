/**
 * 
 */
package org.testium.systemundertest;

import java.io.File;
import java.util.ArrayList;

import org.testtoolinterfaces.utils.RunTimeData;

/**
 * @author Arjan Kranenburg
 */
public interface SutIfCommand
{
	public String getName();
	
	public boolean doAction(RunTimeData aVariables, File aLogDir);

	public boolean verifyParameters(RunTimeData aVariables);
	
	public ArrayList<Parameter> getParameters();
}