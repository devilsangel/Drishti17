package com.drishti.drishti17.ui.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drishti.drishti17.R;
import com.drishti.drishti17.ui.adapters.EventListAdapter;
import com.drishti.drishti17.ui.factory.ProgressDialog;
import com.drishti.drishti17.util.Import;
import com.drishti.drishti17.util.db.EventTable;

import java.util.List;

public class CompetitionListFragment extends Fragment {

    private static final String TAG = CompetitionListFragment.class.getSimpleName();
    String deptKey;

    public CompetitionListFragment() {

    }

    public static CompetitionListFragment newInstance(String deptKey) {
        CompetitionListFragment fragment = new CompetitionListFragment();

        Bundle args = new Bundle();
        args.putString("deptKey", deptKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            deptKey = getArguments().getString("deptKey");
            Log.d(TAG, "onCreate: department selected "+deptKey);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_competition_list, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!Import.isEventDownloading()) {
            loadEvents();
        }
    }

    private void loadEvents() {
        new AsyncLoad().execute(deptKey);
    }

     class AsyncLoad extends AsyncTask<String, Void, List<EventTable>> {

        ProgressDialog progressDialog ;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.showProgressDialog();

        }

        @Override
        protected List<EventTable> doInBackground(String... strings) {
            String deptKey = strings[0];
            String where = "category = ? and is_workshop = 0";
            String[] args = {deptKey};
            List<EventTable> eventModels = EventTable.find(EventTable.class, where, args);

            Log.d(TAG, "loadEvents: event no " + eventModels.size());
            return eventModels;
        }

         @Override
         protected void onPostExecute(List<EventTable> eventTables) {
             super.onPostExecute(eventTables);

             progressDialog.disMissProgressDialog();
             if(eventTables.size() == 0){
                 onFailure();
             }else{
                 onSuccess(eventTables);
             }
         }

         private void onSuccess(List<EventTable> deptMap) {
             RecyclerView deptList = (RecyclerView)getActivity().findViewById(R.id.list_events);

             deptList.setVisibility(View.VISIBLE);
             EventListAdapter eventListAdapter = new EventListAdapter(deptMap,getContext(),"eventlist");
             deptList.setAdapter(eventListAdapter);
             deptList.setHasFixedSize(true);
             deptList.setLayoutManager(new LinearLayoutManager(getContext()));

         }

         private void onFailure() {
             Import.snack(getActivity().findViewById(R.id.content_event_list),"No data available. Try for computer science");
         }
     }



}
