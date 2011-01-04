package org.gnomiki.plugins;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gnomiki.config.Configuration;

/**
 * Configure this with spring by inserting a Configuration to property config.
 * 
 * @author MicWin
 * 
 */
public class ConfigurationPluginManager implements IPluginManager {

	private final Log L = LogFactory.getLog(ConfigurationPluginManager.class);

	Configuration config;

	Map<String, IPlugin> pluginsById = new HashMap<String, IPlugin>();

	public ConfigurationPluginManager(Configuration config) {
		this.config = config;
	}

	public IPlugin getPlugin(String pluginId) {
		return pluginsById.get(pluginId);
	}

	public void init() {
		String[] pluginClasses = config.getStringArray("plugins");
		for (int i = 0; i < pluginClasses.length; i++) {
			String className = pluginClasses[i];
			try {
				Class clazz = Class.forName(className);
				IPlugin plugin = (IPlugin) clazz.newInstance();
				plugin.init(this);
				pluginsById.put(plugin.getPluginId(), plugin);
				L.info("registered Plugin '" + plugin.getPluginId() + "'");
			} catch (Exception e) {
				L.error("Cannot laod plugin class '" + className + "'",
						e);
			}
		}
	}

	public IPlugin[] getPlugins() {
		return pluginsById.values().toArray(new IPlugin[0]);
	}

	public void shutDown() {
		for (IPlugin plugin : pluginsById.values()) {
			plugin.shutDown();
		}

		pluginsById.clear();

		L.info("shutdown");
	}
}
