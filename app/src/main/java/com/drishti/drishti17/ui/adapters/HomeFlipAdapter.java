package com.drishti.drishti17.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.drishti.drishti17.R;
import com.drishti.drishti17.util.Import;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by droidcafe on 2/19/2017.
 */

public class HomeFlipAdapter extends BaseAdapter {

    private static final String TAG = HomeFlipAdapter.class.getSimpleName();
    private LayoutInflater inflater;
    private Callback callback;
    private List<Item> items = new ArrayList<>();

    public interface Callback {
        public void onPageRequested(int page);
    }

    static class Item {
        static long id = 0;
        String title,picUrl;
        long mId;

        public Item(String title,String picUrl) {
            mId = id++;
            this.title = title;
            this.picUrl  = picUrl;
        }

        String getTitle() {
            return title;
        }
        String getPicUrl() {
            return picUrl;
        }
        long getId() {
            return mId;
        }
    }

    public HomeFlipAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        String[] titles = context.getResources().getStringArray(R.array.temp_events);
        String[] deptPic = context.getResources().getStringArray(R.array.dept_images);

        for (int i = 0; i < 8; i++) {
            items.add(new Item(titles[i],deptPic[i]));
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            Log.d(TAG, "getView: initialising");
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.include_ad_view, viewGroup, false);

            holder.title = (TextView) view.findViewById(R.id.title);
            holder.desp = (TextView) view.findViewById(R.id.desp);
            holder.promo = (ImageView) view.findViewById(R.id.promo);

            view.setTag(holder);
        } else {
            Log.d(TAG, "getView: not initialising");
            holder = (ViewHolder) view.getTag();
        }

        Context context = view.getContext();
        holder.title.setText(items.get(position).getTitle());
        int id = Import.getBackgroundImage(context,items.get(position).getPicUrl());
        Glide.with(context)
                .load(id)
                .into(holder.promo);
        return view;
    }


    static class ViewHolder {
        TextView title, desp;
        ImageView promo;
    }

}
