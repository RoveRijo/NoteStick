package com.rove.datalayer;

import android.content.Context;
import android.util.Log;

import com.rove.datalayer.Data.DatabaseInteractor;
import com.rove.datalayer.Data.Entity_Note;
import com.rove.datalayer.Data.mDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class testDao {
    private mDao Dao;
    private DatabaseInteractor db;
    private Date date = new Date();
    private String Title = "Title";
    private String TitleUpdate = "Titleupdated";
    private String Content = "content";
    private String ImgUrl = "imgurl";
    private final String Tag = "testDao";

    public testDao() {

    }

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, DatabaseInteractor.class).build();
        Dao = db.getDao();
        Log.d(Tag, "db created");
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeNoteAndReadNote() throws Exception {
        Entity_Note note = new Entity_Note();
        note.setDate(date);
        note.setTitle(Title);
        note.setContent(Content);
        note.setImgUrl(ImgUrl);
        Dao.insertNote(note);
        Log.d(Tag, "One note inserted");

        List<Entity_Note> retrievedNotes = (List<Entity_Note>) Dao.getNotesContainsTitle(Title);
        for (Entity_Note retrievedNote : retrievedNotes) {
            assertTrue(isEquel(note, retrievedNote));
        }


    }

    @Test
    public void updateNoteTest() throws Exception {

        Entity_Note note = new Entity_Note();
        note.setDate(date);
        note.setTitle(Title);
        note.setContent(Content);
        note.setImgUrl(ImgUrl);
        Dao.insertNote(note);
        Log.d(Tag, "One note inserted");

        List<Entity_Note> retrievedNotes = (List<Entity_Note>) Dao.getAllNotes();
        List<Entity_Note> retrievedNotes1 = (List<Entity_Note>) Dao.getAllNotes();
        note = retrievedNotes.get(0);
        Entity_Note notePrev = retrievedNotes1.get(0);
        note.setTitle(TitleUpdate);
        int id = note.NoteId;
        Dao.updateNote(note);

        Entity_Note retrievedNote = Dao.getNoteById(id).getValue();
        assertTrue(!isEquel(notePrev, retrievedNote));


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
