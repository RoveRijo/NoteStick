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
import com.rove.notestick.Util.NoteContentParsor;
import com.rove.notestick.Util.StringImageJsonViewModem;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CRUDnoteViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;
    public static final String THUMBNAIL_POSTFIX = "_thbnl";

    private JsonViewModem jsonViewModem;
    private LiveData<Entity_Note> currentNote;
    private ImageSaver imageSaver;
    private Application mApplication;
    //private boolean save_state = true;

    //private StringImageLayout contentLayout;

    private JsonViewModem.ViewContainer<TextImageLayout> viewContainer;


    public CRUDnoteViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        noteRepository = new NoteRepository(application);
        imageSaver = new ImageSaver(application.getApplicationContext());
        imageSaver.setDirectoryName(application.getApplicationContext().getString(R.string.Image_Directory_Name));
        imageSaver.setExternal(false);
        jsonViewModem = new StringImageJsonViewModem(application.getApplicationContext(), imageSaver);
    }

    public void saveCurrentNote(Entity_Note note) throws ExecutionException, InterruptedException {
        if (viewContainer == null)
            throw new RuntimeException("Set viewContainer first, before calling saveCurren" +
                    "tNote()");
        else {
            String contentJson = jsonViewModem.getJsonfromView();
            note.setContent(contentJson);
            NoteContentParsor contentParsor = new NoteContentParsor(contentJson);
            List<String> imageSrcs = contentParsor.getImageSrcs();
            if (imageSrcs.size() > 0) {
                String thumbnail = imageSrcs.get(imageSrcs.size() - 1);
                imageSaver.setFileName(thumbnail);
                try {
                    Bitmap bitmap = imageSaver.load();
                    Bitmap thumbnail_bitmap = Bitmap.createScaledBitmap(bitmap, 96, 96, false);
                    thumbnail += THUMBNAIL_POSTFIX;
                    saveImagetoPrivatefile(thumbnail, thumbnail_bitmap);
                    note.setImgUrl(thumbnail);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            } else {
                note.setImgUrl("");
            }
            if (noteRepository.isExisting(note)) {

                noteRepository.editNote(note);
            } else {
                noteRepository.saveNote(note);
            }
            currentNote = noteRepository.getNoteByIDLiveData(note.NoteId);
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
        ImageViewWithSrc imageview = new ImageViewWithSrc(mApplication.getApplicationContext(), imageSaver);
        imageview.setmImageURI(URI);
        imageview.setBackgroundResource(R.drawable.content_image_background);
        //imageview.setClickable(true);
        contentLayout.addView(imageview);
        EditText editText = new EditText(new ContextThemeWrapper(mApplication.getApplicationContext(),
                R.style.content_new_note_style));
        editText.setBackgroundColor(Color.TRANSPARENT);
//        editText.setHint(R.string.New_note_content_hint);
//        editText.setTextColor(Color.BLACK);
        contentLayout.addView(editText);
    }
    public void replaceImageOnScrollView(String URI,ImageViewWithSrc imageview) throws FileNotFoundException {
        imageview.setmImageURI(URI);
        imageview.setBackgroundColor(Color.BLUE);
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
    public void getContentOf(Entity_Note note) throws FileNotFoundException {
        String content = note.getContent();
        if(content!=null)
        jsonViewModem.populateViewfromJson(note.getContent());
    }

    public void deleteNoteWithId(Entity_Note note) {
        noteRepository.deleteNote(note);
    }

//    public void setSave_state(boolean save_state) {
//        this.save_state = save_state;
//    }
//
//    public boolean isSaved() {
//        return save_state;
//    }
}
