package com.hkust.android.event.tools;

import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sam on 15/12/1.
 */
public class ValidFormTools {
    /*
    * validate email tool*/
    public boolean isValidEmailAddress(String email) {
        /*String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();*/
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidateName(String name) {
        String regx = "^[\\p{L} .'-]+$";
        Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        return matcher.find();
    }

    public boolean isValidatePassword(String password, String re_password){
        if(password.equals(re_password))
            return true;
        return false;
    }

    public boolean isValidatePasswordLength(String password){
        if(password.length()>=6&&password.length()<=18){
            return true;
        }else{
            return false;
        }
    }
    public boolean isValidCellPhone(String number)
    {
        return android.util.Patterns.PHONE.matcher(number).matches();
    }

    public boolean isValidEndDate(String startDate, String endDate){
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime startDateTime = DateTime.parse(startDate, format);
        DateTime endDateTime = DateTime.parse(endDate, format);
        return startDateTime.isBefore(endDateTime);
    }



    public boolean isValidStartDateTime(String startDate, String time){
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd-hh:mm");
        DateTime startDateTime = DateTime.parse(startDate + "-" + time, format);
        return !startDateTime.isBeforeNow();

    }
}
