package com.drishti.drishti17.ui.factory;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.drishti.drishti17.util.Import;


public class ProximaTextView extends TextView {
    public ProximaTextView(Context context) {
        this(context, null);
    }

    public ProximaTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProximaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Import.settypefaces(context,"ProximaNovaSoft-Regular.otf",this);
    }

}
