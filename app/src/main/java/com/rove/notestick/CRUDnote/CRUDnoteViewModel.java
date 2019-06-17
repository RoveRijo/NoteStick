package com.rove.notestick.CRUDnote;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.EditText;

import com.rove.datalayer.Data.Entity_Note;
import com.rove.domainlayer.NoteRepository;
import com.rove.notestick.CustomViews.ImageViewWithSrc;
import com.rove.notestick.CustomViews.TextImageLayout;
import com.rove.notestick.R;
import com.rove.notestick.Util.ImageSaver;
import com.rove.notestick.Util.JsonViewModem;
import com.rove.notestick.Util.StringImageJsonViewModem;

import java.io.FileNotFoundException;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CRUDnoteViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;

    private JsonViewModem jsonViewModem;
    private LiveData<Entity_Note> currentNote;
    private ImageSaver imageSaver;
    private Application mApplication;

    //private StringImageLayout contentLayout;

    private JsonViewModem.ViewContainer<TextImageLayout> viewContainer;



    public CRUDnoteViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        noteRepository = new NoteRepository(application);
        imageSaver = new ImageSaver(application.getApplicationContext());
        imageSaver.setDirectoryName(application.getApplicationContext().getString(R.string.Image_Directory_Name));
        imageSaver.setExternal(false);
        jsonViewModem = new StringImageJsonViewModem(application.getApplicationContext(),imageSaver);
    }

    public void saveCurrentNote(Entity_Note note) {
        if (viewContainer == null)
            throw new RuntimeException("Set viewContainer first, before calling saveCurren" +
                    "tNote()");
        else {
            if (noteRepository.isExisting(note)) {
                note.setContent(jsonViewModem.getJsonfromView());
                noteRepository.editNote(note);
            } else {
                note.setContent(jsonViewModem.getJsonfromView());
                noteRepository.saveNote(note);
                currentNote = noteRepository.getNoteByID(note.NoteId);
            }
        }
    }

    public LiveData<Entity_Note> getCurrentNote() {
        return currentNote;
    }

    public void saveImagetoPrivatefile(String URI, Bitmap image) throws FileNotFoundException {
        imageSaver.setFileName(URI);
        imageSaver.save(image);

    }


    public void loadImageOnScrollView(String URI) throws FileNotFoundException {
        TextImageLayout contentLayout = viewContainer.getViewContainer();
        ImageViewWithSrc imageview = new ImageViewWithSrc(mApplication.getApplicationContext(),imageSaver);
        imageview.setmImageURI(URI);
        imageview.setBackgroundColor(Color.BLUE);
        contentLayout.addView(imageview);
        EditText editText = new EditText(new ContextThemeWrapper(mApplication.getApplicationContext(),
                R.style.content_new_note_style));
        editText.setBackgroundColor(Color.TRANSPARENT);
//        editText.setHint(R.string.New_note_content_hint);
//        editText.setTextColor(Color.BLACK);
        contentLayout.addView(editText);
    }

    public Application getApplication() {
        return mApplication;
    }
    public void setCurrentNote(LiveData<Entity_Note> currentNote) {
        this.currentNote = currentNote;
    }
    public void setViewContainer(JsonViewModem.ViewContainer<TextImageLayout> viewContainer) {
        this.viewContainer = viewContainer;
        jsonViewModem.setViewContainer(viewContainer);
    }

}
