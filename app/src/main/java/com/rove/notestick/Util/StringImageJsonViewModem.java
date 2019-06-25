package com.rove.notestick.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.rove.notestick.CustomViews.ImageViewWithSrc;
import com.rove.notestick.CustomViews.TextImageLayout;
import com.rove.notestick.R;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

public class StringImageJsonViewModem implements JsonViewModem {
    private Context context;
    private ImageSaver imageSaver;
    private ViewContainer<TextImageLayout> viewContainer;
    public static final int EDITEXT_TYPE = 1;
    public static final int IMAGEVIEW_TYPE = 2;

    public static final int SIZE_WRAPCONTENT = -1;
    public static final int SIZE_MATCHPARENT = -2;

    public StringImageJsonViewModem(Context context, ImageSaver imageSaver) {
        this.context = context;
        this.imageSaver = imageSaver;
    }

    @Override
    public String getJsonfromView() {
        TextImageLayout textImageLayout = viewContainer.getViewContainer();
        int viewcount = textImageLayout.getChildCount();
        JsonArray jsonArray = new JsonArray();
        ContentView contentView;
        for (int i = 0; i < viewcount; i++) {
            View view = textImageLayout.getChildAt(i);
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                contentView = new ContentView(EDITEXT_TYPE, editText.getText().toString(),
                        new ViewSize(SIZE_MATCHPARENT, SIZE_WRAPCONTENT));
            } else if (view instanceof ImageViewWithSrc) {
                ImageViewWithSrc imageView = (ImageViewWithSrc) view;
                contentView = new ContentView(IMAGEVIEW_TYPE, imageView.getURI(),
                        //new ViewSize(imageView.getWidth(), imageView.getHeight()));
                        new ViewSize(SIZE_MATCHPARENT, SIZE_WRAPCONTENT));
            } else {
                contentView = null;
            }
            JsonObject jsonObj = new Gson().toJsonTree(contentView).getAsJsonObject();
            //jsonArray.add(jsonObj);
            jsonArray.add(jsonObj);
        }
        //return jsonArray.toString();
        return String.valueOf(jsonArray);
    }

    @Override
    public void populateViewfromJson(String Json) throws FileNotFoundException {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<ContentView>>() {
        }.getType();
        List<ContentView> contentViews = gson.fromJson(Json, collectionType);
        TextImageLayout textImageLayout = viewContainer.getViewContainer();
        textImageLayout.removeAllViews();
        for (ContentView contentView : contentViews) {
            int viewtype = contentView.getViewType();
            ViewGroup.LayoutParams params;
            switch (viewtype) {
                case EDITEXT_TYPE:
                    EditText editText = new EditText(new ContextThemeWrapper(context,R.style.content_new_note_style));
                    params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                            , ViewGroup.LayoutParams.WRAP_CONTENT);
                    editText.setLayoutParams(params);
                    editText.setText(contentView.content);
                    editText.setEnabled(false);
                    textImageLayout.addView(editText);
                    break;
                case IMAGEVIEW_TYPE:
                    ImageViewWithSrc imageview = new ImageViewWithSrc(context, imageSaver);
                    if (contentView.getViewSize().Width == SIZE_MATCHPARENT && contentView.getViewSize().Height ==
                            SIZE_WRAPCONTENT) {
                        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                                , ViewGroup.LayoutParams.WRAP_CONTENT);
                    } else {
                        params = new ViewGroup.LayoutParams(contentView.getViewSize().Width
                                , contentView.getViewSize().Height);
                    }
                    imageview.setLayoutParams(params);
                    imageSaver.setFileName(contentView.getContent());
                    Bitmap image = imageSaver.load();
                    imageview.setImageBitmap(image);
                    textImageLayout.addView(imageview);
                    break;
                default:
                    break;
            }

        }


    }

    @Override
    public void setViewContainer(ViewContainer<TextImageLayout> viewContainer) {
        this.viewContainer = viewContainer;
    }

    class ContentView {
        private int viewType;


        private String content;
        private ViewSize viewSize;

        public ContentView(int viewType, String content, ViewSize viewSize) {
            this.viewType = viewType;
            this.content = content;
            this.viewSize = viewSize;
        }

        public int getViewType() {
            return viewType;
        }

        public String getContent() {
            return content;
        }

        public ViewSize getViewSize() {
            return viewSize;
        }
    }

    private class ViewSize {
        private int Width;
        private int Height;

        public ViewSize(int width, int height) {
            Width = width;
            Height = height;
        }
    }


}
