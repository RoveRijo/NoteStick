package com.rove.notestick.CustomViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.rove.notestick.Util.ImageSaver;

import java.io.FileNotFoundException;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class ImageViewWithSrc extends androidx.appcompat.widget.AppCompatImageView{

    private String mImageURI;
    private ImageSaver imageSaver;
    public ImageViewWithSrc(Context context,ImageSaver imageSaver) {
        super(context);
        this.imageSaver = imageSaver;
    }

    public ImageViewWithSrc(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewWithSrc(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public String getURI(){
        return mImageURI;
    }

    public void setmImageURI(String ImageURI) throws FileNotFoundException {
        this.mImageURI = ImageURI;
        imageSaver.setFileName(mImageURI);
        setImageBitmap(imageSaver.load());
    }
}
