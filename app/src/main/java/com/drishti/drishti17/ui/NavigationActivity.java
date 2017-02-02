package com.drishti.drishti17.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.drishti.drishti17.R;
import com.drishti.drishti17.ui.transition.FabTransform;
import com.drishti.drishti17.util.Import;

public class NavigationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = NavigationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        if (FabTransform.configure(this, findViewById(R.id.content_navigation))) {
            Log.d(TAG, "onCreate: animation success ");
        }

        setupUI();
        findViewById(R.id.fab).setOnClickListener(this);
    }

    private void setupUI() {
        Import.settypefaces(this,"SourceSansPro-Regular.otf", (TextView) findViewById(R.id.text_home));
        Import.settypefaces(this,"SourceSansPro-Regular.otf", (TextView) findViewById(R.id.text_events));
        Import.settypefaces(this,"SourceSansPro-Regular.otf", (TextView) findViewById(R.id.text_expo));
        Import.settypefaces(this,"SourceSansPro-Regular.otf", (TextView) findViewById(R.id.text_dev));
        Import.settypefaces(this,"SourceSansPro-Regular.otf", (TextView) findViewById(R.id.text_schedule));
        Import.settypefaces(this,"SourceSansPro-Regular.otf", (TextView) findViewById(R.id.text_profile));
        Import.settypefaces(this,"SourceSansPro-Regular.otf", (TextView) findViewById(R.id.text_map));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:

                //finish();
                break;
        }
    }
}
