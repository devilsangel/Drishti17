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
import com.drishti.drishti17.network.models.HighLightModel;
import com.drishti.drishti17.util.UIUtil;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by nirmal on 2/19/2017
 */

public class HomeFlipAdapter extends BaseAdapter {

    private static final String TAG = HomeFlipAdapter.class.getSimpleName();

    private List<HighLightModel> items = new ArrayList<>();
    Random random;

    public interface Callback {
        public void onPageRequested(int page);
    }

    public HomeFlipAdapter(Context context, List<HighLightModel> flipList) {
        items = flipList;
        random = new Random();
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int position) {
        return position < items.size() && position >= 0 ? items.get(position).getId() : -1;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

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
        // int placeholder = random.nextInt(context.getResources().getInteger(R.integer.blur_limit));
        //  int error = random.nextInt(context.getResources().getInteger(R.integer.logo_limit));

        holder.title.setText(items.get(position).getName());
        UIUtil.printHTML(holder.desp, items.get(position).getPromo());

        // int id = Import.getBackgroundImage(context,items.get(position).getImage());
        // UIUtil.loadPic(context, holder.promo, items.get(position).getImage(), placeholder, error);

        String imageUrl = items.get(position).getImage();
        if (imageUrl != null && imageUrl.startsWith("https://firebasestorage.googleapis.com/")) {
            StorageReference gsReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(gsReference)
                    .error(UIUtil.getBackgroundImage(context, "drishti_logo" + 0))
                    .crossFade()
                    .into(holder.promo);
        } else {
            Glide.with(context)
                    .load(imageUrl)
                    .error(R.drawable.drishti_logo1)
                    .into(holder.promo);
        }

//        Glide.with(context)
//                .load(items.get(position).getImage())
//                .error(R.drawable.drishti_logo1)
//                .crossFade()
//                .into(holder.promo);
        return view;
    }


    static class ViewHolder {
        TextView title, desp;
        ImageView promo;
    }

}
