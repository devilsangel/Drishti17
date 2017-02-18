package com.drishti.drishti17.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.drishti.drishti17.R;
import com.drishti.drishti17.network.models.DepartmentModel;
import com.drishti.drishti17.ui.adapters.DepartmentListAdapter;
import com.drishti.drishti17.ui.factory.ProgressDialog;
import com.drishti.drishti17.ui.fragments.DeptListFragment;
import com.drishti.drishti17.util.NavUtil;
import com.drishti.drishti17.util.UIUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Departments extends AppCompatActivity implements DeptListFragment.OnDepartmentSelectListener, View.OnClickListener {

    ProgressDialog progressDialog;
    private static final String TAG = Departments.class.getSimpleName();
    @BindView(R.id.fab)  FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        UIUtil.setToolBar(this,"Events");

        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);

        fab.setOnClickListener(this);

        loadDepartments();

    }

    private void loadDepartments() {
        progressDialog.showProgressDialog();

        String[] deptNames = getResources().getStringArray(R.array.dept);
        String[] deptKeys = getResources().getStringArray(R.array.dept_keys);
        String[] deptPic = getResources().getStringArray(R.array.dept_images);

        HashMap<Integer,DepartmentModel> deptMap = new HashMap<>(deptNames.length);
        for (int i = 0; i < deptNames.length; i++) {
            DepartmentModel deptModel = new DepartmentModel(deptKeys[i],deptNames[i],deptPic[i]);
            deptMap.put(i,deptModel);
        }

        progressDialog.disMissProgressDialog();
        RecyclerView deptList = (RecyclerView) findViewById(R.id.list_dept);

        deptList.setVisibility(View.VISIBLE);
        DepartmentListAdapter departmentListAdapter = new DepartmentListAdapter(deptMap,this);
        deptList.setAdapter(departmentListAdapter);
        deptList.setHasFixedSize(true);
        deptList.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public void onDepartmentSelect(String deptId) {
        Log.d(TAG, "onDepartmentSelect: department clicked "+deptId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                NavUtil.openNavigation(this,this,fab);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (NavUtil.isFromNav(requestCode)) {
            NavUtil.handleNavigation(this,this,resultCode);
        }
    }


     /* private void setUpFragmentUi() {

        Log.d(TAG, "setUpFragmentUi: setting up fragments");
        Fragment inputMobile = fragmentManager.findFragmentByTag("dept");
        if ((inputMobile == null) || !inputMobile.isAdded()) {
            Fragment deptListFragment = DeptListFragment.newInstance("events",this);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.content_events, deptListFragment, "dept");
            fragmentTransaction.commit();
        } else {
            Log.d(TAG, "setUpFragmentUi: some fragment already present");
        }

    }*/
}
