package org.hartorn.htf.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

/**
 * Class gathering all the utility methods for handling JSON data.
 *
 * @author Hartorn
 *
 */
public enum JsonHelper {
    ;

    private JsonHelper() {
        // private constructor, for helper class.
    }

    public static Object getObjectFromJson(final HttpServletRequest request, final Type t) throws IOException {
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            return JsonHelper.newGson().fromJson(reader, t);
        } finally {
            reader.close();
        }
    }

    private static Gson newGson() {
        return new Gson();
    }
}
