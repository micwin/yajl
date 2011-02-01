/*
 * (c) 2006 Michael Winkler, Munich, Germany 
 * All rights reserved
 */

package net.micwin.tools4j.data.structures;

import net.micwin.tools4j.ArrayFactory;
import net.micwin.tools4j.StringFactory;

/**
 * This class encapsulates the logic of parsing, generating and handling all
 * sorts of hierarchical paths.
 * 
 * @author micwin
 * 
 */
public class HierarchyPath {

    String _rootSymbol;

    char _pathSeparator;

    String[] _pathNames;

    private String _pathString = null;

    /**
         * Creates a {@link HierarchyPath} out of a parent and a name. All other
         * parameters are taken from the parent.
         * 
         * @param parent
         * @param name
         */
    public HierarchyPath(HierarchyPath parent, String name)
	    throws IllegalArgumentException {
	if (parent == null) {
	    throw new IllegalArgumentException("argument 'parent' is null");
	} else if (name == null) {
	    throw new IllegalArgumentException("argument 'name' is null");
	}

	_rootSymbol = parent._rootSymbol;
	_pathSeparator = parent._pathSeparator;
	_pathNames = (String[]) ArrayFactory.append(parent._pathNames, name);
    }

    /**
         * Creates a new Hierarchy path by completely defining the structure
         * manually.
         * 
         * @param rootSymbol
         *                A symbol that must preceed a path.
         * @param pathSeparator
         *                A separator between the path names.
         * @param pathNames
         *                the path names denoting the specific levels of the
         *                hierarchy (not including root symbol or path
         *                separators!)
         */
    public HierarchyPath(String rootSymbol, char pathSeparator,
	    String[] pathNames) {

	if (rootSymbol == null) {
	    throw new IllegalArgumentException("argument 'rootSymbol' is null");
	}
	if (pathNames == null) {
	    throw new IllegalArgumentException("argument 'pathNames' is null");
	}

	_rootSymbol = rootSymbol;
	_pathSeparator = pathSeparator;
	_pathNames = (String[]) ArrayFactory.copy(pathNames);
    }

    public HierarchyPath(String rootSymbol, char pathSeparator,
	    String pathString) {

	if (rootSymbol == null) {
	    throw new IllegalArgumentException("argument 'rootSymbol' is null");
	}

	if (pathString == null) {
	    throw new IllegalArgumentException("argument 'pathNames' is null");
	}

	if (!"".equals(rootSymbol) && !pathString.startsWith(rootSymbol)) {
	    throw new IllegalArgumentException("argument pathString ("
		    + pathString + ") does not start with rootSymbol '"
		    + rootSymbol + "'");
	}

	_rootSymbol = rootSymbol;
	_pathSeparator = pathSeparator;

	String purePath = pathString.substring(rootSymbol.length());

	_pathNames = StringFactory.tokenizeToArray(purePath,
		"" + pathSeparator, null, null);
    }

    public String getPathString() {
	if (_pathString == null) {
	    _pathString = StringFactory.toString(_rootSymbol, _pathNames, ""
		    + _pathSeparator, "");
	}
	return _pathString;
    }

    /**
         * Creates a child object from this one.
         * 
         * @param name
         * @return
         */
    public HierarchyPath createChild(String name) {
	return new HierarchyPath(this, name);
    }
}
