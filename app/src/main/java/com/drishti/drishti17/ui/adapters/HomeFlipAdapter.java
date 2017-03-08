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
    private OnCardClick cardClick;

    public interface OnCardClick {
        void onPageRequested(boolean is_event,int server_id);
    }

    public HomeFlipAdapter(Context context, List<HighLightModel> flipList,OnCardClick mlistener) {
        setOnClickListener(mlistener);
        items = flipList;
        random = new Random();
    }


    @Override
    public int getCount() {
        return items.size();
    }

    public void setOnClickListener(OnCardClick mlistener) {
        cardClick = mlistener;
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        if (view == null) {
            Log.d(TAG, "getView: initialising");
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.include_ad_view, viewGroup, false);

            holder.title = (TextView) view.findViewById(R.id.title);
            holder.desp = (TextView) view.findViewById(R.id.desp);
            holder.promo = (ImageView) view.findViewById(R.id.promo);
            holder.ad_card = view.findViewById(R.id.ad_card);

            view.setTag(holder);
        } else {
            Log.d(TAG, "getView: not initialising");
            holder = (ViewHolder) view.getTag();
        }


        Context context = view.getContext();

        holder.title.setText(items.get(position).getName());
        UIUtil.printHTML(holder.desp, items.get(position).getPromo());


        holder.ad_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardClick.onPageRequested(items.get(position).getIsEvent(),
                        items.get(position).getServerId());
            }
        });

        String imageUrl = items.get(position).getImage();
        if (imageUrl != null && imageUrl.startsWith("https://firebasestorage.googleapis.com/")) {
            StorageReference gsReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(gsReference)
                    .placeholder(UIUtil.getBackgroundImage(context, "drishti_logo" + 1))
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
        View ad_card;
    }

}
