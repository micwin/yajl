package org.gnomiki.plugins;

/**
 * A client plugin.
 * 
 * @author MicWin
 * 
 */
public interface IPlugin {

	/**
	 * Returns a unique id to which other plugins can connect to. Consider this
	 * as a unique id for s specific group of domain functionality.
	 * 
	 * @return
	 */
	public String getPluginId();

	/**
	 * Commands the plugin to initialize.
	 * 
	 * @param pluginManager
	 *            the plugin manager this plugin is bound to.
	 * @throws Exception
	 *             If initialization fails *and* this plugin is not eecutable.
	 *             It hence will not be registered in the repository and will
	 *             not be available.
	 */
	public void init(IPluginManager pluginManager) throws Exception;

	/**
	 * Shuts thsi plugin down. No matter which result this has, this plugin
	 * plugin is not available any more.
	 */
	public void shutDown();
}
