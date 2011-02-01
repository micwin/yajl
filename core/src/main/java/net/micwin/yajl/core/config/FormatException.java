/*
 * (C) 2004 M.Winkler All Rights reserved
 * 
 * 
 * $Log: FormatException.java,v $
 * Revision 1.5  2007/12/11 22:57:10  recipient00
 * removed typo
 *
 * Revision 1.4  2007/09/06 15:57:58  recipient00
 * reformatted code, removed some code quality flaws
 *
 * Revision 1.3  2007/08/30 07:31:25  recipient00
 * reformatted to meet builtin sun java code conventions
 *
 * Revision 1.2  2007/08/30 07:18:18  recipient00
 * gone jdk 1.5 and eclipse 3.2.x
 *
 * Revision 1.1.2.1  2007/06/03 13:15:23  recipient00
 * Migrating to maven2
 *
 * Revision 1.1.2.1  2007/02/15 07:06:27  recipient00
 * Renaming ALL packages from de.micwin.* to net.micwin.tools4j.*
 *
 * Revision 1.2.2.1  2006/12/29 12:04:06  recipient00
 * Added serialVersionUIDs, removed "disabled constructors"
 *
 * Revision 1.2  2004/04/18 21:44:43  recipient00
 * changed author's email to micwin@gmx.org
 *
 * Revision 1.1  2004/04/18 16:28:44  recipient00
 * Implemented FormatException and adjusted Interfaces and abstract
 *
 * Revision 1.1  2004/04/17 16:03:58  recipient00
 * Initial check in
 *  
 */
package net.micwin.yajl.core.config;

/**
 * This exception is thrown when a config key is present, but cannot meet a
 * requested type. For instance, if you set a config key
 * <code>&gt;message&lt;hello, world!&lt;message&gt;</code> and request
 * 
 * <pre>
 *         IConfiguration config = new XmlConfiguration (new URL (&quot;http://myserver.org/config.xml&quot;)) ;
 *         config.getInteger (&quot;message&quot;) ;
 *    &lt;&amp;pre&gt;,&lt; br/&gt;
 *    then you will get a FormatException.
 *    
 *    @author micwin@gmx.org
 * 
 */
public class FormatException extends RuntimeException {

    /**
         * 
         */
    private static final long serialVersionUID = -7673289521646107271L;

    protected String _path;

    protected String _key;

    protected Class _requestedType;

    /**
         * Disabled constructor.
         */
    protected FormatException() {
	super();
    }

    /**
         * Disabled constructor.
         * 
         * @param message
         */
    protected FormatException(String message) {
	super(message);
    }

    /**
         * Disabled constructor.
         * 
         * @param cause
         */
    protected FormatException(Throwable cause) {
	super(cause);
    }

    /**
         * Disabled constructor.
         * 
         * @param message
         * @param cause
         */
    protected FormatException(String message, Throwable cause) {
	super(message, cause);
    }

    /**
         * A new FormatException saying that a key has the wrong format.
         * 
         * @param path
         *                The path of the configuration having the wrong key
         * @param key
         *                The name of the key having a wrong type.
         * @param requestedType
         *                The requested type.
         */
    public FormatException(String path, String key, Object value,
	    Class requestedType) {
	super("cannot parse value '" + value + "' of key '" + key
		+ "' in config '" + path + "' as " + requestedType.getName());
	_key = key;
	_path = path;
	_requestedType = requestedType;
    }

    /**
         * Returns the malicious key.
         * 
         * @return
         */
    public String getKey() {
	return _key;
    }

    /**
         * Returns the path of the configuration having this malicious key.
         * 
         * @return
         */
    public String getPath() {
	return _path;
    }

    /**
         * The type the key should be parsed to.
         * 
         * @return
         */
    public Class getRequestedType() {
	return _requestedType;
    }

}
