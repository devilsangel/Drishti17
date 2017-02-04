package com.drishti.drishti17.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.drishti.drishti17.R;

public class TempHome extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       findViewById(R.id.button_home).setOnClickListener(this);
       findViewById(R.id.button_login).setOnClickListener(this);
       findViewById(R.id.button_qr).setOnClickListener(this);
       findViewById(R.id.button_events).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_home:
                startActivity(new Intent(this, Home.class));
                break;
            case R.id.button_login:
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.button_qr:
                startActivity(new Intent(this, QRactivity.class));
                break;
            case R.id.button_events:
                startActivity(new Intent(this, Events.class));
                break;
        }

        finish();
    }
}