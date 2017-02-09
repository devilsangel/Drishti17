package com.drishti.drishti17.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.drishti.drishti17.R;
import com.drishti.drishti17.network.models.EventListModel;
import com.drishti.drishti17.ui.adapters.EventListAdapter;
import com.drishti.drishti17.ui.factory.ProgressDialog;
import com.drishti.drishti17.util.GuillotineUtil;
import com.drishti.drishti17.util.Import;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventList extends AppCompatActivity implements GuillotineUtil.OnNavigationClickListener, ValueEventListener {

    private static final long RIPPLE_DURATION = 250;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.root)
    FrameLayout root;
    @BindView(R.id.content_hamburger)
    View contentHamburger;
    String dept;
    String fpath;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        ButterKnife.bind(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }
        getArguments();
        new GuillotineUtil(this).setUpNav(root, findViewById(R.id.content_hamburger), toolbar, this);

        progressDialog = new ProgressDialog(this);
        load();
    }

    private void getArguments() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            dept = bundle.getString("dept");
            fpath = "dept-events/"+dept+"/list";
        }
    }

    @Override
    public void onNavigationClick(int id) {
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        String text ="";
        switch (id) {
            case R.id.competitions_group:
                text = getString(R.string.competitions);
                break;
            case R.id.workshops_group:
                text = getString(R.string.workshops);
                break;
            case R.id.informals_group:
                text = getString(R.string.informals);
                break;
        }

        title.setText(text);
    }

    void load() {
        progressDialog.showProgressDialog();
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child(fpath).addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        switch (dataSnapshot.getKey())
        {
            case "list":
                int count = (int) dataSnapshot.getChildrenCount();
                HashMap<String, EventListModel> deptMap = new HashMap<>(count);
                for (DataSnapshot event : dataSnapshot.getChildren()) {
                    EventListModel listModel = event.getValue(EventListModel.class);
                    deptMap.put(event.getKey(),listModel);
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
        RecyclerView deptList = (RecyclerView)findViewById(R.id.list_events);

        deptList.setVisibility(View.VISIBLE);
        EventListAdapter eventListAdapter = new EventListAdapter(deptMap,this,"eventlist");
        deptList.setAdapter(eventListAdapter);
        deptList.setHasFixedSize(true);
        deptList.setLayoutManager(new LinearLayoutManager(this));


    }

    private void onFailure() {
        progressDialog.disMissProgressDialog();
        Import.snack(root,"No data available. Try for computer science");
    }


    @Override
    public void onCancelled(DatabaseError databaseError) {
        progressDialog.disMissProgressDialog();
        Import.toast(this,"Cancelled event "+databaseError.getMessage());
    }
}
