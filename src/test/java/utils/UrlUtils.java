package utils;

import java.util.regex.*;

public class UrlUtils {

    public static String extractEmpId (String url) {
        Pattern pattern = Pattern.compile("/empNumbers/(\\d+)");
        Matcher matcher = pattern.matcher(url);
        String empId ="";
        if(matcher.find()) {
            empId = matcher.group(1);
        }
        return empId;
    }
}
