package com.rove.datalayer;

import android.content.Context;
import android.util.Log;

import com.rove.datalayer.Data.DatabaseInteractor;
import com.rove.datalayer.Data.Entity_Note;
import com.rove.datalayer.Data.mDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class testDao {
    @Rule
    MockitoRule mockitoRule = MockitoJUnit.rule();
    private mDao Dao;
    private DatabaseInteractor db;
    private Date date = new Date();
    private String Title = "Title";
    private String TitleUpdate = "Titleupdated";
    private String Content = "content";
    private String ImgUrl = "imgurl";
    private final String Tag = "testDao";
    private Context context;

//    @Mock
//    LifecycleOwner lifecycleOwner;

    Lifecycle lifecycle;

    public testDao() {

    }

    @Before
    public void createDb() {
        context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, DatabaseInteractor.class).build();
        Dao = db.getDao();
        Log.d(Tag, "db created");
        //lifecycle = new LifecycleRegistry(lifecycleOwner);
        //((LifecycleRegistry) lifecycle).handleLifecycleEvent(Lifecycle.Event.ON_RESUME);

    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeNoteAndReadNote() throws Exception {
        final Entity_Note note = new Entity_Note();
        note.setDate(date);
        note.setTitle(Title);
        note.setContent(Content);
        note.setImgUrl(ImgUrl);
        Dao.insertNote(note);
        Log.d(Tag, "One note inserted");


//        Dao.getNotesContainsTitle(Title).observe(lifecycleOwner, new Observer<List<Entity_Note>>() {
//            @Override
//            public void onChanged(List<Entity_Note> entity_notes) {
//                for (Entity_Note retrievedNote : entity_notes) {
//                    assertTrue(isEquel(note, retrievedNote));
//                }
//            }
//        });


    }



    private boolean isEquel(Entity_Note Original, Entity_Note Retrieved) {

        if (!Original.getContent().equals(Retrieved.getContent())) {
            Log.d(Tag,"Content Changed");
            return false;
        } else if (!Original.getTitle().equals(Retrieved.getTitle())) {
            Log.d(Tag,"Title Changed");
            return false;
        } else if (!Original.getImgUrl().equals(Retrieved.getImgUrl())) {
            Log.d(Tag,"ImgUrl Changed");
            return false;
        } else {
            Log.d(Tag,"Everything Matched");
            return true;
        }
    }
}
