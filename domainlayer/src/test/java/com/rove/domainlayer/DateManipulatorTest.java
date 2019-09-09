package com.rove.domainlayer;

import android.util.Log;

import com.rove.domainlayer.Util.DateManipulator;

import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DateManipulatorTest {
    private final String TAG1 = "Current Date is: ";
    private final String TAG2 = "Next Date is: ";
    private final String TAG3 = "Not Trimmed Date is: ";
    private final String TAG4 = "Trimmed Date is: ";
    @Test
    public void test_getNextDay(){
        Date currentDate = new Date();
        Date nextDate = DateManipulator.getNextDay(currentDate);
//        Log.d(TAG,"Current Date is: "+currentDate);
//        Log.d(TAG,"Next Date is: "+nextDate);
        System.out.println(TAG1+currentDate);
        System.out.println(TAG2+nextDate);
        assert true;
    }
    @Test
    public void test_getTrimmedDate(){
        Date currentDate = new Date();
        Date trimmedDate = new Date();
        try {
            trimmedDate = DateManipulator.getTrimmedDate(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
            assert false;
        }
//        Log.d(TAG,"Current Date is: "+currentDate);
//        Log.d(TAG,"Next Date is: "+nextDate);
        System.out.println(TAG3+currentDate);
        System.out.println(TAG4+trimmedDate);
        assert true;
    }
}