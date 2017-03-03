package com.drishti.drishti17.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.drishti.drishti17.R;
import com.drishti.drishti17.ui.transition.FabTransform;
import com.drishti.drishti17.util.Import;
import com.drishti.drishti17.util.NavUtil;

public class NavigationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = NavigationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        if (Import.isVersionOK(Build.VERSION_CODES.LOLLIPOP)) {
            if (FabTransform.configure(this, findViewById(R.id.content_navigation))) {
                Log.d(TAG, "onCreate: animation success ");
            }
        }


        setupUI();
        findViewById(R.id.fab).setOnClickListener(this);
    }

    private void setupUI() {
     /*   Import.settypefaces(this,"SourceSansPro-Regular.otf", (TextView) findViewById(R.id.text_home));
        Import.settypefaces(this,"SourceSansPro-Regular.otf", (TextView) findViewById(R.id.text_events));
        Import.settypefaces(this,"SourceSansPro-Regular.otf", (TextView) findViewById(R.id.text_expo));
        Import.settypefaces(this,"SourceSansPro-Regular.otf", (TextView) findViewById(R.id.text_dev));
        Import.settypefaces(this,"SourceSansPro-Regular.otf", (TextView) findViewById(R.id.text_schedule));
        Import.settypefaces(this,"SourceSansPro-Regular.otf", (TextView) findViewById(R.id.text_profile));
        Import.settypefaces(this,"SourceSansPro-Regular.otf", (TextView) findViewById(R.id.text_map));*/

        findViewById(R.id.layout_center_top).setOnClickListener(this);
        findViewById(R.id.layout_center_bottom).setOnClickListener(this);
        findViewById(R.id.layout_center).setOnClickListener(this);
        findViewById(R.id.layout_left_bottom).setOnClickListener(this);
        findViewById(R.id.layout_left_top).setOnClickListener(this);
        findViewById(R.id.layout_right_top).setOnClickListener(this);
        findViewById(R.id.layout_right_bottom).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                NavUtil.returnParent(this,-1);
                break;
            case R.id.layout_left_top:
                NavUtil.returnParent(this,1);
                break;
            case R.id.layout_left_bottom:
                NavUtil.returnParent(this,2);
                break;
            case R.id.layout_center_top:
                NavUtil.returnParent(this,3);
                break;
            case R.id.layout_center:
                NavUtil.returnParent(this,4);
                break;
            case R.id.layout_center_bottom:
                NavUtil.returnParent(this,5);
                break;
            case R.id.layout_right_top:
                NavUtil.returnParent(this,6);
                break;
            case R.id.layout_right_bottom:
                NavUtil.returnParent(this,7);
                break;

        }
    }
}
