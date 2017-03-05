package com.drishti.drishti17.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.drishti.drishti17.R;
import com.drishti.drishti17.network.models.College;
import com.drishti.drishti17.network.models.Student;
import com.drishti.drishti17.ui.factory.ProgressDialog;
import com.drishti.drishti17.util.ApiClient;
import com.drishti.drishti17.util.ApiInterface;
import com.drishti.drishti17.util.AuthUtil;
import com.drishti.drishti17.util.Global;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

public class MainRegister extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.gender) MaterialSpinner gender;
    @BindView(R.id.cname)AutoCompleteTextView collegeName;
    @BindView(R.id.pnumber)EditText pNumber;
    @BindView(R.id.acc)RadioGroup accomodation;
    ProgressDialog progressDialog;
    HashMap<String,Integer> searchList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);
        ButterKnife.bind(this);
        gender.setItems("Select Gender","Male","Female");
        getSupportActionBar().setElevation(0);
        findViewById(R.id.subreg).setOnClickListener(this);
        findViewById(R.id.skip).setOnClickListener(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.showProgressDialog();
        ApiInterface service =ApiClient.getService();
        Call<List<College>> call=service.getAllColleges();
        call.enqueue(new retrofit2.Callback<List<College>>() {
            @Override
            public void onResponse(Call<List<College>> call, retrofit2.Response<List<College>> response) {
                progressDialog.disMissProgressDialog();
                ArrayList<College> cList= (ArrayList<College>) response.body();
                ArrayList<String> cnames=new ArrayList<String>();
                searchList=new HashMap<String, Integer>();
                for(College c: cList){
                    cnames.add(c.name);
                    searchList.put(c.name,c.id);
                }
                ArrayAdapter<String> list=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,cnames);
                collegeName.setDropDownBackgroundResource(R.color.colorPrimary);
                collegeName.setAdapter(list);
                collegeName.setThreshold(0);
            }

            @Override
            public void onFailure(Call<List<College>> call, Throwable t) {
                progressDialog.disMissProgressDialog();
            }
        });
    }
    public void submit(){
        final String college =collegeName.getText().toString();
        if(searchList.get(college)==null){
            collegeName.setError("Select College from flipList or choose others");
            return;
        }
        final String number=pNumber.getText().toString();
        if(number.length()!=10){
            pNumber.setError("Enter a valid number");
            return;
        }
        final int gen=gender.getSelectedIndex();
        if(gen==0){
            gender.setError("select gender");
            return;
        }
        final ApiInterface service =ApiClient.getService();
        Student.Accomodation a= Student.Accomodation.none;
        if(accomodation.getId()==R.id.accyes){
            if(gen==1)
                a= Student.Accomodation.male;
            else
                a= Student.Accomodation.female;
        }
        final Student.Accomodation finalA = a;
        AuthUtil.getFirebaseToken(new AuthUtil.Listener() {
            @Override
            public void tokenObtained(String token) {
                service.register(token,number, finalA,searchList.get(college)).enqueue(new retrofit2.Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_SHORT);
                        Global.college=college;
                        Global.isguest=false;
                        startActivity(new Intent(MainRegister.this,Home.class));
                        finish();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
    }
    @Override
    public void onClick(View v) {
        Log.d("clicked","clicked");
        switch (v.getId()){
            case R.id.subreg:
                submit();
                break;
            case R.id.skip:
                startActivity(new Intent(this,Home.class));
                break;
        }
    }
}
