package com.drishti.drishti17.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.drishti.drishti17.R;
import com.drishti.drishti17.ui.factory.Blur.Blur;
import com.firebase.ui.storage.images.FirebaseImageLoader;

public class EventDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        setUpUI();
    }

    private void setUpUI() {

            new Blur((ImageView) findViewById(R.id.back), this, 3, 10).applyBlur(true, findViewById(R.id.inner_layout));

    }

}
