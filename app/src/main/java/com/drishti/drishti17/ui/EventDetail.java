package com.drishti.drishti17.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.drishti.drishti17.R;
import com.drishti.drishti17.ui.factory.Blur.Blur;
import com.drishti.drishti17.util.Import;

public class EventDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        setUpUI();
    }

    private void setUpUI() {
        new Blur((ImageView) findViewById(R.id.back), this, 3, 5).applyBlur(true, findViewById(R.id.inner_layout));

        Import.settypefaces(this, "SourceSansPro-Regular.otf", (TextView) findViewById(R.id.desp));
        Import.settypefaces(this, "SourceSansPro-Regular.otf", (TextView) findViewById(R.id.time));
        Import.settypefaces(this, "SourceSansPro-Regular.otf", (TextView) findViewById(R.id.date));
        Import.settypefaces(this, "SourceSansPro-Regular.otf", (TextView) findViewById(R.id.location));

    }

}
