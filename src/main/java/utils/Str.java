package utils;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * authored at
 * https://www.programcreek.com/2011/03/java-method-for-spliting-a-camelcase-string/
 */
public class Str {
    public static LinkedList<String> splitCamelCaseString(String s){
       return new LinkedList<String>(
               Arrays.asList(s.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"))
       );
    }
    public static String snakeCaseOfCamelCase(String s) {
        String[] list = splitCamelCaseString(s).toArray(new String[0]);
        StringBuilder sb = new StringBuilder();
        for (String l: list) {
            sb.append(l.toLowerCase()).append("_");
        }
        if (sb.lastIndexOf("_") > -1) sb.deleteCharAt(sb.lastIndexOf("_"));
        return sb.toString();
    }
}
