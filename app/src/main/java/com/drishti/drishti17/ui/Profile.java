package com.drishti.drishti17.ui;

import android.support.annotation.BinderThread;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.drishti.drishti17.R;
import com.drishti.drishti17.util.Import;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Profile extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.qr) ImageView imageView;
    @BindView(R.id.college)TextView college;
    @BindView(R.id.name)TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        Import.settypefaces(this, "ProximaNovaSoft-Regular.otf",name);
        Import.settypefaces(this,"SourceSansPro-Regular.otf",college);
    }
}
