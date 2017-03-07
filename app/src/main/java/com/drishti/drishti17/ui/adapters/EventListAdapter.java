package com.drishti.drishti17.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.drishti.drishti17.R;
import com.drishti.drishti17.network.models.EventModel;
import com.drishti.drishti17.ui.EventPage;
import com.drishti.drishti17.util.Global;
import com.drishti.drishti17.util.UIUtil;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Random;

/**
 * Created by nirmal on 2/5/2017
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private List<EventModel> eventList;
    private static final String TAG = EventListAdapter.class.getSimpleName();

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Context context;
    static String from;
    private Random random;
    private FirebaseAnalytics mFirebaseAnalytics;

    public EventListAdapter(List<EventModel> eventList, Context context, String from) {
        this.from = from;
        this.eventList = eventList;
        this.context = context;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        random = new Random();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View deptCard = layoutInflater.inflate(R.layout.recycler_event_list, parent, false);
        return new ViewHolder(deptCard);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        EventModel event = eventList.get(position);

        int placeholder = random.nextInt(context.getResources().getInteger(R.integer.blur_limit));
        int error = random.nextInt(context.getResources().getInteger(R.integer.logo_limit));

        if (event.image != null &&event.image.startsWith("https://firebasestorage.googleapis.com/"))
        {
            Log.d(TAG, "onBindViewHolder: image url "+event.image);
            StorageReference gsReference = storage.getReferenceFromUrl(event.image);
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(gsReference)
                    .error(UIUtil.getBackgroundImage(context,"drishti_logo"+error))
                    .placeholder(UIUtil.getBackgroundImage(context,"blur"+placeholder))
                    .crossFade()
                    .into(holder.backImage);
        }else{
            Glide.with(context)
                    .load(event.image)
                    .error(UIUtil.getBackgroundImage(context,"drishti_logo"+error))
                    .placeholder(UIUtil.getBackgroundImage(context,"blur"+placeholder))
                    .crossFade()
                    .into(holder.backImage);
        }

        holder.title.setText(event.name);

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

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
                    Log.d(TAG, "onClick: "+from);
                    proceedDetail();
                }
            });

        }

        void proceedDetail() {
            mFirebaseAnalytics.logEvent(Global.FIRE_EVENT_CLICK,new Bundle());

            Log.d(TAG, "proceedDetail: "+eventList.get(getAdapterPosition()).server_id);
            Intent intent_expand = new Intent(view.getContext(), EventPage.class);
            Bundle bundle = new Bundle();
            bundle.putInt("id", eventList.get(getAdapterPosition()).server_id);
            intent_expand.putExtras(bundle);
            view.getContext().startActivity(intent_expand);
        }
        void proceedCompetition() {
           /* Intent intent_expand = new Intent(view.getContext(), EventDetail.class);
            Bundle bundle = new Bundle();
            bundle.putInt("dept", eventList.get(getAdapterPosition()).server_id);
            intent_expand.putExtras(bundle);
            view.getContext().startActivity(intent_expand);*/
        }

        void proceedWorkshop() {
           /* Intent intent_expand = new Intent(view.getContext(), EventDetail.class);
            Bundle bundle = new Bundle();
            bundle.putString("dept", keys.get(getAdapterPosition()));
            intent_expand.putExtras(bundle);
            view.getContext().startActivity(intent_expand);*/
        }
    }
}
