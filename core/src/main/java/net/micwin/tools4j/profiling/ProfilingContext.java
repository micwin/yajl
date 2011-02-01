/*
 * (C) 2004 M.Winkler All Rights reserved
 * 
 * TODO copyright legal notes for final license model
 *
 * $Log: ProfilingContext.java,v $
 * Revision 1.3  2007/08/30 07:31:37  recipient00
 * reformatted to meet builtin sun java code conventions
 *
 * Revision 1.2  2007/08/30 07:18:25  recipient00
 * gone jdk 1.5 and eclipse 3.2.x
 *
 * Revision 1.1.2.1  2007/06/03 13:15:29  recipient00
 * Migrating to maven2
 *
 * Revision 1.1.2.1  2007/02/15 07:06:29  recipient00
 * Renaming ALL packages from de.micwin.* to net.micwin.tools4j.*
 *
 * Revision 1.2.2.1  2006/12/29 12:05:02  recipient00
 * introducing generics
 *
 * Revision 1.2  2004/11/24 13:57:36  recipient00
 * Some convenience methods
 *
 * Revision 1.1  2004/08/26 11:26:36  recipient00
 * A powerful profiler and a fitting framework-class
 *
 *  
 */
package net.micwin.tools4j.profiling;

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import net.micwin.tools4j.StringFactory;
import net.micwin.tools4j.config.ConfigException;
import net.micwin.tools4j.config.IConfiguration;

/**
 * This class covers a single profiling context from which runtime statistics
 * about code snippets can be taken. It also provides a static context for
 * initialization and context lookup. This is granted to work even if its not
 * initialized. In that case, all profiling in all contextx is disabled until
 * explicisely enabled.
 * 
 * @author micwin@gmx.org
 * @since 26.08.2004
 */
public final class ProfilingContext {

    // static
    // ---------------------------------------------------------------------

    public static final String KEY_STATE_LENIENT = "stateLenient";

    public static final String KEY_ENABLED = "enabled";

    public static final String KEY_STATES = "states";

    static ProfilingContext _root = new ProfilingContext(false, true, "/");

    static boolean _initiallyEnabled = false;

    static char _pathSeparator = '.';

    static boolean _stateLenient = false;

    /**
         * (Re)configures profiling. Takes some keys out of the configuration
         * instance and propagates them around the static COntext tree.
         * 
         * @param config
         * @throws ConfigException
         */
    public static void configure(IConfiguration config) throws ConfigException {
	_initiallyEnabled = Boolean.valueOf(
		config.getString(KEY_ENABLED, "false")).booleanValue();

	_root.setEnabled(_initiallyEnabled, true);
	_pathSeparator = config.getString("pathSeparator", ".").charAt(0);

	_stateLenient = Boolean.valueOf(
		config.getString(KEY_STATE_LENIENT, "true")).booleanValue();

	String[] states = config.getArray(KEY_STATES, new String[0]);

	for (int i = 0; i < states.length; i++) {
	    String stateLine = states[i];
	    StringTokenizer st = new StringTokenizer(stateLine, ":");
	    String pathString = st.nextToken().trim();
	    boolean enabled = Boolean.valueOf(st.nextToken().trim())
		    .booleanValue();

	    // Extract propagation flag
	    // if specified after a colon, the take this
	    boolean propagate = st.hasMoreTokens() ? Boolean.valueOf(
		    st.nextToken().trim()).booleanValue() : false;

	    // if path ends with an asterisk, then force overwrite flag
	    if (pathString.endsWith("" + _pathSeparator + '*')) {
		pathString = pathString.substring(0, pathString.length() - 2);
		propagate = true;
	    }

	    String[] path = StringFactory.tokenizeToArray(pathString, String
		    .valueOf(_pathSeparator), null, null);
	    ProfilingContext ctx = getContext(path);
	    ctx.setEnabled(enabled, propagate);
	}
    }

    /**
         * Returns wether or not profiling is globally enabled.
         * 
         * @return
         */
    public static boolean isProfilingEnabled() {
	return _initiallyEnabled;
    }

