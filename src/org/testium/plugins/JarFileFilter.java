/**
 * 
 */
package org.testium.plugins;

import java.io.File;
import java.io.FileFilter;

/**
 * @author Arjan Kranenburg
 *
 */
public class JarFileFilter implements FileFilter
{
	/* (non-Javadoc)
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File aFile)
	{
	     return aFile.getName().toLowerCase().endsWith(".jar");
	}

}

