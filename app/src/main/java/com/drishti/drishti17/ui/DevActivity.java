package com.drishti.drishti17.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.drishti.drishti17.R;
import com.drishti.drishti17.util.NavUtil;
import com.drishti.drishti17.util.UIUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DevActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = DevActivity.class.getSimpleName();
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev);
        UIUtil.setToolBar(this, "The Team");

        ButterKnife.bind(this);
        fab.setOnClickListener(this);

        HorizontalScrollView hs = (HorizontalScrollView) findViewById(R.id.scroll_list);

        final View test = findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onCreate: " + test.getTop() + " " + test.getLeft());

            }
        });

       /* for (int i = 0; i < 8; i++) {
            final int j = i;
            Log.d(TAG, "onCreate: i is "+i);
            hs.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Import.toast(DevActivity.this,"Position is "+j);

                }
            });
        }*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                NavUtil.openNavigation(this, this, fab);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (NavUtil.isFromNav(requestCode)) {
            NavUtil.handleNavigation(this, this, resultCode);
        }
    }
}
