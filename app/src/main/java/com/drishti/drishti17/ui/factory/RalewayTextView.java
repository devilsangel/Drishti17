package com.drishti.drishti17.ui.factory;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.drishti.drishti17.util.Import;


public class RalewayTextView extends TextView {
    public RalewayTextView(Context context) {
        this(context, null);
    }

    public RalewayTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RalewayTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Import.settypefaces(context,"Raleway-Regular.ttf",this);
    }

}
