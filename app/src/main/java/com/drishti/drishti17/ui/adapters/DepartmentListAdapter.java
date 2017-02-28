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
import com.drishti.drishti17.network.models.DepartmentModel;
import com.drishti.drishti17.ui.EventList;
import com.drishti.drishti17.util.UIUtil;

import java.util.HashMap;

/**
 * Created by droidcafe on 2/10/2017
 */

public class DepartmentListAdapter extends RecyclerView.Adapter<DepartmentListAdapter.ViewHolder> {

    private HashMap<Integer, DepartmentModel> deptMap;
    private Context context;

    private static final String TAG = DepartmentListAdapter.class.getSimpleName();
    public DepartmentListAdapter(HashMap<Integer, DepartmentModel> deptMap, Context context) {
        this.deptMap = deptMap;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View deptCard = layoutInflater.inflate(R.layout.recycler_event_list, parent, false);
        return new ViewHolder(deptCard);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DepartmentModel deptModel = deptMap.get(position);
        holder.title.setText(deptModel.name);

        int id = UIUtil.getBackgroundImage(context,deptModel.url);
        Log.d(TAG, "onBindViewHolder: "+deptModel.url +" "+id);
        Glide.with(context)
                .load(id)
                .into(holder.backImage);
    }


    @Override
    public int getItemCount() {
        return deptMap.size();
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
                    proceedEventList();
                }
            });
        }

        void proceedEventList() {
            Intent intent_expand = new Intent(view.getContext(), EventList.class);
            Bundle bundle = new Bundle();
            bundle.putString("dept", deptMap.get(getAdapterPosition()).key);
            intent_expand.putExtras(bundle);
            view.getContext().startActivity(intent_expand);
        }
    }
}