package com.drishti.drishti17.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.drishti.drishti17.R;
import com.drishti.drishti17.util.NavUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Profile extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.qr) ImageView imageView;
    @BindView(R.id.college)TextView college;
    @BindView(R.id.name)TextView name;
    @BindView(R.id.fab)FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                NavUtil.openNavigation(this,this,fab);
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
