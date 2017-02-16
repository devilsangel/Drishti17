package com.drishti.drishti17.ui.factory;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.drishti.drishti17.util.Import;


public class MakSukTextView extends TextView {
    public MakSukTextView(Context context) {
        this(context, null);
    }

    public MakSukTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MakSukTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Import.settypefaces(context,"PressStart2P-Regular.ttf",this);
    }

}
