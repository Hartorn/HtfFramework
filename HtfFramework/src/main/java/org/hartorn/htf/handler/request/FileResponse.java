package org.hartorn.htf.handler.request;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tika.Tika;
import org.hartorn.htf.exception.ImplementationException;

/**
 * This response build the File response for the given request, returning file given in constructor.
 *
 *
 * @author Hartorn
 */
public final class FileResponse implements HtfResponse {
    private final static Tika CONTENT_DETECTOR = new Tika();
    private final File toDownload;

    /**
     * Constructor.
     *
     * @param file
     *            the file to send in the answer
     */
    public FileResponse(final File file) {
        this.toDownload = file;
    }

    @Override
    public void doWriteResponse(final HttpServletRequest request, final HttpServletResponse response) throws ImplementationException {

        try (ServletOutputStream responseWriter = response.getOutputStream();) {
            response.setHeader("Content-Type", FileResponse.CONTENT_DETECTOR.detect(this.toDownload));
            response.setHeader("Content-Length", String.valueOf(this.toDownload.length()));
            response.setHeader("Content-Disposition", "inline; filename=\"" + this.toDownload.getName() + "\"");
            Files.copy(this.toDownload.toPath(), responseWriter);
        } catch (final IOException e) {
            throw new ImplementationException("Error while writing file to Http response", e);
        }
    }
}
