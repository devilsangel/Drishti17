package com.drishti.drishti17.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drishti.drishti17.R;
import com.drishti.drishti17.network.models.EventListModel;
import com.drishti.drishti17.ui.adapters.EventListAdapter;
import com.drishti.drishti17.ui.factory.ProgressDialog;
import com.drishti.drishti17.util.Import;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class DeptListFragment extends Fragment implements ValueEventListener {

    private String from;
    ProgressDialog progressDialog;

    private OnDepartmentSelectListener mListener;

    public DeptListFragment() {
    }


    public static DeptListFragment newInstance(String from,
                                               OnDepartmentSelectListener mListener) {
        DeptListFragment fragment = new DeptListFragment();
        fragment.setOnDepartmentListener(mListener);

        Bundle args = new Bundle();
        args.putString("from", from);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            from = getArguments().getString("from");
        }

        progressDialog = new ProgressDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dept_list, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        load();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void setOnDepartmentListener(OnDepartmentSelectListener mListener) {
        this.mListener = mListener;
    }

    void load() {
        progressDialog.showProgressDialog();
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("android/departments").child("list").addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        switch (dataSnapshot.getKey())
        {
            case "list":
                int count = (int) dataSnapshot.getChildrenCount();
                HashMap<String, EventListModel> deptMap = new HashMap<>(count);
                for (DataSnapshot department : dataSnapshot.getChildren()) {
                    EventListModel listModel = department.getValue(EventListModel.class);
                    deptMap.put(department.getKey(),listModel);
                }
                if (deptMap.isEmpty()){
                    onFailure();
                    return;
                }

                onSuccess(deptMap);
                break;
        }
    }

    private void onSuccess(HashMap<String, EventListModel> deptMap) {
        progressDialog.disMissProgressDialog();
        RecyclerView deptList = (RecyclerView) getActivity().findViewById(R.id.list_dept);

        deptList.setVisibility(View.VISIBLE);
        EventListAdapter eventListAdapter = new EventListAdapter(deptMap,getContext(),"dept");
        deptList.setAdapter(eventListAdapter);
        deptList.setHasFixedSize(true);
        deptList.setLayoutManager(new LinearLayoutManager(getContext()));


    }

    private void onFailure() {
        progressDialog.disMissProgressDialog();
        Import.snack(getView(),"No data available");
    }


    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void onButtonPressed(String deptId) {
        if (mListener != null) {
            mListener.onDepartmentSelect(deptId);
        }
    }

    public interface OnDepartmentSelectListener {
        void onDepartmentSelect(String deptId);
    }
}
