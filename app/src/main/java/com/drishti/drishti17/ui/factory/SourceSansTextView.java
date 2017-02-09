package com.drishti.drishti17.ui.factory;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.drishti.drishti17.util.Import;


public class SourceSansTextView extends TextView {
    public SourceSansTextView(Context context) {
        this(context, null);
    }

    public SourceSansTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SourceSansTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Import.settypefaces(context,"SourceSansPro-Regular.otf",this);
    }

}
