package com.cutezilla.cryptomanager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cutezilla.cryptomanager.R;
import com.cutezilla.cryptomanager.adapter.CurrencyListAdapter;
import com.cutezilla.cryptomanager.model.Coin;
import com.cutezilla.cryptomanager.util.Common;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.utkala.searchablespinner.SearchableSpinner;

public class AdminActivity extends AppCompatActivity {

    Button button;


    private RecyclerView recyclerView;
    private CurrencyListAdapter mAdapter;

    private List<Coin> coins= new ArrayList<>();
    private SearchableSpinner ss ;
    private List<String> coinSymbols = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        initComponent();
        fetchCurrency();


    }

    private void initComponent() {
        ss = (SearchableSpinner) findViewById(R.id.ss_admin_id);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCurrency();
            }
        });
    }

    private void fetchCurrency() {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.GET, Common.ConiFeckoGETLISTURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("HttpClient", "success! response: " + response.toString());
//                        Log.i("VOLLEY", response);
                        JSONObject jsonResponse = null;
                        try {
                            JSONArray coinArray = new JSONArray(response);
                            for (int i = 0; i < coinArray.length(); i++) {
                                // create a JSONObject for fetching single user data
                                Coin coin = new Coin();
                                JSONObject coinDetails=coinArray.getJSONObject(i);
                                coin.setName(coinArray.getJSONObject(i).get("name").toString());
                                coin.setId(coinArray.getJSONObject(i).get("id").toString());
                                coin.setSymbol(coinArray.getJSONObject(i).get("symbol").toString());
                                if (coinArray.getJSONObject(i).getJSONObject("platforms").has("binance-smart-chain")
                                            || coinArray.getJSONObject(i).getJSONObject("platforms").has("binancecoin")){
                                    coinSymbols.add(coinArray.getJSONObject(i).get("symbol").toString());
                                }

                                coins.add(coin);

                            }
                            Toast.makeText(AdminActivity.this,"Completed"+coins.get(0).getName(),Toast.LENGTH_SHORT).show();
                            //call adapter
                            mAdapter = new CurrencyListAdapter(AdminActivity.this, coins);
                            recyclerView.setAdapter(mAdapter);

                            // on item list clicked
                            mAdapter.setOnItemClickListener(new CurrencyListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, Coin obj, int position) {
                                    Toast.makeText(AdminActivity.this,obj.getName(),Toast.LENGTH_SHORT).show();
                                }
                            });



                            ArrayAdapter<String> adp1 = new ArrayAdapter<String>(AdminActivity.this,
                                    android.R.layout.simple_list_item_1, coinSymbols);
                            adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            ss.setAdapter(adp1);
                            ss.setAdapter(adp1);
                            ss.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Toast.makeText(AdminActivity.this,coinSymbols.get(position),Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            ss.setDialogTitle("Select currency");
                            ss.setDismissText("Cancel");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HttpClient", "error: " + error.toString());
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("usd","YOUR USERNAME");
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }
}