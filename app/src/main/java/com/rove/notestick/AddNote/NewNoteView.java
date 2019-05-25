package com.rove.notestick.AddNote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.rove.datalayer.Data.Entity_Note;
import com.rove.notestick.MyNotes.MyNotesViewModel;
import com.rove.notestick.R;

public class NewNoteView extends AppCompatActivity {

    NewNoteViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        mViewModel = ViewModelProviders.of(this).get(NewNoteViewModel.class);
        Entity_Note note = getIntent().getParcelableExtra(MyNotesViewModel.NEW_NOTE);
        mViewModel.saveCurrentNote(note);
        mViewModel.getCurrentNote().observe(this, new Observer<Entity_Note>() {
            @Override
            public void onChanged(Entity_Note note) {

            }
        });

    }

    private void cleanUpCurrentNote(){
        // check if a useless note present
    }

    @Override
    protected void onDestroy() {
        cleanUpCurrentNote();
        super.onDestroy();
    }
}