    /**
         * Globally enables or disables profiling. Also serves as default value
         * for newly created ProfilingContexts
         * 
         * @param b
         */
    static void setProfilingEnabled(boolean newState) {
	_initiallyEnabled = newState;
	_root.setEnabled(newState, false);
    }

    /**
         * Checks wether or not the classes throw an exception when states are
         * weird.
         * 
         * @return
         */
    public static boolean isStateLenient() {
	return _stateLenient;
    }

    /**
         * Retrieves a ProfilingContext specified by the passed path
         * information.
         * 
         * @param path
         *                A hierarchical path consisting of (sub) context names.
         *                Must contain at least one element.
         * @return The appropriate ProfilingContext instance. Will be created if
         *         missing.
         */
    public static ProfilingContext getContext(String[] path) {
	if (path == null) {
	    throw new IllegalArgumentException("argument 'path' is null");
	}

	else if (path.length == 0) {
	    throw new IllegalArgumentException("argument 'path' is empty");
	}

	ProfilingContext ctx = _root;
	for (int index = 0; index < path.length; index++) {
	    ctx = ctx.getSub(path[index]);
	}

	return ctx;
    }

    /**
         * Retrieves a ProfilingContext by analyzing the class name and
         * tokenizing its path and name information.
         * 
         * @param context
         *                A class that serves as context.
         * @return The appropriate ProfilingContext instance. Will be created if
         *         missing.
         */
    public static ProfilingContext getContext(Class context) {
	StringTokenizer tokenizer = new StringTokenizer(context.getName(), ".");

	String[] path = new String[tokenizer.countTokens()];
	for (int i = 0; i < path.length; i++) {
	    path[i] = tokenizer.nextToken();
	}
	return getContext(path);
    }

    /**
         * Retrieves a ProfilingContext by analyzing the class name and
         * tokenizing its path and name information, and an additional sub
         * context name (such as a method name, or another code snippet
         * information).
         * 
         * @param context
         *                A class that serves as context.
         * @param subContext
         *                A identification of another code snippet.
         * @return The appropriate ProfilingContext instance. Will be created if
         *         missing.
         */
    public static ProfilingContext getContext(Class context, String subContext) {
	ProfilingContext ctx = getContext(context);
	if (subContext != null) {
	    ctx = ctx.getSub(subContext);
	}
	return ctx;
    }

    /**
         * Retrieves a ProfilingContext by analyzing the path information of the
         * passed package instance.
         * 
         * @param context
         *                A Package-Instance to serve as source for
         *                hierarchy-information.
         * @return
         */
    public static ProfilingContext getContext(Package context) {
	StringTokenizer tokenizer = new StringTokenizer(context.getName(), ".");

	String[] path = new String[tokenizer.countTokens()];
	int index = -1;
	while (tokenizer.hasMoreElements()) {
	    path[++index] = tokenizer.nextToken();
	}
	return getContext(path);
    }

    public static Collection getAvailableContexts(
	    Collection<ProfilingContext> dest) {
	return _root.collectSubs(dest, true);
    }

    // instance
    // ---------------------------------------------------------------------

    boolean _enabled, _threadSafe;

    Map<String, ProfilingContext> _sub = null;

    long _totalTime = 0;

    long _totalCalls = 0;

    Map<Thread, Long> _starts = null;

    String _pathString;

    /**
         * Creates a new Root context.
         */
    ProfilingContext(boolean enabled, boolean threadSafe, String pathString) {
	super();
	_enabled = enabled;
	_threadSafe = threadSafe;

	if (threadSafe) {
	    _starts = new Hashtable<Thread, Long>();
	    _sub = new Hashtable<String, ProfilingContext>();
	} else {
	    _starts = new HashMap<Thread, Long>();
	    _sub = new HashMap<String, ProfilingContext>();
	}

	_pathString = pathString;
    }

    public String getPathString() {
	return _pathString;
    }

