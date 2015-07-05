package org.hartorn.htf.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.hartorn.htf.exception.ImplementationException;

/**
 * Utility class containing all methods for manipulating files.
 *
 * @author Hartorn
 *
 */
public enum FileUtil {
    ;
    private static final Tika CONTENT_DETECTOR = new Tika();

    private FileUtil() {
        // private constructor for helper class.
    }

    /**
     * Returns the mime type, if detected.
     *
     * @param file
     *            the file we want the mime type.
     * @return a string, containing the mime-type
     * @throws ImplementationException
     *             Technical Exception, wrapping IoException
     */
    public static String getMimeType(final File file) throws ImplementationException {
        try {
            return FileUtil.CONTENT_DETECTOR.detect(file);
        } catch (final IOException e) {
            throw new ImplementationException(e);
        }
    }

    /**
     * Returns the mime type, if detected.
     *
     * @param inputStream
     *            the file we want the mime type.
     * @return a string, containing the mime-type
     * @throws ImplementationException
     *             Technical Exception, wrapping IoException
     */
    public static String getMimeType(final InputStream inputStream) throws ImplementationException {
        try {
            return FileUtil.CONTENT_DETECTOR.detect(inputStream);
        } catch (final IOException e) {
            throw new ImplementationException(e);
        }
    }
}
