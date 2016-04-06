package org.composi;

import java.util.HashMap;
import java.util.Map;

class EmailRegex {
    private static String emailMatch = "\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}\\b";
    static Map<String, String> map;
    static {
        map = new HashMap<String, String>();
        map.put("email", emailMatch);
    }
}
