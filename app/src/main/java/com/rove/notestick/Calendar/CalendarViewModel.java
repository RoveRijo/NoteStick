package com.rove.notestick.Calendar;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.rove.datalayer.Data.Entity_Note;
import com.rove.domainlayer.Repository.NoteRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class CalendarViewModel extends AndroidViewModel {
    private NoteRepository noteRepository;
    private Application mApplication;
    private LiveData<List<Entity_Note>> notesOnDate;
    private MutableLiveData<Calendar> withDate;

    public CalendarViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        noteRepository = new NoteRepository(application);
        withDate = new MutableLiveData<>();
        notesOnDate = Transformations.switchMap(withDate,calendar->noteRepository.getAllNoteswithDate(calendar.getTime()));
    }

    LiveData<List<Date>> getRelavantDates(){
        return noteRepository.getAllDateswithNotes();
    }

    public Entity_Note getNotWithId(int noteId) throws ExecutionException, InterruptedException {
        Entity_Note note = noteRepository.getNoteByID(noteId);
        return note;
    }

    public void showNotesOnDate(Calendar calendar) {
            withDate.setValue(calendar);

    }

    public LiveData<List<Entity_Note>> getNotesOnDate() {
        return notesOnDate;
    }
}
