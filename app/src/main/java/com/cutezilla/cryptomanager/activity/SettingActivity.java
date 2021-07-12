package com.cutezilla.cryptomanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cutezilla.cryptomanager.R;
import com.cutezilla.cryptomanager.application.MyApplication;
import com.cutezilla.cryptomanager.util.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.utkala.searchablespinner.SearchableSpinner;

public class SettingActivity extends AppCompatActivity {

    Map<String, String> myGlobalCurrencyMap;
    SearchableSpinner currencySpinner;
    List<String> curryList,idList,mainList;
    TextView tv_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        initLayout();
        fetchData();
    }

    private void initLayout() {
        currencySpinner = findViewById(R.id.ss_currency_id);
        tv_price = findViewById(R.id.tv_price);
    }

    private void fetchData() {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Common.CURRENCYNAMESURL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response!=null){
                            mainList = new ArrayList<>();
                            idList = new ArrayList<>();
                            curryList = new ArrayList<>();
                            Type type = new TypeToken<Map<String, String>>(){}.getType();
                            myGlobalCurrencyMap = new Gson().fromJson(response.toString(), type);
                            for (Map.Entry<String ,String> cr: myGlobalCurrencyMap.entrySet()){
                                idList.add(cr.getKey());
                                curryList.add(cr.getValue());
                                mainList.add(cr.getKey()+" -/- "+cr.getValue());
                            }
                            ArrayAdapter<String> adp1 = new ArrayAdapter<String>(SettingActivity.this,
                                    android.R.layout.simple_list_item_1, mainList);
                            adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            currencySpinner.setAdapter(adp1);


                            currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                   Toast.makeText(SettingActivity.this,idList.get(position),Toast.LENGTH_SHORT).show();
                                   fetchSelectedPrice(position);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            currencySpinner.setDialogTitle("Select currency");
                            currencySpinner.setDismissText("Cancel");
                            currencySpinner.setSelection(Common.SET_SELECTED_CURRENCY);



                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SettingActivity.this,"Failed to fetch currency",Toast.LENGTH_SHORT).show();
            }
        });
                MyApplication.getInstance().addToRequestQueue(request);
    }

    private void fetchSelectedPrice(int position){
        Common.SET_SELECTED_CURRENCY = position;

        String slctUrl = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/usd/"+idList.get(position)+".json";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                slctUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response!=null){
                            Type type = new TypeToken<Map<String, String>>(){}.getType();
                            Map<String, String> myC = new Gson().fromJson(response.toString(), type);
                            for (Map.Entry<String,String> c: myC.entrySet()){
                                if (!c.getKey().equals("date")){
                                    tv_price.setText(c.getValue());
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MyApplication.getInstance().addToRequestQueue(request);
    }
}