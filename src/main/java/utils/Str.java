package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<String> result = new ArrayList<>();
        for (String s1 : splitCamelCaseString(s)) {
            String toLowerCase = s1.toLowerCase();
            result.add(toLowerCase);
        }
        String[] list = result.toArray(new String[0]);
        return String.join("_", list);
    }
}
