package org.hartorn.htf.util;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

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

    private static Gson newGson() {
        return new Gson();
    }
}
