package com.drishti.drishti17.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.drishti.drishti17.R;

import java.util.ArrayList;

/**
 * Created by kevin on 3/5/17.
 */

public class GrpListAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> names;
    public static LayoutInflater inflater=null;
    public GrpListAdapter(Context context,ArrayList<String> names){
        this.context=context;
        this.names=names;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return names.size();
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
        Button del;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("herebloo",position+"");
        GrpListAdapter.Holder holder=new GrpListAdapter.Holder();
        View rowView=inflater.inflate(R.layout.grp_list,null);
        holder.t1=(TextView)rowView.findViewById(R.id.uname);
        holder.t1.setText(names.get(position));
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return rowView;
    }
}
