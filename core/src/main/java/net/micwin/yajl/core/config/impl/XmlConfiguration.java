/*
 * (C) 2004 M.Winkler All Rights reserved
 * 
 * 
 * $Log: XmlConfiguration.java,v $
 * Revision 1.3  2007/08/30 07:31:24  recipient00
 * reformatted to meet builtin sun java code conventions
 *
 * Revision 1.2  2007/08/30 07:18:17  recipient00
 * gone jdk 1.5 and eclipse 3.2.x
 *
 * Revision 1.1.2.1  2007/06/03 13:15:23  recipient00
 * Migrating to maven2
 *
 * Revision 1.1.2.2  2007/02/15 08:59:27  recipient00
 * Removed log4j dependencies
 *
 * Revision 1.1.2.1  2007/02/15 07:06:22  recipient00
 * Renaming ALL packages from de.micwin.* to net.micwin.tools4j.*
 *
 * Revision 1.2.2.2  2007/02/14 22:39:24  recipient00
 * now path is of type HierarchyPath
 *
 * Revision 1.2.2.1  2006/12/29 12:07:56  recipient00
 * distinction between the configuration implementation and its build process
 *
 * Revision 1.2  2004/04/18 21:47:32  recipient00
 * changed author's email to micwin@gmx.org
 *
 * Revision 1.1  2004/04/17 16:03:58  recipient00
 * Initial check in
 *
 *  
 */

package net.micwin.yajl.core.config.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import net.micwin.yajl.core.config.IConfiguration;
import net.micwin.yajl.core.config.MissingConfigKeyException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class provides an easy way to handle configurations that are put down as
 * xml data. You can either pass in XML strings (to directly creane a
 * configuration, or for test purposes), or reference an url to do the job.
 * 
 * @author micwin@gmx.org
 * @since 16.04.2004 14:32:05
 */
public final class XmlConfiguration implements IConfiguration {

    static final Logger L = Logger.getLogger(XmlConfiguration.class.getName());

    String _path = null;

    /**
         * A Hashmap holding the keys and values as Strings.
         */
    HashMap<String, String> _keys;

    /**
         * A hashmap holding array typed keys.
         */
    private HashMap<String, Object> _arrays = null;

    /**
         * A Hashmap holding the sub groups.
         */
    HashMap<Object, XmlConfiguration> _subs;

    /**
         * The value of the name attribute of the building node.
         */
    Object _name;

    /**
         * Disabled constructor. Dont use this.
         * 
         */
    private XmlConfiguration() {
	super();
    }

    /**
         * Creates a new XmlConfiguration by analyzing a xml snippet.
         * 
         * @param xml
         *                XMl snippet to use.
         * @throws ParserConfigurationException
         * 
         * @throws FactoryConfigurationError
         *                 Something is wrong with the xml parser configuration.
         * @throws SAXException
         *                 xml-Snippet is not wellformed.
         */
    public XmlConfiguration(String xml) throws ParserConfigurationException,
	    FactoryConfigurationError, SAXException {
	DocumentBuilder db = DocumentBuilderFactory.newInstance()
		.newDocumentBuilder();
	try {
	    Document doc = db.parse(new ByteArrayInputStream(xml.getBytes()));
	    Node docRoot = doc.getDocumentElement();
	    init("/", docRoot);
	    _path = "//" + _name;
	} catch (IOException e) {
	    // since we're reading from a String, this shouldnt happen
	    L.log(Level.SEVERE, "IOException while analyzing string ?!?!", e);
	    throw new IllegalStateException(
		    "See log4j-log for further details : " + e);
	}
    }

    /**
         * loads a configuration from a remote source identified by an url.
         * 
         * @param url
         * @throws ParserConfigurationException
         * 
         * @throws FactoryConfigurationError
         *                 Something is wrong with the xml parser configuration.
         * @throws SAXException
         *                 xml-Snippet is not wellformed.
         * @throws IOException
         *                 If something is wrong with the remote source.
         */
    public XmlConfiguration(URL url) throws ParserConfigurationException,
	    FactoryConfigurationError, SAXException, IOException {
	DocumentBuilder db = DocumentBuilderFactory.newInstance()
		.newDocumentBuilder();
	Document doc = db.parse(url.openStream());
	Node docRoot = doc.getDocumentElement();
	init("/", docRoot);
    }

    /**
         * Loads a root configuration directly from a node.
         * 
         * @param node
         */
    public XmlConfiguration(Node node) {
	this("/", node);
    }

    /**
         * Composes a sub node.
         * 
         * @param parentPath
         *                the parent path
         * @param node
         *                the node data to use.
         */
    protected XmlConfiguration(String parentPath, Node node) {
	init(parentPath, node);
    }

