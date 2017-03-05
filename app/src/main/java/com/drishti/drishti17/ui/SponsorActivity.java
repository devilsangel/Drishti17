package com.drishti.drishti17.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.drishti.drishti17.R;
import com.drishti.drishti17.db.data.SponsorMap;
import com.drishti.drishti17.ui.adapters.SponsorListAdapter;
import com.drishti.drishti17.util.NavUtil;
import com.drishti.drishti17.util.UIUtil;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SponsorActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SponsorActivity.class.getSimpleName();
    Map<String, SponsorMap.Sponsor[]> sponsorMap;
    String[] title;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor);

       /* ImageView gifView = (ImageView) findViewById(R.id.gif_clap);
        GlideDrawableImageViewTarget viewTarget = new GlideDrawableImageViewTarget(gifView);
        Glide.with(this).load(R.raw.giphy).into(viewTarget);*/
        UIUtil.setToolBar(this, "Sponsors");

        ButterKnife.bind(this);
        fab.setOnClickListener(this);

        loadSponsors();
        setUI();

    }

    private void loadSponsors() {
        title = getResources().getStringArray(R.array.sponsors_title);
        sponsorMap = SponsorMap.getMap();

        Log.d(TAG, "loadSponsors: sponsor size " + sponsorMap.size());

    }

    private void setUI() {

        RecyclerView sponsorList = (RecyclerView) findViewById(R.id.sponsor_list);
        sponsorList.setAdapter(new SponsorListAdapter(this, title, sponsorMap));
        sponsorList.setLayoutManager(new LinearLayoutManager(this));

        sponsorList.setHasFixedSize(true);
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
