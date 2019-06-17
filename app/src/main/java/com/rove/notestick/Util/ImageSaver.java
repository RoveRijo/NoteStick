package com.rove.notestick.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;

public class ImageSaver {
    private static int INITILIZER_BEGIN=0,INITIALIZER_FINISHED_SAVING=3,INITIALIZER_FINISHED_LOADING=4;
    private String directoryName = "images";
    private String fileName = "image.png";
    private Context context;
    private boolean external;
    private int initializestat = INITILIZER_BEGIN;

    public ImageSaver(Context context) {
        this.context = context;
    }

    public ImageSaver setFileName(String fileName) {
        this.fileName = fileName;
        initializestat++;
        return this;
    }

    public ImageSaver setExternal(boolean external) {
        this.external = external;
        initializestat++;
        return this;
    }

    public ImageSaver setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
        initializestat++;
        return this;
    }

    public void save(Bitmap bitmapImage) throws FileNotFoundException {
        if(initializestat>=INITIALIZER_FINISHED_SAVING) {
            FileOutputStream fileOutputStream = null;
                fileOutputStream = new FileOutputStream(createFile());
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        }
        else
        {
            throw new RuntimeException(context.toString()+" ImageSaver Not initialized Correctly");
        }
    }

    @NonNull
    private File createFile() {
        File directory;
        if(external){
            directory = getAlbumStorageDir(directoryName);
        }
        else {
            directory = context.getDir(directoryName, Context.MODE_PRIVATE);
        }
        if(!directory.exists() && !directory.mkdirs()){
            Log.e("ImageSaver","Error creating directory " + directory);
            throw new RuntimeException("Error creating directory " + directory);
        }

        return new File(directory, fileName);
    }

    private File getAlbumStorageDir(String albumName) {
        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public Bitmap load() throws FileNotFoundException{
        if(initializestat>=INITIALIZER_FINISHED_LOADING){
        FileInputStream inputStream = null;
            inputStream = new FileInputStream(createFile());
            return BitmapFactory.decodeStream(inputStream);

        }
        else{
            throw new RuntimeException(context.toString()+"ImageSaver Not initialized Correctly");
        }

    }

}
