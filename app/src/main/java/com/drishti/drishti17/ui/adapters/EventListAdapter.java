package com.drishti.drishti17.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.drishti.drishti17.R;
import com.drishti.drishti17.util.db.EventTable;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by droidcafe on 2/5/2017
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private List<EventTable> eventList;
    private static final String TAG = EventListAdapter.class.getSimpleName();

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Context context;
    static String from;

    public EventListAdapter(List<EventTable> eventList, Context context, String from) {
        this.from = from;
        this.eventList = eventList;
        this.context = context;

    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View deptCard = layoutInflater.inflate(R.layout.recycler_event_list, parent, false);
        return new ViewHolder(deptCard);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        EventTable event = eventList.get(position);

        if (event.image != null &&event.image.startsWith("https://firebasestorage.googleapis.com/"))
        {
            Log.d(TAG, "onBindViewHolder: image url "+event.image);
            StorageReference gsReference = storage.getReferenceFromUrl(event.image);
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(gsReference)
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
                    switch (from) {
                        case "competitions":
                            proceedCompetition();
                            break;
                        case "workshops":
                            proceedWorkshop();
                            break;
                    }
                }
            });

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
