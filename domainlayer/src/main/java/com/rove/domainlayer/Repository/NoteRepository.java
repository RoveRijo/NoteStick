package com.rove.domainlayer.Repository;


import android.app.Application;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.rove.datalayer.Data.*;
import com.rove.datalayer.Data.Entity_Note;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.rove.datalayer.Data.DatabaseInteractor;
import com.rove.domainlayer.Util.DateManipulator;

import static com.rove.datalayer.Data.DatabaseInteractor.getDatabaseInstance;

//import static com.rove.datalayer.Data.DatabaseInteractor.getDatabaseInstance;

public class NoteRepository {
    private DatabaseInteractor databaseInteractor;
    private static MutableLiveData<Long> inserted_noteID = new MutableLiveData<>();
    private MediatorLiveData<List<Entity_Note>> notesByDateAndQuery = new MediatorLiveData<>();


    public NoteRepository(Application application) {
        databaseInteractor = getDatabaseInstance(application);

    }

    public MediatorLiveData<List<Entity_Note>> getNotesByDateAndQuery() {
        return notesByDateAndQuery;
    }

    public LiveData<List<Entity_Note>> getAllNotesOrderBydate() {
        return databaseInteractor.getDao().getAllNotesOrderByDate();
    }
//    public LiveData<List<Entity_Note>> getAllNotesOrderBydate_andQuery(String query) {
//
//        if(query.equals("")) {
//             mergedListLiveData.addSource(databaseInteractor.getDao().getAllNotesOrderByDate(),
//                     NotesBydate-> mergedListLiveData.setValue(NotesBydate));
//        }
//        else
//        {
//            mergedListLiveData.addSource(databaseInteractor.getDao().getAllNotesOrderByQuery("%"+query+"%"),
//                    NotesByQuery->mergedListLiveData.setValue(NotesByQuery));
//        }
//        return mergedListLiveData;
//    }

    public void getAllNotesOrderBydate_andQuery(String query){
        notesByDateAndQuery.addSource(databaseInteractor.getDao().getAllNotesOrderByQuery("%"+query+"%"),
                NotesBydate-> notesByDateAndQuery.setValue(NotesBydate));
    }
    public void getAllNotesOrderBydate_andQuery(){
        notesByDateAndQuery.addSource(databaseInteractor.getDao().getAllNotesOrderByDate(),
                     NotesBydate-> notesByDateAndQuery.setValue(NotesBydate));
    }

    public MutableLiveData<Long> saveNote(Entity_Note note) {
        new Task_SaveNote(databaseInteractor).execute(note);
        return inserted_noteID;

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
    public LiveData<List<Date>> getAllDateswithNotes(){
        return databaseInteractor.getDao().getAllDatesWithNotes();
    }
    public LiveData<List<Entity_Note>> getAllNoteswithDate(Date date){
        return databaseInteractor.getDao().getNotesFromPeriod(date, DateManipulator.getNextDay(date));
    }





    private static class Task_SaveNote extends AsyncTask<Entity_Note, Void, Void> {
        public Task_SaveNote(DatabaseInteractor databaseInteractor) {
            this.databaseInteractor = databaseInteractor;
        }

        private DatabaseInteractor databaseInteractor;

        @Override
        protected Void doInBackground(Entity_Note... entity_notes) {
            long noteId = databaseInteractor.getDao().insertNote(entity_notes[0]);
            inserted_noteID.postValue(noteId);
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
