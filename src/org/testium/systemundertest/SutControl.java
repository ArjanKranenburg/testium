/**
 * 
 */
package org.testium.systemundertest;

import java.io.File;

import org.testtoolinterfaces.testresult.SutInfo;
import org.testtoolinterfaces.utils.RunTimeData;


/**
 * @author Arjan Kranenburg
 *
 */
public abstract class SutControl implements SutInterface
{
	public abstract String getName();
	
	public String getInterfaceName()
	{
		return "SUT";
	}
	
	public SutInfo getSutInfo(File aLogDir, RunTimeData aParentRtData)
	{
		return new SutInfo( this.getName() );
	}
}
