package com.api.user.constants;

public class Constant {

    public static final String REGEX_EMAIL = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[CLcl]{2,6}$";
    public static final String REGEX_PASSWORD ="^(\\d{2}[A-Z]{1}[a-z]+|\\d{1}[a-z]+[A-Z]{1}\\d{1}([a-z]+)?|([a-z]+)?[A-Z]{1}([a-z]+)?\\d{1}[a-z]+\\d{1}([a-z]+)?|[a-z]+\\d{1}([a-z]+)?[A-Z]{1}([a-z]+)?\\d{1}([a-z]+)?|([a-z]+)?\\d{1}([a-z]+)?[A-Z]{1}[a-z]+\\d{1}([a-z]+)?|([a-z]+)?\\d{1}[a-z]+\\d{1}([a-z]+)?[A-Z]{1}([a-z]+)?|([a-z]+)?\\d{2}[a-z]+[A-Z]{1}([a-z]+)?|[a-z]+[A-Z]{1}([a-z]+)?\\d{2}([a-z]+)?|[a-z]+\\d{2}([a-z]+)?[A-Z]{1}([a-z]+)?|([a-z]+)?[A-Z]{1}([a-z]+)?\\d{2}[a-z]+|([a-z]+)?[A-Z]{1}[a-z]+\\d{2}([a-z]+)?)$";
    public static final String HEADER = "Authorization";
    public static final String PREFIX = "Bearer ";
    public static final String USER_NOT_FOUND =  "USER NOT FOUND";
    public static final String NO_DATA_FOUND =  "NO DATA FOUND";
   

    private Constant() {

    }

}
