/**
 * (c) 2006 Michael Winkler, Munich, Germany
 */
package net.micwin.yajl.core.config;

import java.util.StringTokenizer;

/**
 * A version number. has the ability to perform checks on the version number
 * stored.
 * <p />
 * 
 * A Version number consists of at least three parts :
 * <ul>
 * <li><b>version</b>: the most left number. Reflects "big" changes like
 * architectural changes, vast functional changes and such.</li>
 * <li><b>increment</b>: the second left number. Reflects interface changes
 * and small functional increments.</li>
 * <li><b>build</b>: the third left number. Reflects bug fixes and uncritical
 * changes. Also reflects compiler runs et al that are reflected in binary
 * changes of the distribution.</li>
 * </ul>
 * <p />
 * 
 * <strong>Cookbook</strong>
 * <p />
 * <ul>
 * <li>You should change <code>build</code> if you change some source code,
 * for example to remove a bug or to implement a stub.</li>
 * <li>You should change <code>increment</code> if you change some methods
 * behavior or signature that is relevant to clients, move/rename classes and
 * such</li>
 * <li>You should change <code>version</code> if you change technology,
 * platform and such</li>
 * </ul>
 * <p />
 * 
 * <strong>About comparison</strong>
 * <p />
 * 
 * Consider
 * 
 * <pre>
 * Version v = new Version(v1, v2, v3);
 * 
 * Version r = new Version(r1, r2, r3);
 * </pre>.
 * 
 * Then it is always true that
 * 
 * <pre>
 *                                                 v.compareTo(r) == -1 -&gt; (v1 &lt; r1) || (v2 &lt; r2) || (v3 &lt; r3) ;
 *                                                 v.compareTo(r) == 1 -&gt; (v1 &gt; r1) || (v2 &gt; r2) || (v3 &gt; r3) ;
 * </pre>
 * 
 * It is especially true that
 * 
 * <pre>
 *                                                 v.compareTo(r) == 0 &lt;-&gt; (v1 == r1) &amp;&amp; (v2 == r2) &amp;&amp; (v3 == r3) ;
 * </pre>
 * 
 * Please note that the first arrows are unidirectional, while the last arrow is
 * bidirectional.
 * <p />
 * 
 * @author micwin@micwin.net
 * 
 */
public final class Version implements Comparable {

    final int _version;

    final int _increment;

    final int _build;

    private final boolean _snapshot;

    private String _toString = null;

    public Version(int version, int increment, int build) {
	this(version, increment, build, false);
    }

    public Version(int version, int increment, int build, boolean snapshot) {
	_version = version;
	_increment = increment;
	_build = build;
	this._snapshot = snapshot;

    }

    /**
         * Compares two Versions according to the declared method's function.
         * 
         * @throws ClassCastException
         *                 If o is not of type <code>Version</code>.
         * 
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
    public int compareTo(Object o) {
	// cast to correct type.
	Version v2 = (Version) o;

	return compareTo(v2);
    }

    /**
         * Compares v2 to this version. If <code>this &lt; v2</code> , then
         * return -1; if <code>this &gt;  v2</code>, then return 1; else
         * return 0.
         * 
         * @param v2
         *                A version to compare with.
         * @return
         */
    private int compareTo(Version v2) {
	// check version number
	if (_version < v2._version)
	    return -1;
	else if (_version > v2._version)
	    return 1;

	// check increment number
	if (_increment < v2._increment)
	    return -1;
	else if (_increment > v2._increment)
	    return 1;

	// check build number
	if (_build < v2._build)
	    return -1;
	else if (_build > v2._build)
	    return 1;

	// check snapshot flag number
	if (_snapshot && !v2._snapshot)
	    return -1;
	else if (!_snapshot && v2._snapshot)
	    return 1;

	// everything equal
	return 0;
    }

    /**
         * Creates a String that looks like the commonly known version numbers
         * <i>1.1.5</i> or <i>7.2.6</i>, according to the pattern "<i>{version}.{increment}.{build}</i>".
         * 
         * @see java.lang.Object#toString()
         */
    public String toString() {
	if (_toString == null) {
	    _toString = "" + _version + '.' + _increment + '.' + _build
		    + (_snapshot ? "-SNAPSHOT" : "");
	}

	return _toString;
    }

    /**
         * Converts this version number into an array consisting out of its
         * numbers, aligned in an array that looks like
         * <code>[version, increment, build]</code>.
         * 
         * @param array
         *                n array to put the numbers to. If <code>null</code>
         *                or too short, a nerw array is created.
         * @return The array of numbers.
         */
    public int[] toArray(int[] array) {
	if (array == null || array.length < 3) {
	    array = new int[] { _version, _increment, _build };
	} else {
	    array[0] = _version;
	    array[1] = _increment;
	    array[2] = _build;
	}

	return array;
    }

    /**
         * Checks wether or not <code>this</code> version is below
         * <code>v2</code>.
         * 
         * @param v2
         * @return
         */
    public boolean isBelow(Version v2) {
	return compareTo(v2) < 0;
    }

    /**
         * Checks wether or not <code>this</code> version is above
         * <code>v2</code>.
         * 
         * @param v2
         * @return
         */
    public boolean isAbove(Version v2) {
	return compareTo(v2) > 0;
    }

    /**
         * interprets a string as a version number. ALl numbers must be
         * separated by dots, like <code>1.4.3</code> or <code>2.4.0</code>.
         * 
         * @param line
         * @return
         * @throws NumberFormatException
         */
    public static Version valueOf(String line) throws NumberFormatException {
	StringTokenizer tokenizer = new StringTokenizer(line, ".");
	int version = Integer.valueOf(tokenizer.nextToken().trim()).intValue();
	int increment = tokenizer.hasMoreElements() ? Integer.valueOf(
		tokenizer.nextToken().trim()).intValue() : 0;
	int build = tokenizer.hasMoreElements() ? Integer.valueOf(
		tokenizer.nextToken().trim()).intValue() : 0;
	return new Version(version, increment, build);
    }

    /**
         * Returns the version (first) part of the version number.
         * 
         * @return
         */
    public int getVersion() {
	return _version;
    }

    /**
         * Returns the increment (middle) part of the version number. This must
         * change if a code breaks interface.
         * 
         * @return
         */
    public int getIncrement() {
	return _increment;
    }

    /**
         * Returns the build (last) part of the version number.
         * 
         * @return
         */
    public int getBuild() {
	return _build;
    }

    @Override
    public boolean equals(Object obj) {

	// shortcut : null
	if (obj == null) {
	    return false;
	}

	// shortcut : this
	if (obj == this) {
	    return true;
	}

	// the long way
	try {
	    return compareTo((Version) obj) == 0;
	} catch (ClassCastException cce) {
	    // different type
	    return false;
	}
    }

    public boolean isSnapShot() {
	return _snapshot;
    }
}
