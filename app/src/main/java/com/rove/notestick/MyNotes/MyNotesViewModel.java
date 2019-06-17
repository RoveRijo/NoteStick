package com.rove.notestick.MyNotes;

import android.app.Application;
import android.provider.ContactsContract;

import com.rove.datalayer.Data.Entity_Note;
import com.rove.domainlayer.NoteRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MyNotesViewModel extends AndroidViewModel {

    public static final String NEW_NOTE = "new_note";
    private LiveData<List<Entity_Note>> notesByDate;
    private NoteRepository noteRepository;

    public MyNotesViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        notesByDate = noteRepository.getAllNotesOrderBydate();
    }


    public LiveData<List<Entity_Note>> getNotesByDate() {
        return notesByDate;
    }
//    public  void saveNote(Entity_Note note){
//        noteRepository.saveNote(note);
//    }
}
