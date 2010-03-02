/**
 * Dynamic class loader
 * 
 * PluginClassLoader.java
 */

package org.testium.plugins;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import org.testium.configuration.Configuration;
import org.testium.configuration.ConfigurationException;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.Warning;


/*
 * Our own class for plugin class loading. 
 */
public class PluginClassLoader extends URLClassLoader
{
	private static PluginClassLoader pluginLoader;
    private static Plugin plugin;
    
    public PluginClassLoader(URL[] anUrlArray)
    {
        super(anUrlArray);
		Trace.print(Trace.LEVEL.CONSTRUCTOR, "PluginClassLoader( ", true);
    	for ( URL url : anUrlArray )
    	{
    		Trace.append(Trace.LEVEL.CONSTRUCTOR, url.toString() + " ");
    	}
		Trace.append(Trace.LEVEL.CONSTRUCTOR, " )\n");
    }
    
	@SuppressWarnings("unchecked")
	public Class findClass(String aClassName) throws ClassNotFoundException
    {
		Trace.println(Trace.LEVEL.UTIL, "findClass( " + aClassName + " )", true);
		return super.findClass(aClassName);
    }
    
	public static PluginCollection loadPlugins( File aPluginDir, Configuration aConfig ) throws ConfigurationException
	{
		Trace.println(Trace.LEVEL.UTIL, "loadPlugins( )", true);

    	ArrayList<URL> urlArray = new ArrayList<URL>();
    	for ( File file : aPluginDir.listFiles(new JarFileFilter()) )
    	{
    		try
    		{
				urlArray.add(file.toURI().toURL());
			}
    		catch (MalformedURLException e)
    		{
				throw new ConfigurationException( "File \"" + file.getPath() + "\" could not be found.", e );
			}
    	}
    	
    	URL[] urls = new URL[ urlArray.size() ];
    	pluginLoader = new PluginClassLoader(urlArray.toArray(urls));

    	PluginCollection pluginCollection = new PluginCollection();
    	
    	// Load default plugins
    	TestResultInterfacePlugin testResultInterfacePlugin = new TestResultInterfacePlugin();
    	testResultInterfacePlugin.loadPlugIn(pluginCollection, aConfig);
    	
    	// Load configured plugins
		ArrayList<String> pluginLoaders = aConfig.getPluginLoaders();
    	for ( String className : pluginLoaders )
    	{
			try
			{
				plugin = (Plugin) pluginLoader.findClass(className).newInstance();
			}
			catch (Throwable e)
			{
				throw new ConfigurationException( "Class \"" + className + "\" could not be loaded:\n" + e.getMessage(), e );
			}
			plugin.loadPlugIn( pluginCollection, aConfig );
    	}
    	
    	return pluginCollection;
	}
	
	public static void addJarToClassLoader(File aFile) throws MalformedURLException
	{
		Trace.println(Trace.LEVEL.UTIL, "addJarToClassLoader( " + aFile.getName() + " )", true);
		URL url = null;
		url = aFile.toURI().toURL();
		Class[] parameters = new Class[]{URL.class};

		URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
		Class<URLClassLoader> sysclass = URLClassLoader.class;
	 
		try
		{
			Method method = sysclass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			method.invoke(sysloader, new Object[] { url });
		}
		catch (Throwable t)
		{
			Warning.println(t.getMessage());
		}

	}

	public static void addDirToClassLoader(File aDir) throws MalformedURLException
	{
		Trace.println(Trace.LEVEL.UTIL, "addDirToClassLoader( " + aDir.getName() + " )", true);
    	if( !aDir.isDirectory() )
    	{
    		throw new MalformedURLException(aDir.getPath());
    	}
    	
    	for ( File file : aDir.listFiles(new JarFileFilter()) )
    	{
    		addJarToClassLoader( file );
    	}
	}
}
