/*
 * 
 * (C) 2003 M.Winkler All Rights reserved
 * 
 * 
 * $Log: FrameworkManager.java,v $
 * Revision 1.4  2007/09/06 15:57:59  recipient00
 * reformatted code, removed some code quality flaws
 *
 * Revision 1.3  2007/08/30 07:31:38  recipient00
 * reformatted to meet builtin sun java code conventions
 *
 * Revision 1.2  2007/08/30 07:18:26  recipient00
 * gone jdk 1.5 and eclipse 3.2.x
 *
 * Revision 1.1.2.1  2007/06/03 13:15:28  recipient00
 * Migrating to maven2
 *
 * Revision 1.1.2.1  2007/02/15 07:06:23  recipient00
 * Renaming ALL packages from de.micwin.* to net.micwin.tools4j.*
 *
 * Revision 1.9.2.1  2006/12/29 12:05:14  recipient00
 * introducing generics
 *
 * Revision 1.9  2005/07/15 11:05:25  recipient00
 * solved some problems with comment
 *
 * Revision 1.8  2004/11/24 14:00:20  recipient00
 * ongoing framework installation can now access what's there
 *
 * Revision 1.7  2004/04/27 21:13:17  recipient00
 * isSingletonInitialized()
 *
 * Revision 1.6  2004/04/18 22:29:05  recipient00
 * changed author's email to micwin@gmx.org
 *
 * Revision 1.5  2004/04/16 12:27:53  recipient00
 * once more adjusted formattings :( and extended javadocs
 *
 * 
 * Revision 1.4 2004/04/15 19:02:21 recipient00 Messed around with malformed
 * comments ...
 * 
 * 
 * Revision 1.3 2004/01/11 20:54:59 recipient00 Translated into mere english
 * 
 * Revision 1.2 2004/01/10 20:21:39 recipient00 Added todo tags for translation
 * and legal notes
 * 
 * Revision 1.1 2004/01/07 22:34:20 recipient00 initial check in
 * 
 * Revision 1.3 2003/12/30 21:13:18 micwin pseudo-Kollisionen :(
 * 
 * Revision 1.2 2003/12/16 09:43:31 micwin EInbindung von log4j
 * 
 * Revision 1.1 2003/12/16 09:26:22 micwin initial check in
 *  
 */

package net.micwin.tools4j.frameworks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.micwin.tools4j.config.ConfigException;
import net.micwin.tools4j.config.IConfiguration;
import net.micwin.tools4j.config.MissingConfigKeyException;


/**
 * Instantiates, initializes, handles and shuts down multiple frameworks by a
 * simple name. This completely decouples the internal structure and
 * dependencies of the frameworks from their client code, and therefore moves
 * configurative work normally done by client code, to this class respectively
 * the used, concrete implementation of IConfiguration and its configuration
 * file.</br>
 * 
 * The configuration of the specific frameworks is done by passing in instances
 * of IConfiguration via an init method. Each framework gets its own group named
 * by a specific framework name. This name also is used to retrieve the
 * framework later on via <code>getFramework(String)</code>, by the client
 * code.</br>
 * 
 * The concrete class to be instantiated via a default constructor is named in
 * its configuration group, namely the key <i>fm.class </i>.</br>
 * 
 * The key ( <i>fm.frameworks </i>) determines the initialization and shutdown
 * order by listing the framework names, separated by commas.</br>
 * 
 * A third (mandatory) key <i>fm.init.resume </i>, placed in the manager's base
 * IConfiguration, determines wether or not the whole init process fails if a
 * single framework's init fails.</br>
 * 
 * <i>fm.packages </i> provides a list of packages in which classes specified
 * via <code>fm.class</code> should be searched if prefixed with
 * <code>*.</code>; this provides a comfortable way to ease configuration by
 * simply omitting package names.</br>
 * 
 * @author micwin@gmx.org
 * @since 02.12.2003 22:00:26
 */
public final class FrameworkManager implements IFramework {

    // --------------------------------------------------------------
    // -- static field
    // --------------------------------------------------------------
    /**
         * Prefix for administrative keys of the framewirk manager.
         */
    public static final String KEY_PREFIX = "fm.";