    private void init(String parentPath, Node configNode) {
	Node nodeNameNode = configNode.getAttributes().getNamedItem("name");
	if (nodeNameNode != null) {
	    _name = nodeNameNode.getNodeValue();
	} else {
	    _name = configNode.getLocalName();
	}
	if (L.isLoggable(Level.FINEST)) {
	    L.finest("Initializing node " + _name + " ...");
	}
	_path = parentPath + "/" + _name;
	NodeList nl = configNode.getChildNodes();
	_keys = new HashMap<String, String>();
	_subs = new HashMap<Object, XmlConfiguration>();
	_arrays = new HashMap<String, Object>();
	for (int i = 0; i < nl.getLength(); i++) {
	    Node activeNode = nl.item(i);
	    String tagName = activeNode.getNodeName();
	    if (tagName.equals("sub")) {
		XmlConfiguration sub = new XmlConfiguration(_path, activeNode);
		_subs.put(sub.getName(), sub);
	    } else if ("array".equals(tagName)) {
		// array node having children
		String arrayName = activeNode.getAttributes().getNamedItem(
			"name").getNodeValue();
		// collect all child elements
		LinkedList<String> ll = new LinkedList<String>();
		NodeList children = activeNode.getChildNodes();
		for (int j = 0; j < children.getLength(); j++) {
		    Node child = children.item(j);
		    String childName = child.getNodeName();
		    // exclude system nodes HERE
		    if (!childName.startsWith("#")) {
			ll.add(child.getFirstChild().getNodeValue().toString());
		    }
		}
		_arrays.put(arrayName, ll.toArray(new String[ll.size()]));
	    } else if (!tagName.startsWith("#")) {
		// normal node having a single #text- or #cdata-element
		String nodeValue = activeNode.getFirstChild().getNodeValue();
		_keys.put(tagName, nodeValue);
	    }
	}
    }

    /**
         * Returns the attribute "name" of the xml node that built this config.
         * If this is root, then name returns "root".
         * 
         * @return
         */
    public Object getName() {
	return _name;
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.micwin.yajl.core.config.IConfiguration#getBoolean(java.lang.String)
         */
    public Boolean getBoolean(String key) throws MissingConfigKeyException {
	String boolString = (String) _keys.get(key);
	if (boolString == null) {
	    throw new MissingConfigKeyException(_path, key);
	}
	return Boolean.valueOf(boolString.trim());
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.micwin.yajl.core.config.IConfiguration#getBoolean(java.lang.String,
         *      java.lang.Boolean)
         */
    public Boolean getBoolean(String key, Boolean defaultBool) {
	try {
	    return getBoolean(key);
	} catch (MissingConfigKeyException e) {
	    return defaultBool;
	}
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.micwin.yajl.core.config.IConfiguration#getInteger(java.lang.String)
         */
    public Integer getInteger(String key) throws MissingConfigKeyException {
	String value = (String) _keys.get(key);
	if (value == null) {
	    throw new MissingConfigKeyException(_path, key);
	}
	return Integer.valueOf(value);
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.micwin.yajl.core.config.IConfiguration#getInteger(java.lang.String,
         *      java.lang.Integer)
         */
    public Integer getInteger(String key, Integer defaultInt) {
	try {
	    return getInteger(key);
	} catch (MissingConfigKeyException e) {
	    return defaultInt;
	}
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.micwin.yajl.core.config.IConfiguration#getString(java.lang.String)
         */
    public String getString(String key) throws MissingConfigKeyException {
	String value = (String) _keys.get(key);
	if (value == null) {
	    throw new MissingConfigKeyException(_path, key);
	}
	return value;
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.micwin.yajl.core.config.IConfiguration#getString(java.lang.String,
         *      java.lang.String)
         */
    public String getString(String key, String defaultString) {
	try {
	    return getString(key);
	} catch (MissingConfigKeyException e) {
	    return defaultString;
	}
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.micwin.yajl.core.config.IConfiguration#getArray(java.lang.String)
         */
    public String[] getArray(String key) throws MissingConfigKeyException {
	String[] array = (String[]) _arrays.get(key);
	if (array == null) {
	    throw new MissingConfigKeyException(_path, key);
	}
	return array;
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.micwin.yajl.core.config.IConfiguration#getArray(java.lang.String,
         *      java.lang.String[])
         */
    public String[] getArray(String key, String[] defaultArray) {

	try {
	    return getArray(key);
	} catch (MissingConfigKeyException e) {
	    return defaultArray;
	}
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.micwin.yajl.core.config.IConfiguration#assertAvailable(java.lang.String[])
         */
    public void assertAvailable(String[] keys) throws MissingConfigKeyException {
	if ((_keys == null) || (_keys.size() == 0)) {
	    throw new MissingConfigKeyException(_path, keys);
	}
	LinkedList<String> missingKeys = new LinkedList<String>();
	for (int i = 0; i < keys.length; i++) {
	    if (!_keys.containsKey(keys[i]) && !_arrays.containsKey(keys[i])
		    && !_subs.containsKey(keys[i])) {
		missingKeys.add(keys[i]);
	    }
	}
	if (missingKeys.size() > 0) {
	    throw new MissingConfigKeyException(_path, (String[]) missingKeys
		    .toArray(new String[missingKeys.size()]));
	}
	missingKeys.clear();
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.micwin.yajl.core.config.IConfiguration#getPath()
         */
    public String getPathString() {
	return _path;
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.micwin.yajl.core.config.IConfiguration#sub(java.lang.String)
         */
    public IConfiguration sub(String key) throws MissingConfigKeyException {
	XmlConfiguration value = (XmlConfiguration) _subs.get(key);
	if (value == null) {
	    throw new MissingConfigKeyException(_path, key);
	}
	return value;
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.micwin.yajl.core.config.IConfiguration#keys()
         */
    public String[] keys() {
	Set<String> keySet = _keys.keySet();
	return (String[]) keySet.toArray(new String[keySet.size()]);
    }
}