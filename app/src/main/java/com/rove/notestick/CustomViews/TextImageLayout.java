package com.rove.notestick.CustomViews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;


import com.rove.notestick.R;
import com.rove.notestick.Util.ImageSaver;
import com.rove.notestick.Util.StringImageJsonViewModem;

import java.io.FileNotFoundException;

public class TextImageLayout extends LinearLayout {

    public TextImageLayout(Context context) {
        super(context);
        this.setOrientation(LinearLayout.VERTICAL);
    }

    public TextImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(LinearLayout.VERTICAL);
    }

    public TextImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOrientation(LinearLayout.VERTICAL);
    }




}

