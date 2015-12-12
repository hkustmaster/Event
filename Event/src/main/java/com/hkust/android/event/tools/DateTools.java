package com.hkust.android.event.tools;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

/**
 * Created by hozdanny on 15/12/8.
 */
public class DateTools {

    public ArrayList<String> getDateList(String startDate, String endDate){
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime startDateTime = DateTime.parse(startDate, format);
        DateTime endDateTime = DateTime.parse(endDate, format);
        ArrayList<String> dates = new ArrayList<String>();
        while(startDateTime.isBefore(endDateTime)){
            dates.add(startDateTime.toString(format));
            startDateTime = startDateTime.plusDays(1);
        }
        dates.add(endDate);
        return dates;

    }
}
