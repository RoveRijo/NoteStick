package com.rove.domainlayer;

import android.content.Context;
import android.util.Log;

//import com.rove.datalayer.Data.DatabaseInteractor;
//import com.rove.datalayer.Data.Entity_Note;
//import com.rove.datalayer.Data.mDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Date;
import java.util.List;

//import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        //assertEquals("com.rove.domainlayer.test", appContext.getPackageName());
    }
}
