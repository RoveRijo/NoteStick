package com.rove.notestick.Util;

import com.rove.notestick.CustomViews.TextImageLayout;

import java.io.FileNotFoundException;

public interface JsonViewModem {
    String getJsonfromView();
    void populateViewfromJson(String Json) throws FileNotFoundException;

    void setViewContainer(ViewContainer<TextImageLayout> viewContainer);

    interface ViewContainer<T>{
        T getViewContainer();
    }
}
