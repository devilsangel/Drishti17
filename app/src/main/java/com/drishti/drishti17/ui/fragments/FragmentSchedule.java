package com.drishti.drishti17.ui.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.drishti.drishti17.R;
import com.drishti.drishti17.db.EventsTable;
import com.drishti.drishti17.network.models.EventModel;
import com.drishti.drishti17.ui.adapters.ScheduleAdapter;
import com.drishti.drishti17.ui.factory.ProgressDialog;
import com.drishti.drishti17.util.Import;
import com.kenber.view.DoubleStickyHeaderListView;


import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by kevin on 3/13/17.
 */

public class FragmentSchedule extends Fragment {
    String day,where;
    ProgressDialog progressDialog;
    public FragmentSchedule(){}
    public static FragmentSchedule newInstance(String day,String where){
        FragmentSchedule fragment=new FragmentSchedule();
        Bundle args=new Bundle();
        args.putString("day",day);
        args.putString("where",where);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            day = getArguments().getString("day");
            where = getArguments().getString("where");
        }

    }
    public void initLayout(List<EventModel> events){
        DoubleStickyHeaderListView listView=(DoubleStickyHeaderListView)getView().findViewById(R.id.sticky);
        listView.setDivider(new ColorDrawable(0xff252e39));
        listView.setDividerHeight(1);
        listView.setAdapter(new ScheduleAdapter(getActivity(),R.layout.schedule_item,createItems(events)));
    }
    public ArrayList<ScheduleItem> createItems(List<EventModel> events){
        ArrayList<ScheduleItem> list=new ArrayList<>();
        for(int i=0;i<24;i++){
            boolean first=true;
            for(EventModel e:events){
                Log.d("time",e.time);
                if(e.time==null||e.time.isEmpty()||!Character.isDigit(e.time.charAt(0)))
                    continue;
                Log.d("baaaa","baaaa");
                int time=0;
                try {
                    if (e.time.length() == 5)
                        time = Integer.parseInt(e.time.substring(0,e.time.indexOf(":")));
                    else {
                        int temp=Integer.parseInt(e.time.substring(0,e.time.indexOf(":")));
                        switch (e.time.charAt(e.time.length()-2)){
                            case 'P':
                                time=temp+12;
                                break;
                            case 'p':
                                time=temp+12;
                                break;
                            default:
                                time=temp;
                        }
                    }
                }catch (Exception f){

                }
                if(time>=i&&time<i+1) {
                    if(first){
                        list.add(new ScheduleItem(0,i<10?("0"+i+":00"):(i+":00"),0));
                        first=false;
                    }
                    list.add(new ScheduleItem(1, e.name, e.server_id));
                }
            }
        }
        return list;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!Import.isEventDownloading()) {
            Log.d(TAG, "onViewCreated: loading events");
            loadEvents();
        }

    }
    public void loadEvents(){new AsyncLoad().execute(day);}
    class AsyncLoad extends AsyncTask<String, Void, List<EventModel>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.showProgressDialog();

        }
        @Override
        protected List<EventModel> doInBackground(String... params) {
            String[] args = {day};
            List<EventModel> eventModels = EventsTable.getAllEventsMinified(getContext(),
                    where, where.contains("?") ? args : null, null);
            Log.d(TAG, "loadEvents: total no of event in day " +day+ eventModels.size());
            return eventModels;
        }
        @Override
        protected void onPostExecute(List<EventModel> eventModelList) {
            super.onPostExecute(eventModelList);

            progressDialog.disMissProgressDialog();
            if (eventModelList.size() == 0) {
                onFailure();
            } else {
                onSuccess(eventModelList);
            }
        }
    }
    public void onFailure(){
        getActivity().findViewById(R.id.error).setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(),"Unable to load schedule",Toast.LENGTH_SHORT).show();
    }
    public void onSuccess(List<EventModel> eventModels){
        getActivity().findViewById(R.id.error).setVisibility(View.GONE);
        initLayout(eventModels);
    }
    public static class ScheduleItem{
        public int level,id;
        public String name;
        public ScheduleItem(int level, String name, int id){
            this.level=level;
            this.name=name;
            this.id=id;
        }
    }
}
