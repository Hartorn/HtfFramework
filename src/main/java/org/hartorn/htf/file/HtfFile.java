package org.hartorn.htf.file;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.Part;

import org.hartorn.htf.exception.ImplementationException;

/**
 * FileWrapper, containing the file content, the filename and the mime-type.
 *
 * @author Hartorn
 *
 */
public final class HtfFile {
    private final Part filePart;
    private final String mimeType;

    /**
     * Constructor of the wrapped file.
     *
     * @param filePartArg
     *            the file
     * @throws ImplementationException
     *             Technical exception, due to IoException
     */
    public HtfFile(final Part filePartArg) throws ImplementationException {
        this.filePart = filePartArg;
        try (InputStream input = this.filePart.getInputStream()) {
            this.mimeType = FileUtil.getMimeType(input);
        } catch (final IOException e) {
            throw new ImplementationException(e);
        }
    }

    /**
     * Getter of the filename (if one was given in the content-disposition).
     *
     * @return the filename, or null
     */
    public String getFilename() {
        return this.filePart.getSubmittedFileName();
    }

    /**
     * Return an input stream of the underlying File or HttpPart.
     *
     * @return an input stream
     * @throws IOException
     *             If an error occurs in retrieving the content as an InputStream
     */
    public InputStream getInputStream() throws IOException {
        return this.filePart.getInputStream();
    }

    /**
     * Getter of the MimeType (can be null if unknown).
     *
     * @return the mimeType
     */
    public String getMimeType() {
        return this.mimeType;
    }
}
