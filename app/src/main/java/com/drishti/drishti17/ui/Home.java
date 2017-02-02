package com.drishti.drishti17.ui;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.drishti.drishti17.R;
import com.drishti.drishti17.ui.transition.FabTransform;
import com.drishti.drishti17.util.Import;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Home extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_NAVIGATION_ITEM_CLICKED = 21;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                if (Import.isVersionOK(Build.VERSION_CODES.LOLLIPOP)) {
                    startFabTransition();
                }else{
                    startActivity(new Intent(this,NavigationActivity.class));
                }
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startFabTransition() {
        Intent intent = new Intent(this, NavigationActivity.class);
        FabTransform.addExtras(intent,
                ContextCompat.getColor(this, R.color.colorPrimary_nav), R.drawable.ic_menu_white_24dp);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, fab,
                getString(R.string.transition_navigation_icon) );
        startActivityForResult(intent, RC_NAVIGATION_ITEM_CLICKED, options.toBundle());
    }
}
