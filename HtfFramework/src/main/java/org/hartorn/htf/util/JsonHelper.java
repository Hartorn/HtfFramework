package org.hartorn.htf.util;

import java.io.IOException;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletResponse;

import org.hartorn.htf.exception.ImplementationException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

/**
 * Class gathering all the utility methods for handling JSON data.
 *
 * @author Hartorn
 *
 */
public enum JsonHelper {
    ;
    private static final Gson GSON = JsonHelper.newGson();

    private JsonHelper() {
        // private constructor, for helper class.
    }

    /**
     * Build an object of class type from this json element.
     *
     * @param jsonElt
     *            the source element
     * @param type
     *            the type of the wanted object
     * @return the new object
     */
    public static Object getObjectFromJsonElement(final JsonElement jsonElt, final Type type) {
        return JsonHelper.GSON.fromJson(jsonElt, type);
    }

    /**
     * Build an object of class type from this json primitive.
     *
     * @param json
     *            the source element
     * @param type
     *            the type of the wanted object
     * @return the new object
     */
    public static Object getPrimitiveObjectFromString(final String json, final Type type) {
        return JsonHelper.GSON.fromJson(new JsonPrimitive(json), type);
    }

    /**
     * Write the given object to the reponse, without losing any generic information.
     *
     * @param object
     *            the given object
     * @param response
     *            the response
     * 
     * @param <D>
     *            type of the object (including generics)
     * @throws ImplementationException
     *             Technical Exception, caused by IOException
     */
    public static <D> void writeObjectToResponse(final D object, final HttpServletResponse response) throws ImplementationException {
        try (JsonWriter jsonWriter = new JsonWriter(response.getWriter())) {
            final Type answerType = new TypeToken<D>() {
            }.getType();
            JsonHelper.GSON.toJson(object, answerType, jsonWriter);
        } catch (final IOException e) {
            throw new ImplementationException(e);
        }
    }

    private static Gson newGson() {
        return new Gson();
    }

}
