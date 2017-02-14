package com.drishti.drishti17.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.drishti.drishti17.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainRegister extends AppCompatActivity {
    @BindView(R.id.gender) MaterialSpinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);
        ButterKnife.bind(this);
        spinner.setItems("Select Gender","Male","Female");
        getSupportActionBar().setElevation(0);
    }
}