    /**
         * Determines wehter or not the complete init fails if at least one
         * framework's init fails.
         */
    public static final String KEY_RESUME = KEY_PREFIX + "init.resume";

    /**
         * Determines the order of and the frameworks to init and shutdown.
         */
    public static final String KEY_FRAMEWORK_LIST = KEY_PREFIX + "frameworks";

    /**
         * A list of packages that are searched for classes named via
         * <code>fm.class</code> and prefixed by <code>*.</code>.
         */
    public static final String KEY_PACKAGES_LIST = KEY_PREFIX + "packages";

    /**
         * Specified framework classes beginning with this chars are passed to
         * the class search algorithm.
         */
    public static final String LOCATOR_PREFIX = "*.";

    /**
         * The concrete framework class. This is a per framework key.
         */
    public static final String KEY_FRAMEWORK_CLASS = KEY_PREFIX + "class";

    /**
         * Using log4j for initial loggings- of course initialized as a
         * framework.
         */
    static Log L = LogFactory.getLog(FrameworkManager.class);

    static FrameworkManager _instance = null;

    /**
         * Checks wether or not the singleton already has been initialized.
         * 
         * @return <code>true</code> if the singleton already has been
         *         initialized.
         */
    public synchronized static boolean isSingletonInitialized() {
	return _instance != null;
    }

    /**
         * To support som centralized manner of initialization and framework
         * management, this class also provides a singleton access. This method
         * initializes the singleton by passing in a IConfiguration.
         * 
         * @param config
         *                The Configuration to use.
         * @return
         * @throws ConfigException
         *                 If some keys are missing, something goes wrong with
         *                 the frameworks or what else.
         * @throws ConfigException
         *                 If the Singleton already has been initialized or a
         *                 configuration key is missing.
         * @see #singleton()
         * @see IConfiguration
         */
    public synchronized static FrameworkManager initSingleton(
	    IConfiguration config) throws ConfigException {
	if (_instance != null) {
	    throw new IllegalStateException(
		    "frameworkmanager singleton already initialized");
	}
	_instance = new FrameworkManager();
	_instance.init(config);
	L.info("Singleton initialized");
	return _instance;
    }

    /**
         * Returns the Singleton of the Framework-Manager.
         * 
         * @return The singletoon instance.
         * @throws IllegalStateException
         *                 If the singleton is not yet initialized using
         *                 initSingleton().
         * @see #initSingleton(IConfiguration)
         */
    public static synchronized FrameworkManager singleton()
	    throws IllegalStateException {
	if (_instance == null) {
	    throw new IllegalStateException(
		    "frameworkmanager singleton not yet initialized");
	}
	return _instance;
    }

    // --------------------------------------------------------------
    // -- instance field
    // --------------------------------------------------------------
    /**
         * The map of frameworks, name is the key.
         */
    Map<String, IFramework> _frameworks = new HashMap<String, IFramework>();

    /**
         * Creates an empty Framework Manager instance.
         * 
         * @see FrameworkManager#init(IConfiguration)
         * 
         */
    public FrameworkManager() {
	// Concrete init job is done in init (IConfiguration)
    }

    /**
         * Creates and initializes a new FrameworkManager instance using an
         * IConfiguration.
         * 
         * @param configuration
         *                The configuration to be used by the manager.
         * @throws ConfigException
         *                 If a mandatory config key is missing.
         */
    public FrameworkManager(IConfiguration configuration)
	    throws ConfigException {
	this();
	init(configuration);
    }