    /**
         * Gets a sub context. If not available, create one.
         * 
         * @param name
         *                The name of the sub in relation to this context.
         * @return
         */
    public ProfilingContext getSub(String name) {
	ProfilingContext sub = (ProfilingContext) _sub.get(name);
	if (sub == null) {
	    String newPath = _pathString + _pathSeparator + name;
	    sub = new ProfilingContext(_enabled, _threadSafe, newPath);
	    _sub.put(name, sub);
	}
	return sub;
    }

    /**
         * Checks wether or not this ProfilingCOntext is enabled. Does NOT tell
         * wether all subcontextx are enabled!
         * 
         * @return
         */
    public boolean isEnabled() {
	return _enabled && isProfilingEnabled();
    }

    /**
         * Sets a new state for this context. If propagate==true, then this
         * state is propagated to all sub contexts.
         * 
         * @param enabled
         * @param propagate
         */
    public void setEnabled(boolean enabled, boolean propagate) {
	_enabled = enabled;
	if (propagate) {
	    for (Iterator iter = _sub.values().iterator(); iter.hasNext();) {
		ProfilingContext ctx = (ProfilingContext) iter.next();
		ctx.setEnabled(enabled, propagate);
	    }
	}
    }

    /**
         * Sets a new state for this and all sub contexts.
         * 
         * @param enabled
         * @param propagate
         */
    public void setEnabled(boolean enabled) {
	setEnabled(enabled, true);
    }

    public void start() {
	if (isEnabled()) {

	    Long actualStart = (Long) _starts.get(Thread.currentThread());
	    if (actualStart != null && !_stateLenient) {
		throw new IllegalStateException("profiling already running");
	    }

	    actualStart = new Long(System.currentTimeMillis());
	    _starts.put(Thread.currentThread(), actualStart);
	}
    }

    /**
         * Stop profiling. If you want to profile a number of operations at once
         * (ie loops), you can pass in the number of operations/loops done to
         * get an average.
         * 
         * @param count
         *                Number of operations done.
         */
    public void stop(long count) {

	if (count < 1) {
	    throw new IllegalArgumentException("argument 'count' is < 1");
	}

	if (isEnabled()) {

	    Long actualStart = (Long) _starts.remove(Thread.currentThread());
	    if (actualStart == null) {
		if (!_stateLenient) {
		    throw new IllegalStateException("profiling not yet running");
		} else {
		    return;
		}
	    }

	    long timeSpent = System.currentTimeMillis()
		    - actualStart.longValue();
	    _totalTime = _totalTime + timeSpent;
	    _totalCalls = _totalCalls + count;
	}
    }

    /**
         * Stops a profiling run for this profilingContext and the actual
         * Thread. Must have started before.
         */
    public void stop() {
	stop(1);
    }

    /**
         * Gets the total time stopped so far (the sum of all times passed
         * between the subsequent calls of start() and stop() for context and
         * all threads).
         * 
         * @return
         */
    public long getTotalTime() {
	return _totalTime;
    }

    /**
         * The number of times stop() has been called.
         * 
         * @return
         */
    public long getTotalCalls() {
	return _totalCalls;
    }

    /**
         * The total time divided by the number of calls.
         * 
         * @return
         */
    public double getAverageTime() {
	if (isEnabled() && _totalCalls > 0) {
	    return 1.0 * _totalTime / _totalCalls;
	} else
	    return 0;
    }

    /**
         * Collects all sub contexts into one Collection.
         * 
         * @param dest
         *                The collection to put the subs into.
         * @param recurse
         *                wether or not to recurse into the sub contexts.
         * @return dest
         */
    Collection collectSubs(Collection<ProfilingContext> dest, boolean recurse) {
	for (Iterator iter = _sub.values().iterator(); iter.hasNext();) {
	    ProfilingContext ctx = (ProfilingContext) iter.next();

	    dest.add(ctx);
	    if (recurse) {
		ctx.collectSubs(dest, true);
	    }

	}
	return dest;
    }

}
