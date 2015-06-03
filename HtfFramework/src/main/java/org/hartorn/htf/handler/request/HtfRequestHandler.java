package org.hartorn.htf.handler.request;

import java.io.IOException;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hartorn.htf.annotation.AnnotationHelper;
import org.hartorn.htf.annotation.FromUrl;
import org.hartorn.htf.annotation.HtfController;
import org.hartorn.htf.annotation.HtfMethod.HttpVerbs;
import org.hartorn.htf.exception.ImplementationException;
import org.hartorn.htf.handler.path.UrlResolver;
import org.hartorn.htf.util.Pair;
import org.hartorn.htf.util.StringUtil;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * Main class for handling the request, directing to right method and controller.
 *
 * @author Hartorn
 *
 */
public final class HtfRequestHandler extends HttpServlet {
    /**
     * Serial ID.
     */
    private static final long serialVersionUID = -6533912768946164886L;
    private static final Logger LOG = LogManager.getLogger();

    private static final String SERVLET_INFO = "HtfRequestHandler, part of Htf Framework (author Hartorn)";

    private UrlResolver pathResolver;

    /**
     * Constructor.
     */
    public HtfRequestHandler() {
        super();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.GenericServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return HtfRequestHandler.SERVLET_INFO;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.GenericServlet#init()
     */
    @Override
    public void init() throws ServletException {
        HtfRequestHandler.LOG.info("HTF - Initialising ...");
        // Initialise the servlet resources
        final Set<Class<?>> controllers = AnnotationHelper.getAnnotatedClasses(HtfController.class);
        try {
            this.pathResolver = new UrlResolver(controllers);
        } catch (final ImplementationException e) {
            throw new ServletException(e);
        }
        HtfRequestHandler.LOG.info("HTF - Initialisation finished");
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest , javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.doHandleRequest(HttpVerbs.DELETE, req, resp);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest , javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.doHandleRequest(HttpVerbs.GET, req, resp);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServlet#doHead(javax.servlet.http.HttpServletRequest , javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doHead(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.doHandleRequest(HttpVerbs.HEAD, req, resp);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest , javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.doHandleRequest(HttpVerbs.POST, req, resp);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest , javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.doHandleRequest(HttpVerbs.PUT, req, resp);
    }

    private void doHandleRequest(final HttpVerbs verb, final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {
        try {
            this.handleRequest(verb, req, resp);
        } catch (final ImplementationException e) {
            HtfRequestHandler.LOG.error("Exception happened while handling request from [{}] with HttpVerb [{}]", req.getRequestURL(), verb.name());
            HtfRequestHandler.LOG.error(e);
            throw new ServletException(e);
        }
    }

    private HtfResponse getHtfResponse(final Object controller, final Method method, final Object... methodParams) throws ImplementationException {
        HtfRequestHandler.LOG.debug("HTF - Invoking controller [{}] with method [{}]", controller.getClass().getCanonicalName(), method.getName());

        try {
            return (HtfResponse) method.invoke(controller, methodParams);
        } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            HtfRequestHandler.LOG.error(e);
            throw new ImplementationException("Error while invocating controller", e);
        }
    }

    private Object[] getMethodParameters(final Method method, final HttpServletRequest request) throws IOException, ImplementationException {
        final Type[] paramTypes = method.getGenericParameterTypes();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();

        if (paramTypes.length == 0) {
            // If no parameters, no no parse or anything
            return null;
        }
        final Gson gson = new Gson();

        final List<Object> params = new ArrayList<Object>();
        final int nbAnnotatedParams = AnnotationHelper.getNumberOfAnnotatedParameters(method, FromUrl.class);
        String[] pathParts = null;
        if (nbAnnotatedParams != 0) {
            pathParts = StringUtil.strip(UrlResolver.getControllerUrl(request), '/').split("/");
        }
        // If only params from url
        if (nbAnnotatedParams == paramTypes.length) {
            for (int indexParam = 0; indexParam < paramTypes.length; indexParam++) {
                final Type type = paramTypes[indexParam];
                params.add(gson.fromJson(pathParts[paramTypes.length - 1 - indexParam], type));
            }
            return params.toArray();
        }

        // If mixed
        final JsonParser jsonParser = new JsonParser();
        final Reader reader = request.getReader();
        final JsonElement jsonTree = jsonParser.parse(reader);
        int nbUrlParams = 0;
        int nbRqParams = 0;

        for (int indexParam = 0; indexParam < paramTypes.length; indexParam++) {
            final Type type = paramTypes[indexParam];
            if (AnnotationHelper.containsAnnotation(paramAnnotations[indexParam], FromUrl.class)) {
                params.add(gson.fromJson(new JsonPrimitive(pathParts[paramTypes.length - 1 - nbUrlParams]), type));
                nbUrlParams++;
            } else {
                if (!jsonTree.isJsonArray() && (nbRqParams != 0)) {
                    throw new ImplementationException("Error while getting method parameters");
                } else if (!jsonTree.isJsonArray()) {
                    params.add(gson.fromJson(jsonTree, type));
                } else {
                    final JsonArray jsonArray = (JsonArray) jsonTree;
                    params.add(gson.fromJson(jsonArray.get(nbRqParams), type));
                }
                nbRqParams++;
            }
        }
        return params.toArray();
    }

    private void handleRequest(final HttpVerbs verb, final HttpServletRequest req, final HttpServletResponse resp) throws ImplementationException,
    IOException {
        HtfRequestHandler.LOG.debug("HTF - Handling request from [{}] with HttpVerb [{}]", req.getRequestURL(), verb.name());

        // 1 - Identify controller and method
        final Pair<Object, Method> ctrlMethod = this.pathResolver.resolveRequest(req, verb);

        final Method method = ctrlMethod.right();
        final Object ctrl = ctrlMethod.left();
        Object[] argToCall = null;

        // 2 - Identify Content type, and build basics arguments
        if ("application/json".equals(req.getContentType())) {
            req.getParameterMap();
            argToCall = this.getMethodParameters(method, req);
        }

        // final Type[] args = ctrlMethod.right().getGenericParameterTypes();
        // final AnnotatedType[] annArgs = ctrlMethod.right().getAnnotatedParameterTypes();
        // for (final AnnotatedType type : annArgs) {
        // if (type.isAnnotationPresent(FromUrl.class)) {
        // System.out.println("ANNOTATION");
        // }
        // }
        // final Annotation[][] annotations = ctrlMethod.right().getParameterAnnotations();
        // Object argToCall = null;
        // for (final Type arg : args) {
        // HtfRequestHandler.LOG.debug("Type :{}", arg.getTypeName());
        // if (arg instanceof ParameterizedType) {
        // final ParameterizedType type = (ParameterizedType) arg;
        // HtfRequestHandler.LOG.debug("Parametized type :{}", type.getTypeName());
        // argToCall = JsonHelper.getObjectFromJson(req, type);
        // }

        // ParameterizedTypeImpl typeImpl = new ParameterizedTypeImpl();
        // typeImpl.
        // final Type fooType = new TypeToken<Foo<Bar>>() {
        // }.getType();
        //
        // }
        // for (final Type t : argTypes) {
        // HtfRequestHandler.LOG.debug("ArgType :{}", t.getTypeName());
        // }

        // }
        // 3 - Get the answer
        final HtfResponse htfResponse = this.getHtfResponse(ctrl, method, argToCall);

        // 4 - Write the answer
        HtfRequestHandler.LOG.debug("HTF - Writing response for request from [{}] with HttpVerb [{}]", req.getRequestURL(), verb.name());
        htfResponse.doWriteResponse(req, resp);
        HtfRequestHandler.LOG.debug("HTF - Finished handling request from [{}] with HttpVerb [{}]", req.getRequestURL(), verb.name());
    }

}
