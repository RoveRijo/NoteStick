package com.rove.datalayer.Data;

import android.database.Cursor;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface mDao {
    @Insert
    long insertNote(Entity_Note note);

    @Update
    void updateNote(Entity_Note notes);

    @Delete
    void deleteNote(Entity_Note note);

    @Query("SELECT * fROM Entity_Note")
    LiveData<List<Entity_Note>> getAllNotes();

    @Query("SELECT * fROM Entity_Note ORDER BY Date DESC")
    LiveData<List<Entity_Note>> getAllNotesOrderByDate();

    @Query("SELECT * fROM Entity_Note WHERE Title LIKE :query ")
    LiveData<List<Entity_Note>> getAllNotesOrderByQuery(String query);

//    @Query("SELECT * fROM Entity_Note")
//    Cursor getAllNotesAsCursor();


    @Query("SELECT * fROM Entity_Note WHERE NoteId = :id")
    Entity_Note getNoteById(int id);

    @Query("SELECT * fROM Entity_Note WHERE NoteId = :id")
    LiveData<Entity_Note> getNoteByIDLiveData(int id);

    @Query("SELECT * fROM Entity_Note WHERE Date >= :fromdate AND Date < :todate ")
    LiveData<List<Entity_Note>> getNotesFromPeriod(Date fromdate, Date todate);

    @Query("SELECT * fROM Entity_Note WHERE instr(Title,:querystr)")
    LiveData<List<Entity_Note>> getNotesContainsTitle(String querystr);

    @Query("SELECT * fROM Entity_Note WHERE instr(Content,:querystr)")
    LiveData<List<Entity_Note>> getNotesContainsContent(String querystr);

    @Query("SELECT DISTINCT Date FROM Entity_Note")
    LiveData<List<Date>> getAllDatesWithNotes();

    @Query("SELECT NoteId FROM Entity_Note WHERE Date = :date ")
    LiveData<List<Entity_Note>> getAllNotesWithDate(Date date);


}
