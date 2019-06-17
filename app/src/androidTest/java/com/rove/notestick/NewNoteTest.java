package com.rove.notestick;


import android.content.Intent;

import com.rove.datalayer.Data.Entity_Note;
import com.rove.notestick.CRUDnote.CRUDnoteView;
import com.rove.notestick.MyNotes.MyNotesViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


//@RunWith(AndroidJUnit4.class)
public class NewNoteTest {
    private String Title;
    private String content;

    @Rule
    public ActivityTestRule<CRUDnoteView> activityTestRule = new ActivityTestRule<>(CRUDnoteView.class,
            true
    ,false);

    @Before
    public void init() {
        Title = "Title";
        content = "content";
        Intent intent = new Intent();
        Entity_Note note = new Entity_Note();
        note.setDate(new Date());
        intent.putExtra(MyNotesViewModel.NEW_NOTE,note);
        activityTestRule.launchActivity(intent);
    }

    @Test
    public void NotesavedSuccessfully(){
        onView(withId(R.id.title)).perform(typeText(Title));
        onView(withId(R.id.save)).perform(click());
        onView(withHint(R.string.New_note_content_hint)).perform(typeText(content));
        onView(withId(R.id.addsticker)).perform(click());

    }

}

