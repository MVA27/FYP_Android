package com.android.rtems.utils;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    private Pattern pattern;
    private Matcher matcher;

    Predicate<String> isInvalid = target -> {
        int length = 30;
        if(target.length() > length) return true;
        else if(target.isEmpty()) return true;
        else return false;
    };

    public boolean validateName(String target){
        System.out.println("target Name = "+target+" len = "+isInvalid.test(target));

        if(isInvalid.test(target)) return false;

        //Check if Name contains digits or special chars
        pattern = Pattern.compile("[^a-zA-Z]+");
        matcher = pattern.matcher(target);
        if(matcher.find()) return false;

        return true;
    }

    public boolean validateUserName(String target){
        //TODO : connect to server and check if user name is available
        return false;
    }

    public boolean validatePassword(String target){
        System.out.println("target Pswd= "+target);

        if(isInvalid.test(target)) return false;

        //Password must contain lower-case character
        pattern = Pattern.compile("[a-z]+");
        matcher = pattern.matcher(target);
        if(!matcher.find()) return false;

        //Password must contain upper-case character
        pattern = Pattern.compile("[A-Z]+");
        matcher = pattern.matcher(target);
        if(!matcher.find()) return false;

        //Password must contain a digit
        pattern = Pattern.compile("[0-9]+");
        matcher = pattern.matcher(target);
        if(!matcher.find()) return false;

        //Password must contain a special character
        pattern = Pattern.compile("[^a-zA-Z0-9]+");
        matcher = pattern.matcher(target);
        if(!matcher.find()) return false;

        return true;
    }

    public boolean validateAge(String target){
        System.out.println("target Age = "+target);

        if(isInvalid.test(target)) return false;

        int age = 0;

        try{
            age = Integer.parseInt(target);
        }
        catch (NumberFormatException e) {
            return false;
        }

        if(age >= 200) return false;

        return true;
    }

    public boolean validatePhoneNumber(String target){
        System.out.println("target Num = "+target);

        //TODO : if(target.length() != 10) return false;

        //Password must contain lower-case character
        pattern = Pattern.compile("[7-9][0-9]{9}");
        matcher = pattern.matcher(target);
        if(!matcher.find()) return false;

        return true;
    }

}
