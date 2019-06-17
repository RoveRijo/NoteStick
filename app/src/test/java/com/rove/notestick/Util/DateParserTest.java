package com.rove.notestick.Util;

import org.junit.Test;

import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DateParserTest {
    Date date = new Date();
    DateParser dateParser = new DateParser(date);
    //String TAG = "DateParser";
    @Test
    public void dateparsing_isCorrect() {
        Log(date.toString());
        String Day,Date,Month,Time,Year;
        Date = dateParser.getDate();
        Day = dateParser.getDay();
        Month = dateParser.getMonth();
        Year = dateParser.getYear();
        Time = dateParser.getTime();

        Log(Date);
        Log(Day);
        Log(Month);
        Log(Year);
        Log(Time);
        assert(true);
    }
    public void Log(String string){
        System.out.println(string);
    }
}