package com.mtx.mobile.employee.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DieHard_04 on 24-05-2021.
 */
public class Common {

    public static String getCurrentDateAndTime() {
        String pattern = "dd-MM-yyyy";
        String dateInString = new SimpleDateFormat(pattern).format(new Date());
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentDateTimeString = sdf.format(d);
        return dateInString + " " + currentDateTimeString;
    }

    //cnvert date to milliseconds in long
    public static long convertdateToLong(String s) {
        String string_date = s;

        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {
            Date d = f.parse(string_date);
            long milliseconds = d.getTime();
            return milliseconds;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;

    }
}
