package com.rove.notestick.CRUDnote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rove.datalayer.Data.Entity_Note;
import com.rove.notestick.CustomViews.TextImageLayout;
import com.rove.notestick.MyNotes.MyNotesViewModel;
import com.rove.notestick.R;
import com.rove.notestick.Util.DateParser;
import com.rove.notestick.Util.JsonViewModem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class CRUDnoteView extends AppCompatActivity {

    private CRUDnoteViewModel mViewModel;
    private TextView Date, Day, Month, Year, Time;
    private EditText Title;
    private TextImageLayout contentLayout;
    private Entity_Note note;
    private FloatingActionButton addStickerbtn,save;
    private static final int IMAGE_REQ_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Date = findViewById(R.id.date);
        Day = findViewById(R.id.day);
        Year = findViewById(R.id.year);
        Time = findViewById(R.id.time);
        Month = findViewById(R.id.month);
        Title = findViewById(R.id.title);
        addStickerbtn = findViewById(R.id.addsticker);
        contentLayout = findViewById(R.id.cotentlayout);
        save = findViewById(R.id.save);

        mViewModel = ViewModelProviders.of(this).get(CRUDnoteViewModel.class);
        note = getIntent().getParcelableExtra(MyNotesViewModel.NEW_NOTE);
        mViewModel.setCurrentNote(new MutableLiveData<>(note));
        mViewModel.getCurrentNote().observe(this, new Observer<Entity_Note>() {
            @Override
            public void onChanged(Entity_Note note) {
                DateParser dateParser = new DateParser(note.getDate());
                Date.setText(dateParser.getDate());
                Day.setText(dateParser.getDay());
                Month.setText(dateParser.getMonth());
                Year.setText(dateParser.getYear());
                Time.setText(dateParser.getTime());
                Title.setText(note.getTitle());


            }
        });
        addStickerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageselector = new Intent();
                imageselector.setAction(Intent.ACTION_GET_CONTENT);
                imageselector.setType("image/*");
                startActivityForResult(imageselector, IMAGE_REQ_CODE);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note.setTitle(Title.getText().toString());
                mViewModel.saveCurrentNote(note);
            }
        });

        mViewModel.setViewContainer(new JsonViewModem.ViewContainer<TextImageLayout>() {
            @Override
            public TextImageLayout getViewContainer() {
                return contentLayout;
            }
        });

    }

    private void cleanUpCurrentNote() {
        // check if a useless note present .(Empty note)
    }


    @Override
    protected void onStop() {
        //cleanUpCurrentNote();
        //mViewModel.saveCurrentNote(note);
        super.onStop();

    }

    private void loadImage(Uri imageURI) {
        Bitmap image = null;
        if (imageURI != null) {
            try {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (image != null) {
                Random random = new Random();
                int number = random.nextInt(10000);
                java.util.Date date = new Date();
                String imgname = String.valueOf(date) + String.valueOf(number);
                try {
                    mViewModel.saveImagetoPrivatefile(imgname, image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    mViewModel.loadImageOnScrollView(imgname);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQ_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                loadImage(data.getData());
            }
        }
    }

}
