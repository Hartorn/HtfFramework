package org.hartorn.htf.handler.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hartorn.htf.exception.ImplementationException;
import org.hartorn.htf.util.JsonUtil;

/**
 * This response build the JSON response for the given request, returning the objects given in constructor.
 *
 *
 * @author Hartorn
 * @param <D>
 *            Precise type of the given object
 */
public final class JsonResponse<D> implements HtfResponse {

    private final D toSerialize;

    /**
     * Constructor.
     *
     * @param answer
     *            the object from where the answer will be constructed
     */
    public JsonResponse(final D answer) {
        this.toSerialize = answer;
    }

    @Override
    public void doWriteResponse(final HttpServletRequest request, final HttpServletResponse response) throws ImplementationException {
        response.setContentType("application/json");
        JsonUtil.writeObjectToResponse(this.toSerialize, response);
    }

}
