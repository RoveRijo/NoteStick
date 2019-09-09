package com.rove.notestick.Util;

import java.text.DateFormat;
import java.text.ParseException;
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
    public static class StringToDateTimeBuilder{
        private String dateTimeString;

        /**
         * This class creates a java.util.Date object from a formatted string
         * @param dateTimeString The format should be EEEE, MMMM dd yyyy hh:mm:ss a
         *                       for example Thursday, Dec 31 1998 23:37:50 AM
         */
        public StringToDateTimeBuilder(String dateTimeString) {
            this.dateTimeString = dateTimeString;
        }

        /**
         * Method will create and return a java.util.Date object as per the
         * @link StringToDateTimeBuilder class instance
         * @return Date object
         * @throws ParseException if the input is not compatible for parsing
         */
        public Date build() throws ParseException {
            SimpleDateFormat formatter=new SimpleDateFormat("EEEE, MMMM dd yyyy hh:mm:ss a");
            Date date = formatter.parse(dateTimeString);
            return date;
        }
    }

}
