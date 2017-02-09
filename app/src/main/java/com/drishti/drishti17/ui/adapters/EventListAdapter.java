package com.drishti.drishti17.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.drishti.drishti17.R;
import com.drishti.drishti17.network.models.EventListModel;
import com.drishti.drishti17.ui.EventDetail;
import com.drishti.drishti17.ui.EventList;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by droidcafe on 2/5/2017
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private HashMap<String, EventListModel> map;
    private static ArrayList<String> keys;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Context context;
    static String from;

    public EventListAdapter(HashMap<String, EventListModel> deptMap,Context context,String from) {
        this.from = from;
        this.map = deptMap;
        this.context =context;
        keys = new ArrayList<>(deptMap.size());
        for (Map.Entry<String, EventListModel> dept : deptMap.entrySet()) {
            keys.add(dept.getKey());
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View deptCard = layoutInflater.inflate(R.layout.recycler_event_list, parent, false);
        return new ViewHolder(deptCard);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        EventListModel eventListModel = map.get(keys.get(position));
        StorageReference gsReference = storage.getReferenceFromUrl(eventListModel.storage_url);

        holder.title.setText(eventListModel.name);
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(gsReference)
                .into(holder.backImage);

    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView backImage;
        TextView title;
        View view;

        ViewHolder(View itemView) {
            super(itemView);

            backImage = (ImageView) itemView.findViewById(R.id.back_image);
            title = (TextView) itemView.findViewById(R.id.title);
            view = itemView.findViewById(R.id.root_layout);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   switch (from){
                       case "dept":
                           proceedEventList();
                           break;
                       case "eventlist":
                           proceedEvent();
                           break;
                   }
                }
            });

        }

        void proceedEventList() {
            Intent intent_expand = new Intent(view.getContext(), EventList.class);
            Bundle bundle = new Bundle();
            bundle.putString("dept", keys.get(getAdapterPosition()));
            intent_expand.putExtras(bundle);
            view.getContext().startActivity(intent_expand);
        }

        void proceedEvent() {
            Intent intent_expand = new Intent(view.getContext(), EventDetail.class);
            Bundle bundle = new Bundle();
            bundle.putString("dept", keys.get(getAdapterPosition()));
            intent_expand.putExtras(bundle);
            view.getContext().startActivity(intent_expand);
        }
    }
}
