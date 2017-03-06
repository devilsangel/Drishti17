package com.drishti.drishti17.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.drishti.drishti17.R;
import com.drishti.drishti17.util.UIUtil;

import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.OverFlipMode;

public class Expo extends AppCompatActivity implements FlipView.OnFlipListener {

    private static final String TAG = Expo.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FlipView mFlipView = (FlipView) findViewById(R.id.flipview);
        mFlipView.setAdapter(new HomeFlipAdapter(this));
        mFlipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);

        mFlipView.setOnFlipListener(this);
    }

    @Override
    public void onFlippedToPage(FlipView v, int position, long id) {
        Log.d(TAG, "onFlippedToPage: flipped to "+position);
    }

    public class HomeFlipAdapter extends BaseAdapter {

        String[] names, images;

        public HomeFlipAdapter(Context context) {
            names = context.getResources().getStringArray(R.array.dept);
            images = context.getResources().getStringArray(R.array.dept_images);

        }

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View newview = inflater.inflate(R.layout.include_ad_view, viewGroup, false);

            TextView title = (TextView) newview.findViewById(R.id.title);
            TextView desp = (TextView) newview.findViewById(R.id.desp);
            ImageView promo = (ImageView) newview.findViewById(R.id.promo);

            title.setText(names[i]);
            Context context = viewGroup.getContext();
            promo.setImageDrawable(UIUtil.getDrawable(context, UIUtil.getBackgroundImage(context, images[i])));


            return newview;
        }
    }

}
