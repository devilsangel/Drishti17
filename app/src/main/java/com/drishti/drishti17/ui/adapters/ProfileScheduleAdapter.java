package com.drishti.drishti17.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.drishti.drishti17.R;
import com.drishti.drishti17.ui.EventPage;
import com.drishti.drishti17.ui.Profile;

import java.util.ArrayList;

/**
 * Created by kevin on 3/4/17.
 */

public class ProfileScheduleAdapter extends BaseAdapter {
    ArrayList<String> eventList;
    ArrayList<String> timeList;
    ArrayList<Integer> idList;
    Context context;
    public static LayoutInflater inflater=null;
    public ProfileScheduleAdapter(Context context, ArrayList<String> eventList, ArrayList<String> timeList, ArrayList<Integer> idList){
        this.eventList=eventList;
        this.timeList=timeList;
        this.context=context;
        this.idList=idList;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder{
        TextView t1;
        TextView time;
        Button b;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        Log.d("here",position+"");
        View rowView=inflater.inflate(R.layout.profile_schedule_list,null);
        holder.t1=(TextView)rowView.findViewById(R.id.event);
        holder.time=(TextView)rowView.findViewById(R.id.time);
        holder.t1.setText(eventList.get(position));
        holder.time.setText(timeList.get(position));
        holder.b=(Button)rowView.findViewById(R.id.overlay);
        holder.b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, EventPage.class).putExtra("id",idList.get(position)));
            }
        });
        return rowView;
    }
}
