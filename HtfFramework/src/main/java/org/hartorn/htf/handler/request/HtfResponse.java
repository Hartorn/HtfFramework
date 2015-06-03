package org.hartorn.htf.handler.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hartorn.htf.exception.ImplementationException;

/**
 * This interface represents any kind of response from a HTTP request, handled by a HtfMethod of a HtfController.
 *
 * @author Hartorn
 *
 */
public interface HtfResponse {
    /**
     * Write the response to the HttpRequest.
     *
     * @param request
     *            the http request
     * @param response
     *            the http response
     *
     * @throws UserException
     *             Exception from misuses by the user
     *
     * @throws ImplementationException
     *             Technical Exception
     *
     */
    void doWriteResponse(final HttpServletRequest request, final HttpServletResponse response) throws ImplementationException;
}
