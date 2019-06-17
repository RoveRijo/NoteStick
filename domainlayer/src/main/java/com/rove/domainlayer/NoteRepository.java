package com.rove.domainlayer;


import android.app.Application;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.rove.datalayer.Data.*;
import com.rove.datalayer.Data.Entity_Note;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.rove.datalayer.Data.DatabaseInteractor;

import static com.rove.datalayer.Data.DatabaseInteractor.getDatabaseInstance;

//import static com.rove.datalayer.Data.DatabaseInteractor.getDatabaseInstance;

public class NoteRepository {
    private DatabaseInteractor databaseInteractor;

    public NoteRepository(Application application) {
        databaseInteractor = getDatabaseInstance(application);

    }


    public LiveData<List<Entity_Note>> getAllNotesOrderBydate() {
        return databaseInteractor.getDao().getAllNotesOrderByDate();
    }


    public void saveNote(Entity_Note note) {
        new Task_SaveNote(databaseInteractor).execute(note);

    }

    public void editNote(Entity_Note note) {
        new Task_EditNote(databaseInteractor).execute(note);
    }

    public boolean isExisting(Entity_Note note) {
        if (databaseInteractor.getDao().getNoteById(note.NoteId).getValue() != null)
            return true;
        else
            return false;
    }

    public LiveData<Entity_Note> getNoteByID(int noteId) {
        return databaseInteractor.getDao().getNoteById(noteId);
    }

    private static class Task_SaveNote extends AsyncTask<Entity_Note, Void, Void> {
        public Task_SaveNote(DatabaseInteractor databaseInteractor) {
            this.databaseInteractor = databaseInteractor;
        }

        private DatabaseInteractor databaseInteractor;

        @Override
        protected Void doInBackground(Entity_Note... entity_notes) {
            databaseInteractor.getDao().insertNote(entity_notes[0]);
            return null;
        }
    }

    private static class Task_EditNote extends AsyncTask<Entity_Note, Void, Void> {
        public Task_EditNote(DatabaseInteractor databaseInteractor) {
            this.databaseInteractor = databaseInteractor;
        }

        private DatabaseInteractor databaseInteractor;

        @Override
        protected Void doInBackground(Entity_Note... entity_notes) {
            databaseInteractor.getDao().updateNote(entity_notes[0]);
            return null;
        }
    }


}
