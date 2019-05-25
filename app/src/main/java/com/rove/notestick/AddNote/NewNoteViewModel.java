package com.rove.notestick.AddNote;

import android.app.Application;

import com.rove.datalayer.Data.Entity_Note;
import com.rove.domainlayer.NoteRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class NewNoteViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;


    private LiveData<Entity_Note> currentNote;

    public NewNoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
    }

    public void saveCurrentNote(Entity_Note note){
        if(noteRepository.isExisting(note)){
            noteRepository.editNote(note);
        }
        else
        {
            noteRepository.saveNote(note);
            currentNote = noteRepository.getNoteByID(note.NoteId);
        }
    }
    public LiveData<Entity_Note> getCurrentNote() {
        return currentNote;
    }


}
