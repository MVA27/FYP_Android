package com.android.rtems.utils;

import android.os.Handler;
import android.widget.Toast;

import com.android.rtems.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    /*
    STEP 1 : Check if input is empty
    STEP 2 : Perform pattern matching
    */

    private int maxLength = 30;
    private int minLength = 1;
    private Pattern pattern;
    private Matcher matcher;
    private Handler handler;
    private Toast toast;

    public Validation(){
    }

    public Validation(Handler handler, Toast toast) {
        this.handler = handler;
        this.toast = toast;
    }

    public boolean error(int resText){ //Displays Toast error message and returns false always
        toast.setText(resText);
        handler.post(new Runnable() {
            @Override
            public void run() {
                toast.show();
            }
        });
        return false;
    }

    public boolean validateName(String target){

        if(target.length() > maxLength) return error(R.string.toast_name_length_max);
        if(target.length() < minLength) return error(R.string.toast_name_length_min);

        //Check if Name contains digits or special chars
        pattern = Pattern.compile("[^a-zA-Z]+");
        matcher = pattern.matcher(target);
        if(matcher.find()) return error(R.string.toast_name);

        return true;
    }

    public boolean validateUserName(String target){

        if(target.length() > maxLength) return error(R.string.toast_user_name_length_max);
        if(target.length() < minLength) return error(R.string.toast_user_name_length_min);

        Pattern pattern = Pattern.compile("[^a-zA-Z]");
        Matcher matcher = pattern.matcher(target);
        if(matcher.find()) return error(R.string.toast_user_name);

        //TODO : connect to server and check if user name is available
        return true;
    }

    public boolean validatePassword(String target){

        if(target.length() > maxLength) return error(R.string.toast_password_length_max);
        if(target.length() < minLength) return error(R.string.toast_password_length_min);

        //Password must contain lower-case character
        pattern = Pattern.compile("[a-z]+");
        matcher = pattern.matcher(target);
        if(!matcher.find()) return error(R.string.toast_password_lc);

        //Password must contain upper-case character
        pattern = Pattern.compile("[A-Z]+");
        matcher = pattern.matcher(target);
        if(!matcher.find()) return error(R.string.toast_password_uc);

        //Password must contain a digit
        pattern = Pattern.compile("[0-9]+");
        matcher = pattern.matcher(target);
        if(!matcher.find()) return error(R.string.toast_password_dg);

        //Password must contain a special character
        pattern = Pattern.compile("[^a-zA-Z0-9]+");
        matcher = pattern.matcher(target);
        if(!matcher.find()) return error(R.string.toast_password_sc);

        return true;
    }

    public boolean validateAge(String target){

        int maxLength = 3;

        if(target.length() > maxLength) return error(R.string.toast_age);
        if(target.length() < minLength) return error(R.string.toast_age);

        int age = 0;

        try{
            age = Integer.parseInt(target);
        }
        catch (NumberFormatException e) {
            return error(R.string.toast_age);
        }

        if(age >= 200) return error(R.string.toast_age);

        return true;
    }

    public boolean validatePhoneNumber(String target){

        int maxLength = 10;
        int minLength = 10;

        if(target.length() > maxLength) return error(R.string.toast_phone_number_length_max);
        if(target.length() < minLength) return error(R.string.toast_phone_number_length_min);

        //Password must contain lower-case character
        pattern = Pattern.compile("[7-9][0-9]{9}");
        matcher = pattern.matcher(target);
        if(!matcher.find()) return error(R.string.toast_phone_number);

        return true;
    }

}
