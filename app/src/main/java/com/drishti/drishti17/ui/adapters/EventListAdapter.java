package com.drishti.drishti17.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.drishti.drishti17.R;
import com.drishti.drishti17.network.models.EventListModel;
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

    private HashMap<String, EventListModel> deptMap;
    private ArrayList<String> deptKeys;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Context context;

    public EventListAdapter(HashMap<String, EventListModel> deptMap,Context context) {
        this.deptMap = deptMap;
        this.context =context;
        deptKeys = new ArrayList<>(deptMap.size());
        for (Map.Entry<String, EventListModel> dept : deptMap.entrySet()) {
            deptKeys.add(dept.getKey());
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View deptCard = layoutInflater.inflate(R.layout.recycler_event_list, parent, false);
        return new ViewHolder(deptCard);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EventListModel eventListModel = deptMap.get(deptKeys.get(position));
        StorageReference gsReference = storage.getReferenceFromUrl(eventListModel.storage_url);

        holder.title.setText(eventListModel.name);
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(gsReference)
                .into(holder.backImage);
    }

    @Override
    public int getItemCount() {
        return deptKeys.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView backImage;
        TextView title;

        ViewHolder(View itemView) {
            super(itemView);

            backImage = (ImageView) itemView.findViewById(R.id.back_image);
            title = (TextView) itemView.findViewById(R.id.title);

        }
    }
}
