package org.hartorn.htf.handler.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hartorn.htf.exception.ImplementationException;

/**
 * This response build the JSON response for the given request, returning the objects given in constructor.
 *
 * @author Hartorn
 *
 */
public class JsonResponse implements HtfResponse {

    /**
     * Constructor.
     */
    public JsonResponse() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void doWriteResponse(final HttpServletRequest request, final HttpServletResponse response) throws ImplementationException {
        // TODO Auto-generated method stub

    }

}
