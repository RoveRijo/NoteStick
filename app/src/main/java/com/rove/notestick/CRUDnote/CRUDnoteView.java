package com.rove.notestick.CRUDnote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.rove.datalayer.Data.Entity_Note;
import com.rove.notestick.CustomViews.ImageViewWithSrc;
import com.rove.notestick.CustomViews.TextImageLayout;
import com.rove.notestick.MyNotes.MyNotesViewModel;
import com.rove.notestick.R;
import com.rove.notestick.Util.DateParser;
import com.rove.notestick.Util.JsonViewModem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class CRUDnoteView extends AppCompatActivity {

    private CRUDnoteViewModel mViewModel;
    private TextView Date, Day, Month, Year, Time;
    private LifecycleOwner lifecycleOwner;
    private EditText Title;
    private TextImageLayout contentLayout;
    private boolean editMode = false;
    private Entity_Note note;
    private SpeedDialView addStickermenu;
    private ConstraintLayout datelayout;
    private CoordinatorLayout rootLayout;
    private static final int IMAGE_REQ_CODE = 1;
    private static final int IMAGE_REPLACE_REQ_CODE = 3;
    private static final int REQUEST_IMAGE_CAPTURE = 4;
    private static final int REQUEST_IMAGE_CAPTURE_REPLACE = 5;
    public static final int CREATE_NOTE_MODE = 1;
    public static final int VIEW_NOTE_MODE = 2;
    public static final String MODE = "mode";
    private int openMode;
    private Context context;
    private ImageViewWithSrc selectedImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rootLayout = findViewById(R.id.rootlayout);
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);

        modifyCollapsingToolbarTitle(collapsingToolbarLayout, appBarLayout, (String) getTitle());

        Date = findViewById(R.id.date);
        Day = findViewById(R.id.day);
        Year = findViewById(R.id.year);
        Time = findViewById(R.id.time);
        Month = findViewById(R.id.month);
        Title = findViewById(R.id.title);
        datelayout = findViewById(R.id.noteheader);
        addStickermenu = findViewById(R.id.addsticker);

        contentLayout = findViewById(R.id.cotentlayout);
        context = this;

        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Dialogs(context).getDatepicker().show();
            }
        });
        Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Dialogs(context).getDatepicker().show();
            }
        });
        Year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Dialogs(context).getDatepicker().show();
            }
        });
        Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Dialogs(context).getTimepicker().show();
            }
        });
        Month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Dialogs(context).getDatepicker().show();
            }
        });
        datelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Dialogs(context).getDatepicker().show();
            }
        });


        mViewModel = ViewModelProviders.of(this).get(CRUDnoteViewModel.class);

        openMode = getIntent().getIntExtra(MODE, -1);
        switch (openMode) {
            case CREATE_NOTE_MODE:
                note = getIntent().getParcelableExtra(MyNotesViewModel.NEW_NOTE);
                makeContentLayoutEditable();

                break;
            case VIEW_NOTE_MODE:
                note = getIntent().getParcelableExtra(MyNotesViewModel.VIEW_NOTE);
                makeContentLayoutReadOnly();
                break;
            default:
                finish();
                break;
        }
        lifecycleOwner = this;

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
                try {
                    mViewModel.getContentOf(note);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }
        });

        addStickermenu.inflate(R.menu.crud_menu_sticker_options);
        addStickermenu.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()) {
                    case R.id.from_camera:
                        getImageFromCamera(REQUEST_IMAGE_CAPTURE);
                        return false;
                    case R.id.from_gallery:
                        launchImageSelector(IMAGE_REQ_CODE);
                        return false;
                    default:
                        return true;
                }

            }
        });

        mViewModel.setViewContainer(new JsonViewModem.ViewContainer<TextImageLayout>() {
            @Override
            public TextImageLayout getViewContainer() {
                return contentLayout;
            }
        });


    }

    @Override
    protected void onStop() {

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
                    registerImagesforContextMenu();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadImagefromCamera(Bitmap image) {
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
            registerImagesforContextMenu();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void replaceImage(Uri imageURI, @NonNull ImageViewWithSrc imageview) {
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
                    mViewModel.replaceImageOnScrollView(imgname, imageview);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void replaceImageWithCamera(Bitmap image, @NonNull ImageViewWithSrc imageview) {
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
            mViewModel.replaceImageOnScrollView(imgname, imageview);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQ_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                loadImage(data.getData());
            }
        } else if (requestCode == IMAGE_REPLACE_REQ_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                if (selectedImageView != null) {
                    replaceImage(data.getData(), selectedImageView);
                }
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle extras = data.getExtras();
                Bitmap image = (Bitmap) extras.get("data");
                loadImagefromCamera(image);
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE_REPLACE && resultCode == RESULT_OK) {
            if (data != null) {
                if (selectedImageView != null) {
                    Bundle extras = data.getExtras();
                    Bitmap image = (Bitmap) extras.get("data");
                    replaceImageWithCamera(image, selectedImageView);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        final MenuItem savebtn, editbtn, deletebtn;
        switch (openMode) {
            case CREATE_NOTE_MODE:
                menuInflater.inflate(R.menu.crud_menu_create_note, menu);
                savebtn = menu.findItem(R.id.save_op_btn);
                savebtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        try {
                            executeSave();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                });

                break;
            case VIEW_NOTE_MODE:
                menuInflater.inflate(R.menu.crud_menu_view_note, menu);
                savebtn = menu.findItem(R.id.save_op_btn);
                deletebtn = menu.findItem(R.id.delete_op_btn);
                editbtn = menu.findItem(R.id.edit_op_btn);
                savebtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        try {
                            executeSave();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        editbtn.setVisible(true);
                        makeContentLayoutReadOnly();
                        return true;
                    }
                });
                deletebtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        new Dialogs(context).getDeleteConfirmDialog().show();
                        return true;
                    }
                });
                editbtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        executeEdit();
                        editbtn.setVisible(false);
                        return true;
                    }
                });

                break;
        }
        return true;
    }

    private void executeSave() throws ParseException {
        note.setTitle(Title.getText().toString());
        Date date = new DateParser.StringToDateTimeBuilder(Day.getText().toString()+", "+
                Month.getText().toString()+" "+Date.getText().toString()
                +" "+Year.getText().toString()+" "+Time.getText().toString())
                .build();
        if(date!=null){
            note.setDate(date);
        }
        try {
            mViewModel.saveCurrentNote(note).observe(lifecycleOwner, new Observer<Long>() {
                @Override
                public void onChanged(Long noteid) {
                    note.NoteId = (int) (long) noteid;
                }
            });
            Snackbar.make(rootLayout, R.string.Save_snackbar_msg, Snackbar.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void executeDelete() {
        mViewModel.deleteNoteWithId(note);
        finish();
    }

    private void executeEdit() {
        makeContentLayoutEditable();
    }

    private void makeContentLayoutEditable() {
        Title.setEnabled(true);
        editMode = true;
        addStickermenu.setVisibility(View.VISIBLE);
        datelayout.setBackgroundResource(R.drawable.date_selection_background);
        Date.setBackgroundResource(R.drawable.date_selection_background);
        Day.setBackgroundResource(R.drawable.date_selection_background);
        Year.setBackgroundResource(R.drawable.date_selection_background);
        Month.setBackgroundResource(R.drawable.date_selection_background);
        Time.setBackgroundResource(R.drawable.date_selection_background);
        Date.setClickable(true);
        Day.setClickable(true);
        Month.setClickable(true);
        Year.setClickable(true);
        Time.setClickable(true);
        datelayout.setClickable(true);

        for (int i = 0; i < contentLayout.getChildCount(); i++) {
            if (contentLayout.getChildAt(i) instanceof EditText) {
                EditText content = (EditText) contentLayout.getChildAt(i);
                content.setHint(R.string.New_note_content_hint);
                content.setEnabled(true);
            } else if (contentLayout.getChildAt(i) instanceof ImageViewWithSrc) {
                registerForContextMenu(contentLayout.getChildAt(i));
                ImageViewWithSrc imageViewWithSrc = (ImageViewWithSrc) contentLayout.getChildAt(i);
                imageViewWithSrc.setEnabled(true);
                imageViewWithSrc.setClickable(true);

            }
        }
    }

    private void makeContentLayoutReadOnly() {

        Title.setEnabled(false);
        editMode = false;
        addStickermenu.setVisibility(View.GONE);
        Date.setClickable(false);
        Day.setClickable(false);
        Month.setClickable(false);
        Year.setClickable(false);
        Time.setClickable(false);
        datelayout.setClickable(false);
        for (int i = 0; i < contentLayout.getChildCount(); i++) {
            if (contentLayout.getChildAt(i) instanceof EditText) {
                EditText content = (EditText) contentLayout.getChildAt(i);
                content.setHint("");
                content.setEnabled(false);
            } else if (contentLayout.getChildAt(i) instanceof ImageViewWithSrc) {
                unregisterForContextMenu(contentLayout.getChildAt(i));
                ImageViewWithSrc imageViewWithSrc = (ImageViewWithSrc) contentLayout.getChildAt(i);
                imageViewWithSrc.setClickable(false);
                imageViewWithSrc.setEnabled(false);
            }
        }
    }

    private void registerImagesforContextMenu() {
        for (int i = 0; i < contentLayout.getChildCount(); i++) {
            if (contentLayout.getChildAt(i) instanceof ImageViewWithSrc) {
                registerForContextMenu(contentLayout.getChildAt(i));
                ImageViewWithSrc imageViewWithSrc = (ImageViewWithSrc) contentLayout.getChildAt(i);
                imageViewWithSrc.setEnabled(true);
                imageViewWithSrc.setClickable(true);

            }
        }
    }

    private void launchImageSelector(int requestCode) {
        Intent imageselector = new Intent();
        imageselector.setAction(Intent.ACTION_GET_CONTENT);
        imageselector.setType("image/*");
        startActivityForResult(imageselector, requestCode);
    }

    private void getImageFromCamera(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, requestCode);
        }
    }

    /*
        ////////////////////////////////////
        Floating Context Menu for Image Edit
        ////////////////////////////////////
        */


    @Override
    public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.image_actions_menu, menu);
        menu.findItem(R.id.delete_im).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                contentLayout.removeView(v);
                return true;
            }
        });
        menu.findItem(R.id.replace_im_gallery).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (v instanceof ImageViewWithSrc) {
                    selectedImageView = (ImageViewWithSrc) v;
                }
                launchImageSelector(IMAGE_REPLACE_REQ_CODE);
                return true;
            }
        });
        menu.findItem(R.id.replace_im_camera).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (v instanceof ImageViewWithSrc) {
                    selectedImageView = (ImageViewWithSrc) v;
                }
                getImageFromCamera(REQUEST_IMAGE_CAPTURE_REPLACE);
                return true;
            }
        });

    }

      /*
        ////////////////////////////////////
        Floating Context Menu for Image Edit
        ////////////////////////////////////
        */

    ////////////////////////////////////////
    //Dialog delivery Class               //
    ///////////////////////////////////////

    private class Dialogs {
        private AlertDialog dialog;
        private DatePickerDialog datePickerDialog;
        private TimePickerDialog timePickerDialog;
        private Context context;

        public Dialogs(Context context) {
            this.context = context;
        }

        public AlertDialog getDeleteConfirmDialog() {
            dialog = new AlertDialog.Builder(context).setTitle(R.string.delete_confirm_title).
                    setMessage(R.string.delete_confirm_description).setPositiveButton(R.string.btn_yes,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            executeDelete();

                        }
                    }).setNegativeButton(R.string.btn_no, null
            ).create();
            return dialog;
        }

        public AlertDialog getSaveConfirmDialog() {
            dialog = new AlertDialog.Builder(context).setTitle(R.string.save_confirm_title).
                    setMessage(R.string.save_confirm_description).setPositiveButton(R.string.btn_yes,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                executeSave();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            finish();

                        }
                    }).setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }
            ).create();
            return dialog;
        }

        public DatePickerDialog getDatepicker() {
            final Calendar today = Calendar.getInstance();
            datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    today.set(i, i1, i2);
                    Date date = today.getTime();
                    DateParser dateParser = new DateParser(date);
                    Date.setText(dateParser.getDate());
                    Day.setText(dateParser.getDay());
                    Year.setText(dateParser.getYear());
                    Month.setText(dateParser.getMonth());
                }
            }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
            return datePickerDialog;
        }

        public TimePickerDialog getTimepicker() {
            final Calendar today = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    today.set(0, 0, 0, i, i1);
                    Date date = today.getTime();
                    DateParser dateParser = new DateParser(date);
                    Time.setText(dateParser.getTime());
                }
            }, today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE), false);

            return timePickerDialog;
        }
    }

    ////////////////////////////////////////
    //Dialog delivery Class               //
    ///////////////////////////////////////

    private void modifyCollapsingToolbarTitle(final CollapsingToolbarLayout collapsingToolbarLayout
            , AppBarLayout appBarLayout, final String title) {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(title);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (editMode)
            new Dialogs(context).getSaveConfirmDialog().show();
        else {
            super.onBackPressed();
        }

    }
}
