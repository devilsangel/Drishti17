package com.drishti.drishti17.ui.factory;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.drishti.drishti17.util.Import;


public class CanaroTextView extends TextView {
    public CanaroTextView(Context context) {
        this(context, null);
    }

    public CanaroTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanaroTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Import.settypefaces(context,"canaro_extra_bold.otf",this);
        setAllCaps(true);
    }

}
