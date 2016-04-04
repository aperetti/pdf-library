package org.composi;

import java.util.HashMap;
import java.util.Map;

class Pud {
    private static String cuMatch = "[A-Za-z]{2}\\d{2}[A-Za-z]+|[A-Za-z]{1,2}\\d{4}[A-Za-z]{0,3}";
    private static String asMatch = "(?:12[FVU]|1U|1L|1J|115[FHPVW])\\d{3}(?:A|STL)?" ;
    private static String tdMatch = "4 ?- ?\\d+ ?- ?\\d+\\.\\d+";
    private static String matMatch = "(?<=\\s)\\d{6,7}(?=[\\s\\.])";
    static Map<String, String> map;
    static {
        map = new HashMap<String, String>();
        map.put("CU",Pud.cuMatch);
        map.put("AS",Pud.asMatch);
        map.put("TD",Pud.tdMatch);
        map.put("MM",Pud.matMatch);
    }
}
