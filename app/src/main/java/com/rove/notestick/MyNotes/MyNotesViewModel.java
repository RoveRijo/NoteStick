package com.rove.notestick.MyNotes;

import android.app.Application;
import android.graphics.Bitmap;
import android.provider.ContactsContract;

import com.rove.datalayer.Data.Entity_Note;
import com.rove.domainlayer.NoteRepository;
import com.rove.notestick.R;
import com.rove.notestick.Util.ImageSaver;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MyNotesViewModel extends AndroidViewModel {

    public static final String NEW_NOTE = "new_note";
    public static final String VIEW_NOTE = "view_note";
    private LiveData<List<Entity_Note>> notesByDate;
    private NoteRepository noteRepository;
    private ImageSaver imageSaver;

    public MyNotesViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        notesByDate = noteRepository.getAllNotesOrderBydate();
        imageSaver = new ImageSaver(application.getApplicationContext());
        imageSaver.setDirectoryName(application.getApplicationContext().getString(R.string.Image_Directory_Name));
        imageSaver.setExternal(false);
    }

    public Bitmap getThumbnail(String URI){
        imageSaver.setFileName(URI);
        try {
            return imageSaver.load();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public LiveData<List<Entity_Note>> getNotesByDate() {
        return notesByDate;
    }

    public Entity_Note getNotWithId(int noteId) throws ExecutionException, InterruptedException {
        Entity_Note note = noteRepository.getNoteByID(noteId);
        return note;
    }
//    public  void showNoteEditorOnCreateMode(Entity_Note note){
//        noteRepository.showNoteEditorOnCreateMode(note);
//    }
}
