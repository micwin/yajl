package net.micwin.tools4j.config;

import java.io.IOException;

/**
 * Use this class to configure the singleton ConfigManager via Injection. This
 * is needen when using Spring et al.
 * 
 * @author MicWin
 * 
 */
public class ConfigManagerSingletonInjector {

    private void inject(IConfiguration config) {
	if (ConfigManager.isSingletonConfigured()) {
	    throw new IllegalStateException(
		    "cannot inject singleton config manager - already initialized");
	}

	ConfigManager._singleton = config;
    }

    public ConfigManagerSingletonInjector(String eUrl) throws IOException {
	IConfiguration config = ConfigManager.loadConfig(eUrl);
	inject(config);
    }
}
