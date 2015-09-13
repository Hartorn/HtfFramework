package org.hartorn.htf.handler.request;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hartorn.htf.annotation.AnnotationHelper;
import org.hartorn.htf.annotation.FromUrl;
import org.hartorn.htf.exception.ImplementationException;
import org.hartorn.htf.exception.NotYetImplementedException;
import org.hartorn.htf.exception.UserException;
import org.hartorn.htf.file.HtfFile;
import org.hartorn.htf.handler.path.UrlResolver;
import org.hartorn.htf.util.HtmlConstants;
import org.hartorn.htf.util.JsonUtil;
import org.hartorn.htf.util.Pair;
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
    private static final String TMP_FOLDER_PATH = System.getProperty("java.io.tmpdir") + File.separator + "HTF";
    private static final File TMP_FOLDER = new File(HtfRequestHandler.TMP_FOLDER_PATH);
    static {
        HtfRequestHandler.TMP_FOLDER.mkdirs();
    }

    private HtfRequestHandler() {
        // Private constructor.
    }

    /**
     * Invoke the method on the given controller, with the given arguments. Also handle the user exception.
     *
     * @param controller
     *            the controller
     * @param method
     *            the method
     * @param methodParams
     *            the arguments of the given method
     * @return the build HtfResponse
     * @throws ImplementationException
     *             the exception happened (can be unchecked exception caught)
     */
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

    /**
     * Extract the parameters from the request, for all handled content type.
     *
     * @param method
     *            the method for which the parameters are extracted from the request
     * @param request
     *            the received request
     * @return the parameters for the method
     * @throws ImplementationException
     *             Technical exception, from extracting the parameters (mainly IOException)
     */
    public static Object[] getMethodParametersFromRequest(final Method method, final HttpServletRequest request) throws ImplementationException {
        Object[] params = null;
        final String contentType = StringUtil.emptyIfNull(request.getContentType());
        try {
            // 2 - Identify Content type, and build basics arguments
            if (StringUtil.EMPTY.equals(contentType)) {
                // Nothing to do
            } else if (HtmlConstants.ContentType.JSON.isContentType(contentType)) {
                params = HtfRequestHandler.getMethodParametersFromJsonRequest(method, request);
            } else {
                throw new ImplementationException("Cannot handle this kind of content type " + request.getContentType());
            }
        } catch (final RuntimeException e) {
            // From unchecked to checked Exception
            throw new ImplementationException(e);
        }

        return params;
    }

    private static void addAllParameters(final List<Object> params, final Type[] paramTypes, final String[] pathParts) {
        for (int indexParam = 0; indexParam < paramTypes.length; indexParam++) {
            final Type type = paramTypes[indexParam];
            params.add(JsonUtil.getPrimitiveObjectFromString(pathParts[paramTypes.length - 1 - indexParam], type));
        }
    }

    private static List<Pair<String, HtfFile>> extractFilesFromRequest(final HttpServletRequest request) throws ImplementationException {
        final List<Pair<String, HtfFile>> fileList = new ArrayList<Pair<String, HtfFile>>();

        Collection<Part> fileParts;
        try {
            fileParts = request.getParts();
        } catch (final IOException | ServletException e) {
            throw new ImplementationException(e);
        }

        for (final Part filePart : fileParts) {
            fileList.add(Pair.of(filePart.getName(), new HtfFile(filePart)));
        }

        return fileList;
    }

    /**
     * Extract the parameters from the request, if sent in JSON.
     *
     * @param method
     *            the method for which the arguments are extracted
     * @param request
     *            the request received
     * @return the parameters for the methods
     * @throws ImplementationException
     *             Technical Exception, wrapping IOException
     */
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
            offset = pathParts.length - nbAnnotatedParams;
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
                        params.add(JsonUtil.getPrimitiveObjectFromString(pathParts[offset + nbUrlParams], type));
                        nbUrlParams++;
                    } else {
                        // Else the parameter is from the request body

                        // If Json is an object, primitive or null, must have only one body parameter
                        if (!jsonTree.isJsonArray() && (nbRqParams != 0)) {
                            throw new ImplementationException("Too many method parameters : only one Json object >" + jsonTree.toString());
                        } else if (!jsonTree.isJsonArray() || ((paramTypes.length - nbAnnotatedParams) == 1)) {
                            // Get unique parameter (or else exception for next one)
                            params.add(JsonUtil.getObjectFromJsonElement(jsonTree, type));
                        } else {
                            final JsonArray jsonArray = (JsonArray) jsonTree;
                            params.add(JsonUtil.getObjectFromJsonElement(jsonArray.get(nbRqParams), type));
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
