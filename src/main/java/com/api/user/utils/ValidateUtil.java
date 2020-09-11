package com.api.user.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.api.user.constants.Constant;

public final class ValidateUtil {
    
    private ValidateUtil() {
        
    }

    public static final boolean isValidEmail(final String email) {
        Pattern pattern = Pattern.compile(Constant.REGEX_EMAIL);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static final boolean isValidPassword(final String password) {
        Pattern pattern = Pattern.compile(Constant.REGEX_EMAIL);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

}
