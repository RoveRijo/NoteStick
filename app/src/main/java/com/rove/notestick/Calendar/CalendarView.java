package com.rove.notestick.Calendar;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.rove.datalayer.Data.Entity_Note;
import com.rove.domainlayer.Util.DateManipulator;
import com.rove.notestick.CRUDnote.CRUDnoteView;
import com.rove.notestick.MyNotes.MyNotesViewModel;
import com.rove.notestick.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CalendarView extends Fragment {

    private CalendarViewModel mViewModel;
    private FragmentManager fragmentManager;
    private com.applandeo.materialcalendarview.CalendarView appCalender;
    private List<Date> relevantDates;
    public static CalendarView newInstance() {
        return new CalendarView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);

        mViewModel.getRelavantDates().observe(this, new Observer<List<Date>>() {
            @Override
            public void onChanged(List<Date> dates) {
                markInCalendar(dates);
                relevantDates = dates;
            }
        });
        appCalender = getActivity().findViewById(R.id.calendar);
        try {
            appCalender.setDate(Calendar.getInstance());
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }
        appCalender.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                try {
                    if (isFreeSlotDate(eventDay.getCalendar().getTime())) {
                        showNoteEditorOnCreateModeWithDate(eventDay.getCalendar().getTime());
                    } else {
                        mViewModel.showNotesOnDate(eventDay.getCalendar());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        fragmentManager = getFragmentManager();

        mViewModel.getNotesOnDate().observe(this, new Observer<List<Entity_Note>>() {
            @Override
            public void onChanged(List<Entity_Note> entity_notes) {
                if (entity_notes.size() > 1) {
                    NoteSelectionFrag noteSelectionFrag = new NoteSelectionFrag(entity_notes);
                    noteSelectionFrag.show(fragmentManager, getString(R.string.ChoseNoteDialogTitle));
                } else {
                    try {
                        if (entity_notes.size() == 1)
                            showNoteEditorOnViewNoteMode(entity_notes.get(0).NoteId);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void markInCalendar(List<Date> dates) {
        List<EventDay> events = new ArrayList<>();
        int markerIcon = R.drawable.calendar_mark_icon;
        for (Date date : dates) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            events.add(new EventDay(calendar, markerIcon));
        }
        appCalender.setEvents(events);
    }

    private void showNoteEditorOnCreateModeWithDate(Date date) {
        Entity_Note note = new Entity_Note();
        note.setDate(date);
        Intent newnote = new Intent(getContext(), CRUDnoteView.class);
        newnote.putExtra(CRUDnoteView.MODE, CRUDnoteView.CREATE_NOTE_MODE);
        newnote.putExtra(MyNotesViewModel.NEW_NOTE, note);
        startActivity(newnote);

    }

    private void showNoteEditorOnViewNoteMode(int noteId) throws ExecutionException, InterruptedException {
        Entity_Note note = mViewModel.getNotWithId(noteId);
        Intent newnote = new Intent(getContext(), CRUDnoteView.class);
        newnote.putExtra(CRUDnoteView.MODE, CRUDnoteView.VIEW_NOTE_MODE);
        newnote.putExtra(MyNotesViewModel.VIEW_NOTE, note);
        startActivity(newnote);

    }

    private boolean isFreeSlotDate(Date date) throws ParseException {

        for (Date rDate : relevantDates){
            if(DateManipulator.getTrimmedDate(rDate).equals(DateManipulator.getTrimmedDate(date))){
                return false;
            }
        }
       return true;
    }


}
