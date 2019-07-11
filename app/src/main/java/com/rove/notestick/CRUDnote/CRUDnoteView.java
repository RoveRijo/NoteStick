package com.rove.notestick.CRUDnote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.rove.datalayer.Data.Entity_Note;
import com.rove.notestick.CustomViews.ImageViewWithSrc;
import com.rove.notestick.CustomViews.TextImageLayout;
import com.rove.notestick.MyNotes.MyNotesViewModel;
import com.rove.notestick.R;
import com.rove.notestick.Util.DateParser;
import com.rove.notestick.Util.JsonViewModem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class CRUDnoteView extends AppCompatActivity {

    private CRUDnoteViewModel mViewModel;
    private TextView Date, Day, Month, Year, Time;
    private EditText Title;
    private TextImageLayout contentLayout;
    private Entity_Note note;
    private FloatingActionButton addStickerbtn, save;
    private CoordinatorLayout rootLayout;
    private static final int IMAGE_REQ_CODE = 1;
    private static final int IMAGE_REPLACE_REQ_CODE = 3;
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

        Date = findViewById(R.id.date);
        Day = findViewById(R.id.day);
        Year = findViewById(R.id.year);
        Time = findViewById(R.id.time);
        Month = findViewById(R.id.month);
        Title = findViewById(R.id.title);
        addStickerbtn = findViewById(R.id.addsticker);
        contentLayout = findViewById(R.id.cotentlayout);
        save = findViewById(R.id.save);
        context = this;

        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        mViewModel = ViewModelProviders.of(this).get(CRUDnoteViewModel.class);

        openMode = getIntent().getIntExtra(MODE, -1);
        switch (openMode) {
            case CREATE_NOTE_MODE:
                note = getIntent().getParcelableExtra(MyNotesViewModel.NEW_NOTE);
                break;
            case VIEW_NOTE_MODE:
                note = getIntent().getParcelableExtra(MyNotesViewModel.VIEW_NOTE);
                break;
            default:
                finish();
                break;
        }

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
        addStickerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchImageSelector(IMAGE_REQ_CODE);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeSave();
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
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void replaceImage(Uri imageURI,@NonNull ImageViewWithSrc imageview) {
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
                    mViewModel.replaceImageOnScrollView(imgname,imageview);
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
        else if (requestCode == IMAGE_REPLACE_REQ_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                if(selectedImageView!=null) {
                    replaceImage(data.getData(), selectedImageView);
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
                deletebtn = menu.findItem(R.id.delete_op_btn);
                savebtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        executeSave();
                        return true;
                    }
                });
                deletebtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        executeDelete();
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
                        executeSave();
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

    private void executeSave() {
        note.setTitle(Title.getText().toString());
        try {
            mViewModel.saveCurrentNote(note);
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
        for (int i = 0; i < contentLayout.getChildCount(); i++) {
            if (contentLayout.getChildAt(i) instanceof EditText) {
                EditText content = (EditText) contentLayout.getChildAt(i);
                content.setEnabled(true);
            } else if (contentLayout.getChildAt(i) instanceof ImageViewWithSrc) {
                registerForContextMenu(contentLayout.getChildAt(i));

            }
        }
    }

    private void makeContentLayoutReadOnly() {

        for (int i = 0; i < contentLayout.getChildCount(); i++) {
            if (contentLayout.getChildAt(i) instanceof EditText) {
                EditText content = (EditText) contentLayout.getChildAt(i);
                content.setEnabled(false);
            } else if (contentLayout.getChildAt(i) instanceof ImageViewWithSrc) {
                unregisterForContextMenu(contentLayout.getChildAt(i));
            }
        }
    }

    private void launchImageSelector(int requestCode){
        Intent imageselector = new Intent();
        imageselector.setAction(Intent.ACTION_GET_CONTENT);
        imageselector.setType("image/*");
        startActivityForResult(imageselector, requestCode);
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
        menu.findItem(R.id.replace_im).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(v instanceof ImageViewWithSrc) {
                    selectedImageView = (ImageViewWithSrc) v;
                }
                launchImageSelector(IMAGE_REPLACE_REQ_CODE);
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

    private class Dialogs{
        private AlertDialog dialog;
        private Context context;
        public Dialogs(Context context) {
            this.context = context;
        }
        public AlertDialog getDeleteConfirmDialog(){
            dialog = new AlertDialog.Builder(context).setTitle(R.string.delete_confirm_title).
                    setMessage(R.string.delete_confirm_description).setPositiveButton(R.string.btn_yes,
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    executeDelete();

                }
            }).setNegativeButton(R.string.btn_no,null
            ).create();
            return dialog;
        }
    }

    ////////////////////////////////////////
    //Dialog delivery Class               //
    ///////////////////////////////////////

}
