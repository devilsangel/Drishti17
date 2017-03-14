package com.drishti.drishti17.ui.adapters;


import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.drishti.drishti17.R;
import com.drishti.drishti17.ui.EventPage;
import com.drishti.drishti17.ui.adapters.ProfileScheduleAdapter;
import com.drishti.drishti17.ui.fragments.FragmentSchedule;
import com.kenber.view.DoubleStickHeadersListAdapter;

import java.util.ArrayList;

/**
 * Created by kevin on 3/14/17.
 */

public class ScheduleAdapter extends ArrayAdapter<FragmentSchedule.ScheduleItem> implements DoubleStickHeadersListAdapter {
    public static LayoutInflater inflater=null;
    ArrayList<FragmentSchedule.ScheduleItem> list;
    Context context;
    public ScheduleAdapter(Context context, int resource, ArrayList<FragmentSchedule.ScheduleItem> list) {
        super(context, resource);
        Log.d("blooo",list.size()+"");
        this.list=list;
        this.context=context;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public FragmentSchedule.ScheduleItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView=inflater.inflate(R.layout.schedule_item,null);
        holder.t1=(TextView)rowView.findViewById(R.id.name);
        holder.t1.setText(list.get(position).name);
        holder.t1.setTextColor(context.getResources().getColor(R.color.white));
        if(getItem(position).level==0) {
            rowView.setBackgroundColor(context.getResources().getColor(R.color.dark_white));
            holder.t1.setTextColor(context.getResources().getColor(R.color.black));
        }
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, EventPage.class).putExtra("id",list.get(position).id));
            }
        });
        return rowView;
    }
    public class Holder{
        TextView t1;
        Button b;
    }
    @Override
    public int getHeaderLevel(int position) {
        return getItem(position).level;
    }
}
