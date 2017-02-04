package com.drishti.drishti17.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.drishti.drishti17.R;
import com.drishti.drishti17.ui.factory.Blur.Blur;

public class EventDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        setUpUI();
    }

    private void setUpUI() {

            new Blur((ImageView) findViewById(R.id.back), this, 3, 10).applyBlur(true, findViewById(R.id.content_event_detail));

    }

}
