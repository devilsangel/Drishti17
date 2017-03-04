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
import com.drishti.drishti17.db.EventsTable;
import com.drishti.drishti17.network.models.EventModel;
import com.drishti.drishti17.ui.adapters.EventListAdapter;
import com.drishti.drishti17.ui.factory.ProgressDialog;
import com.drishti.drishti17.util.Import;

import java.util.List;

public class CompetitionListFragment extends Fragment {

    private static final String TAG = CompetitionListFragment.class.getSimpleName();
    String deptKey, where;

    public CompetitionListFragment() {

    }

    public static CompetitionListFragment newInstance(String deptKey, String where) {
        CompetitionListFragment fragment = new CompetitionListFragment();

        Bundle args = new Bundle();
        args.putString("deptKey", deptKey);
        args.putString("where", where);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            deptKey = getArguments().getString("deptKey");
            where = getArguments().getString("where");
            Log.d(TAG, "onCreate: department selected " + deptKey);
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
            Log.d(TAG, "onViewCreated: loading events");
            loadEvents();
        }
    }

    private void loadEvents() {
        new AsyncLoad().execute(deptKey);
    }

    class AsyncLoad extends AsyncTask<String, Void, List<EventModel>> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.showProgressDialog();

        }

        @Override
        protected List<EventModel> doInBackground(String... strings) {
            String deptKey = strings[0];

            String[] args = {deptKey};
            List<EventModel> eventModels = EventsTable.getAllEventsMinified(getContext(),
                    where, where.contains("?") ? args : null, null);

            Log.d(TAG, "loadEvents: total no of event " + eventModels.size());
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

        private void onSuccess(List<EventModel> deptMap) {

            if (getActivity() == null)
                return;

            getActivity().findViewById(R.id.empty_view).setVisibility(View.GONE);
            getActivity().findViewById(R.id.empty_text).setVisibility(View.GONE);
            getActivity().findViewById(R.id.cube).setVisibility(View.GONE);
            getActivity().findViewById(R.id.reload).setVisibility(View.GONE);

            RecyclerView deptList = (RecyclerView) getActivity().findViewById(R.id.list_events);


            deptList.setVisibility(View.VISIBLE);
            EventListAdapter eventListAdapter = new EventListAdapter(deptMap, getContext(), "eventlist");
            deptList.setAdapter(eventListAdapter);
            deptList.setHasFixedSize(true);
            deptList.setLayoutManager(new LinearLayoutManager(getContext()));

        }

        private void onFailure() {
            getActivity().findViewById(R.id.list_events).setVisibility(View.GONE);
            getActivity().findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.empty_text).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.cube).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.reload).setVisibility(View.VISIBLE);
            //   Import.snack(getActivity().findViewById(R.id.content_event_list), "No data available. Try for computer science");
        }
    }


}
