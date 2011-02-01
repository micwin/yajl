/**
 * 
 */
package net.micwin.tools4j.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * This StreamFactory is a convenient way of aquiring streams. It understands
 * the special extended urls used all over tools4j, and should be used for any
 * streams to be obtained.<br />
 * 
 * @author michael.winkler@micwin.net
 * 
 */
public class StreamFactory {

    /**
         * This is the prefix to identify local files instead of urls when
         * opening a stream via
         * <code>protected InputStream openStream(String) throws IOException</code>.
         * 
         * @see InputStream openStream(String) throws IOException
         */
    public static final String LOCAL_MARKER = "local:";

    /**
         * This is the prefix to identify normal files instead of remote data
         * sources when opening a stream via
         * <code>protected InputStream openStream(String) throws IOException</code>.
         * DOES NOT ACCEPT EXTENDED SYNTAX!
         * 
         * @see InputStream openStream(String) throws IOException
         */
    public static final String FILE_MARKER = "file:";

    /**
         * This is the prefix to identify a file allocated by
         * <code>ClassLoader.getResource ()</code>.
         * 
         * @see InputStream openStream(String) throws IOException
         */
    public static final String RESOURCE_MARKER = "resource:";

    /**
         * Opens an input stream to the specified extended url.
         * 
         * @param eUrl
         *                An extended url.
         * @return
         */
    public static InputStream inputStream(String eUrl) throws IOException {

	if (eUrl.startsWith(LOCAL_MARKER)) {

	    // a local file reference
	    String filePath = eUrl.substring(LOCAL_MARKER.length());
	    return new FileInputStream(filePath);
	} else if (eUrl.startsWith(FILE_MARKER)) {
	    // a url file reference
	    String filePath = eUrl.substring(FILE_MARKER.length());
	    filePath = new File(filePath).getAbsolutePath();
	    return new FileInputStream(filePath);
	} else if (eUrl.startsWith(RESOURCE_MARKER)) {
	    // a resource file reference
	    String resourcePath = eUrl.substring(RESOURCE_MARKER.length());
	    return StreamFactory.class.getResourceAsStream(resourcePath);
	} else {

	    // faciliate class loader
	    InputStream in = null;
	    try {
		URL url = new URL(eUrl);
		in = url.openStream();
	    } catch (IOException exc) {
		throw exc;
	    }
	    if (in == null) {
		String msg = "Cannot resolve url '" + eUrl + "'";
		throw new IOException(msg);
	    } else
		return in;

	}
    }

    /**
         * Opens an output stream to the specified extended url. This only can
         * be done when having a local file.
         * 
         * @param eUrl
         *                An extended url to a file (needs local: or file:
         *                syntax)
         * @return
         */
    public static OutputStream outputStream(String eUrl,
	    boolean createPathIfMissing) throws IOException {

	if (eUrl == null) {
	    throw new NullPointerException("argument 'eUrl' is null");
	}

	if (eUrl.startsWith(LOCAL_MARKER) || eUrl.startsWith(FILE_MARKER)) {

	    // a local file reference
	    String filePath = eUrl.substring(eUrl.indexOf(':') + 1);
	    if (createPathIfMissing) {
		new File(filePath).getParentFile().mkdirs();
	    }
	    return new FileOutputStream(filePath);
	} else {
	    throw new IllegalArgumentException(
		    "this method cannot open an output to normal URLs (eUrl was '"
			    + eUrl + "'");
	}
    }

    /**
         * Moves all data available from stream <code>from</code> to stream
         * <code>to</code>.
         * 
         * @param from
         * @param to
         * @return
         * @throws IOException
         */
    public static int transfer(InputStream from, OutputStream to)
	    throws IOException {

	int moved = 0;
	byte[] buffer = new byte[2048000];
	while (from.available() > 0) {
	    int amount = from.read(buffer);
	    to.write(buffer, 0, amount);
	    moved += amount;
	}
	return moved;

    }

    /**
         * Checks wether or not the specified extended url is readable.
         * 
         * @param eUrl
         *                An eUrl denoting a source of data to check.
         * @return
         */
    public static boolean isReadable(String eUrl) {
	try {
	    InputStream in = inputStream(eUrl);
	    if (in == null) {
		return false;
	    }
	    in.close();
	    return true;
	} catch (IOException e) {
	    return false;
	}
    }
}
