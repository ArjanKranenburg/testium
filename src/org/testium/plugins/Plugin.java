package org.testium.plugins;

import org.testium.configuration.ConfigurationException;
import org.testtoolinterfaces.utils.RunTimeData;

/*
 * Interface for plugin classes
 */
public interface Plugin
{
    public void loadPlugIn( PluginCollection aPluginCollection, RunTimeData aRtData ) throws ConfigurationException;
}