    /**
         * Initializes the FrameworkManager. Use this only if you used the
         * default constructor.
         * 
         * @param configuration
         *                The configuration to use..
         * @throws ConfigException
         *                 If a mandatory config key is missing.
         * 
         */
    public void init(IConfiguration configuration) throws ConfigException {
	// all keys present?
	try {
	    configuration.assertAvailable(new String[] { KEY_RESUME,
		    KEY_FRAMEWORK_LIST });
	} catch (MissingConfigKeyException e1) {
	    String errorMessage = "Cannot initialize frameworks";
	    L.error(errorMessage, e1);
	    throw new ConfigException(errorMessage, e1);
	}
	// Konfiguration auslesen
	boolean onInitResume = configuration.getBoolean(KEY_RESUME,
		Boolean.FALSE);
	String[] packages = configuration.getArray(KEY_PACKAGES_LIST,
		new String[0]);
	// Liste der Frameworks laden
	String[] names = configuration.getArray(KEY_FRAMEWORK_LIST);
	// durchkaspern
	for (int fIndex = 0; fIndex < names.length; fIndex++) {
	    String name = names[fIndex].trim();
	    IConfiguration fConfig = configuration.sub(name);
	    if (fConfig == null) {
		L.warn("listed framework '" + name
			+ "' skipped - no configuration available");
		continue;
	    }
	    String className = fConfig.getString(KEY_FRAMEWORK_CLASS);
	    try {
		IFramework framework = locateAndInitializeFramework(className,
			packages);
		framework.init(fConfig);
		_frameworks.put(name, framework);
		if (L.isDebugEnabled()) {
		    L.info("Framework '" + name + "' registered");
		}
	    } catch (InstantiationException e) {
		String errorMessage = "Cannot initialize framework '" + name
			+ "'";
		L.error(errorMessage, e);
		if (!onInitResume) {
		    throw new ConfigException(errorMessage, e);
		}
	    } catch (IllegalAccessException e) {
		String errorMessage = "Cannot initialize framework '" + name
			+ "'";
		L.error(errorMessage, e);
		if (!onInitResume) {
		    throw new ConfigException(errorMessage, e);
		}
	    } catch (ClassNotFoundException e) {
		String errorMessage = "Cannot initialize framework '" + name
			+ "'";
		L.error(errorMessage, e);
		if (!onInitResume) {
		    throw new ConfigException(errorMessage, e);
		}
	    } catch (ConfigException e) {
		String errorMessage = "Cannot initialize framework '" + name
			+ "'";
		L.error(errorMessage, e);
		if (!onInitResume) {
		    throw new ConfigException(errorMessage, e);
		}
	    }
	}
    }

    /**
         * Localizes and instantiates the framework class.
         * 
         * @param className
         *                to localize. May contain <code>*.</code>
         * @param searchPackages
         *                Packages to search if className starts with
         *                <code>*.</code>.
         * @return The initialized framework.
         * @throws InstantiationException
         * @throws IllegalAccessException
         * @throws ClassNotFoundException
         */
    private IFramework locateAndInitializeFramework(String className,
	    String[] searchPackages) throws InstantiationException,
	    IllegalAccessException, ClassNotFoundException {
	if (className.startsWith(LOCATOR_PREFIX)) {
	    Class clazz = null;
	    className = className.substring(LOCATOR_PREFIX.length());
	    ClassNotFoundException lastEx = null;
	    for (int i = 0; i < searchPackages.length; i++) {
		String packageName = searchPackages[i];
		if (!packageName.endsWith("."))
		    packageName += '.';
		String qName = packageName + className;
		try {
		    clazz = Class.forName(qName);
		    break;
		} catch (ClassNotFoundException cnfe) {
		    lastEx = cnfe;
		}
	    }
	    if (clazz == null) {
		throw lastEx;
	    }
	    return (IFramework) clazz.newInstance();
	} else {
	    return (IFramework) Class.forName(className).newInstance();
	}
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.micwin.tools4j.frameworks.IFramework#shutDown()
         */
    public void shutDown() {
	if (_frameworks != null) {
	    for (Iterator iter = _frameworks.keySet().iterator(); iter
		    .hasNext();) {
		IFramework fWork = (IFramework) _frameworks.get(iter.next());
		fWork.shutDown();
	    }
	}
    }

    /**
         * Gets a named framework, if present. To avoid double casting and you
         * will cast the result to its needed class, this one only returns an
         * instance of type Object. If the named framework is not available,
         * then <code>null</code> is returned.
         * 
         * @param name
         * @return The named framework, initialized and ready.
         */
    public Object getFramework(String name) {
	return _frameworks.get(name);
    }

}