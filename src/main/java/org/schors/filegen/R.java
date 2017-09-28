package org.schors.filegen;

import com.vaadin.server.VaadinSession;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class R {

    private static Map<Locale, ResourceBundle> resourceBundleMap = new HashMap<>();

    public static String get(String key) {
        return get(key, VaadinSession.getCurrent().getLocale());
    }

    public static String get(String key, Locale locale) {
        String result = null;
        ResourceBundle resourceBundle = resourceBundleMap.get(locale);
        if (resourceBundle == null) {
            resourceBundle = ResourceBundle.getBundle("filegen", locale);
            if (resourceBundle != null) {
                resourceBundleMap.put(locale, resourceBundle);
            }
        }
        if (resourceBundle != null) {
            result = resourceBundle.getString(key);
        }
        return result;
    }
}
