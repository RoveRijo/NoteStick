package com.rove.domainlayer;


import android.app.Application;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.rove.datalayer.Data.*;
import com.rove.datalayer.Data.Entity_Note;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

    public boolean isExisting(Entity_Note note) throws ExecutionException, InterruptedException {
        if (getNoteByID(note.getNoteId())!=null)
            return true;
        else
            return false;
    }

    public Entity_Note getNoteByID(int noteId) throws ExecutionException, InterruptedException {
        return new Task_getNotById(databaseInteractor).execute(noteId).get();
    }

    public LiveData<Entity_Note> getNoteByIDLiveData(int noteId){
        return databaseInteractor.getDao().getNoteByIDLiveData(noteId);
    }
    public void deleteNote(Entity_Note note){
      new Task_deleteNote(databaseInteractor).execute(note);
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
    private static class Task_getNotById extends AsyncTask<Integer,Void,Entity_Note>{

        private DatabaseInteractor databaseInteractor;
        public Task_getNotById(DatabaseInteractor databaseInteractor){
            this.databaseInteractor = databaseInteractor;
        }
        @Override
        protected Entity_Note doInBackground(Integer... id) {
            return databaseInteractor.getDao().getNoteById(id[0]);
        }


    }
    private static class Task_deleteNote extends AsyncTask<Entity_Note,Void,Void>{
        private DatabaseInteractor databaseInteractor;

        public Task_deleteNote(DatabaseInteractor databaseInteractor) {
            this.databaseInteractor = databaseInteractor;
        }

        @Override
        protected Void doInBackground(Entity_Note... notes) {
            databaseInteractor.getDao().deleteNote(notes[0]);
            return null;
        }
    }


}
