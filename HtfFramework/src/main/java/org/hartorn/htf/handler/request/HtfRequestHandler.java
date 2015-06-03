package org.hartorn.htf.handler.request;

import java.io.IOException;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hartorn.htf.annotation.AnnotationHelper;
import org.hartorn.htf.annotation.FromUrl;
import org.hartorn.htf.exception.ImplementationException;
import org.hartorn.htf.exception.NotYetImplementedException;
import org.hartorn.htf.exception.UserException;
import org.hartorn.htf.handler.path.UrlResolver;
import org.hartorn.htf.util.JsonHelper;
import org.hartorn.htf.util.StringUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Class to handle receiving the request, and sending the response.
 *
 * @author Hartorn
 *
 */
public enum HtfRequestHandler {
    ;
    private static final Logger LOG = LogManager.getLogger();

    private HtfRequestHandler() {
        // Private constructor.
    }

    public static HtfResponse getHtfResponse(final Object controller, final Method method, final Object... methodParams)
            throws ImplementationException {
        HtfRequestHandler.LOG.debug("HTF - Invoking controller [{}] with method [{}]", controller.getClass().getCanonicalName(), method.getName());

        try {
            return (HtfResponse) method.invoke(controller, methodParams);
        } catch (final IllegalAccessException | IllegalArgumentException e) {
            throw new ImplementationException("Error while invocating controller", e);
        } catch (final InvocationTargetException e) {
            final Throwable cause = e.getCause();
            if ((cause != null) && (cause instanceof UserException)) {
                return HtfRequestHandler.handleUserException((UserException) cause);
            } else {
                throw new ImplementationException(cause != null ? cause : e);
            }
        }
    }

    public static Object[] getMethodParametersFromRequest(final Method method, final HttpServletRequest request) throws ImplementationException {
        Object[] params;
        // 2 - Identify Content type, and build basics arguments
        switch (StringUtil.emptyIfNull(request.getContentType())) {
            case "application/json":
                params = HtfRequestHandler.getMethodParametersFromJsonRequest(method, request);
                break;
            default:
                throw new ImplementationException("Cannot handle this kind of content type " + request.getContentType());
        }

        return params;
    }

    private static void addAllParameters(final List<Object> params, final Type[] paramTypes, final String[] pathParts) {
        for (int indexParam = 0; indexParam < paramTypes.length; indexParam++) {
            final Type type = paramTypes[indexParam];
            params.add(JsonHelper.getPrimitiveObjectFromString(pathParts[paramTypes.length - 1 - indexParam], type));
        }
    }

    private static Object[] getMethodParametersFromJsonRequest(final Method method, final HttpServletRequest request) throws ImplementationException {
        final Type[] paramTypes = method.getGenericParameterTypes();
        // If no parameters, no parse or anything
        if (paramTypes.length == 0) {
            return null;
        }
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();

        final List<Object> params = new ArrayList<Object>();
        final int nbAnnotatedParams = AnnotationHelper.getNumberOfAnnotatedParameters(method, FromUrl.class);
        // Initialise pathPArts and offset
        String[] pathParts = null;
        int offset = 0;
        if (nbAnnotatedParams != 0) {
            pathParts = StringUtil.strip(UrlResolver.getControllerUrl(request), '/').split("/");
            // Calculate offset for urlParams
            offset = paramTypes.length - nbAnnotatedParams;
        }
        // If only params from url
        if (nbAnnotatedParams == paramTypes.length) {
            HtfRequestHandler.addAllParameters(params, paramTypes, pathParts);
        } else {

            // If mixed
            final JsonParser jsonParser = new JsonParser();
            // Try-with for the request reader
            try (final Reader reader = request.getReader()) {
                final JsonElement jsonTree = jsonParser.parse(reader);
                int nbUrlParams = 0;
                int nbRqParams = 0;
                // For each needed params
                for (int indexParam = 0; indexParam < paramTypes.length; indexParam++) {
                    // You get the type
                    final Type type = paramTypes[indexParam];
                    // If parameter is to get from the url
                    if (AnnotationHelper.containsAnnotation(paramAnnotations[indexParam], FromUrl.class)) {
                        if (nbUrlParams > paramTypes.length) {
                            throw new ImplementationException("Too many parameters from url");
                        }
                        params.add(JsonHelper.getPrimitiveObjectFromString(pathParts[offset + nbUrlParams], type));
                        nbUrlParams++;
                    } else {
                        // Else the parameter is from the request body

                        // If Json is an object, primitive or null, must have only one body parameter
                        if (!jsonTree.isJsonArray() && (nbRqParams != 0)) {
                            throw new ImplementationException("Too many method parameters : only one Json object >" + jsonTree.toString());
                        } else if (!jsonTree.isJsonArray()) {
                            // Get unique parameter (or else exception for next one)
                            params.add(JsonHelper.getObjectFromJsonElement(jsonTree, type));
                        } else {
                            final JsonArray jsonArray = (JsonArray) jsonTree;
                            params.add(JsonHelper.getObjectFromJsonElement(jsonArray.get(nbRqParams), type));
                        }
                        nbRqParams++;
                    }
                }
            } catch (final IOException e) {
                throw new ImplementationException(e);
            }
        }
        return params.toArray();
    }

    private static HtfResponse handleUserException(final UserException e) {
        throw new NotYetImplementedException("Handling of User Exception is not yet done");
    }
}
