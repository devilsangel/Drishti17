package com.drishti.drishti17.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.drishti.drishti17.R;
import com.drishti.drishti17.util.GuillotineUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventList extends AppCompatActivity implements GuillotineUtil.OnNavigationClickListener {

    private static final long RIPPLE_DURATION = 250;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.root)
    FrameLayout root;
    @BindView(R.id.content_hamburger)
    View contentHamburger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        ButterKnife.bind(this);


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }


        new GuillotineUtil(this).setUpNav(root,findViewById(R.id.content_hamburger),toolbar,this);

    }

    @Override
    public void onNavigationClick(int id) {
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        String text ="";
        switch (id) {
            case R.id.competitions_group:
                text = getString(R.string.competitions);
                break;
            case R.id.workshops_group:
                text = getString(R.string.workshops);
                break;
            case R.id.informals_group:
                text = getString(R.string.informals);
                break;
        }

        title.setText(text);
    }
}
