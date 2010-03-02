package org.testium.plugins;

import org.testium.configuration.Configuration;
import org.testium.configuration.ConfigurationException;

/*
 * Interface for plugin classes
 */
public interface Plugin
{
    public void loadPlugIn( PluginCollection aPluginCollection, Configuration aConfig ) throws ConfigurationException;
}