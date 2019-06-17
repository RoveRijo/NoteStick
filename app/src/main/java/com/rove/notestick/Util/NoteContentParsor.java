package com.rove.notestick.Util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.room.util.StringUtil;

public class NoteContentParsor {
    private List<String> textContents;
    private List<String> imageSrcs;

    public NoteContentParsor(String content) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<StringImageJsonViewModem.ContentView>>() {
        }.getType();
        List<StringImageJsonViewModem.ContentView> contentViews = gson.fromJson(content, collectionType);
        textContents = new ArrayList<>();
        imageSrcs = new ArrayList<>();
        for (StringImageJsonViewModem.ContentView contentView : contentViews) {
            switch (contentView.getViewType()) {
                case StringImageJsonViewModem.EDITEXT_TYPE:
                    textContents.add(contentView.getContent());
                    break;
                case StringImageJsonViewModem.IMAGEVIEW_TYPE:
                    imageSrcs.add(contentView.getContent());
                    break;
            }
        }
    }

    public List<String> getTextContents() {
        return textContents;
    }

    public List<String> getImageSrcs() {
        return imageSrcs;
    }

    public String getContentPreview() {
        String retString = "";
        for(String content:textContents){
            if(!content.isEmpty()){
                return content;
            }
        }
        return retString;
    }
}
