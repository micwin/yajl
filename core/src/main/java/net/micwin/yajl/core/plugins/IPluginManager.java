package net.micwin.yajl.core.plugins;

/**
 * A plugin mamnager to manage multiple Plugins referring to each other.
 * 
 * @author MicWin
 * 
 */
public interface IPluginManager {

	/**
	 * Initializes, that is, discovers plugins and such. If not, throw an
	 * exception. If exception thrown, plugins will not work at all.
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception;

	/**
	 * Retrieves the plugin with specified id.
	 * 
	 * @param pluginId
	 * @return Specified plugin, if installed. <code>null</code> else.
	 */
	public IPlugin getPlugin(String pluginId);

	/**
	 * returns all installed plugins.
	 * 
	 * @return
	 */
	public IPlugin[] getPlugins();

	/**
	 * Is called to shut the manager down. Also has to shutdown all plugins as
	 * well.
	 */
	public void shutDown();
}
