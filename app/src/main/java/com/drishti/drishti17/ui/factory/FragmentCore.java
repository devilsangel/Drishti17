package com.drishti.drishti17.ui.factory;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.drishti.drishti17.util.Import;


public class FragmentCore extends TextView {
    public FragmentCore(Context context) {
        this(context, null);
    }

    public FragmentCore(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FragmentCore(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Import.settypefaces(context,"Fragmentcore.otf",this);
    }

}
