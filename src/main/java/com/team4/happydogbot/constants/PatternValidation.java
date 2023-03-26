package com.team4.happydogbot.constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternValidation {

    public static final String REPORT_REGEX =
            "(Рацион:)(\\s)[А-Яа-я0-9\\s\\W;\\-]{50,300}(;)(\\s)" +
                    "(Самочувствие:)(\\s)[А-Яа-я0-9\\s\\W;\\-]{50,300}(;)(\\s)" +
                    "(Поведение:)(\\s)[А-Яа-я0-9\\s\\W]{50,300}";

    public static boolean validationPatternReport(String textRegEx) {
        Pattern reportPattern = Pattern.compile(REPORT_REGEX);
        Matcher reportMatcher = reportPattern.matcher(textRegEx);
        return reportMatcher.matches();
    }
}
