package com.rove.notestick.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {
    private Date date;

    public DateParser(Date date) {
        this.date = date;
    }

    public String getDate() {
        String strDateFormat = "dd";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);

        return formattedDate;
    }

    public String getDay() {

        String strDateFormat = "EEEE";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);

        return formattedDate;

    }

    public String getMonth() {
        String strDateFormat = "MMMM";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);

        return formattedDate;
    }

    public String getYear() {
        String strDateFormat = "yyyy";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);

        return formattedDate;
    }

    public String getTime() {
        String strDateFormat = "hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);

        return formattedDate;
    }

}
