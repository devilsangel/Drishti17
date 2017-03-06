package com.drishti.drishti17.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.drishti.drishti17.R;
import com.drishti.drishti17.db.data.SponsorMap;
import com.drishti.drishti17.util.UIUtil;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

/**
 * Created by nirmal on 3/5/2017
 */

public class SponsorListAdapter extends RecyclerView.Adapter<SponsorListAdapter.ViewHolder> {

    String[] titles;
    Map<String, SponsorMap.Sponsor[]> sponsorMap;
    Context context;

    public SponsorListAdapter(Context context, String[] titles,
                              Map<String, SponsorMap.Sponsor[]> sponsorMap) {

        this.context = context;
        this.titles = titles;
        this.sponsorMap = sponsorMap;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_sponsor_card, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String key = titles[position];
        holder.title.setText(key);

        SponsorMap.Sponsor[] sponsors = sponsorMap.get(key);

        for (int i = 0; i < sponsors.length; i += 2) {
            SponsorMap.Sponsor first_sponsor = sponsors[i];
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.include_sponsor_inner_detail, holder.parent_card, false);

            View layout1 = view.findViewById(R.id.layout1);
            setInnerView(layout1,R.id.icon1,R.id.text1,first_sponsor);

            int j =i+1;
            if (j < sponsors.length) {

                view.findViewById(R.id.line).setVisibility(View.GONE);
                SponsorMap.Sponsor second_sponsor = sponsors[j];
                View layout2 = view.findViewById(R.id.layout2);
                setInnerView(layout2,R.id.icon2,R.id.text2,second_sponsor);
            }
            holder.parent_card.addView(view);
        }

    }

    private void setInnerView(View parentView, int iconId,int textId,
                              SponsorMap.Sponsor sponsor) {

        parentView.setVisibility(View.VISIBLE);
        TextView name = (TextView) parentView.findViewById(textId);
        name.setText(sponsor.getTitle());

        ImageView icon = (ImageView) parentView.findViewById(iconId);
        StorageReference gsReference = FirebaseStorage.getInstance().getReferenceFromUrl(sponsor.getImage());
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(gsReference)
                .error(UIUtil.getBackgroundImage(context,"drishti_logo"+0))
                .crossFade()
                .into(icon);

    }


    @Override
    public int getItemCount() {
        return sponsorMap.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout parent_card;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);

            parent_card = (LinearLayout) itemView.findViewById(R.id.sponsor_card);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
