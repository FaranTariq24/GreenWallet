package com.cutezilla.cryptomanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
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
import com.cutezilla.cryptomanager.services.CoinGeckoService;
import com.cutezilla.cryptomanager.util.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.utkala.searchablespinner.SearchableSpinner;

public class LedgerEntryActivity extends AppCompatActivity {

    private SearchableSpinner coinSpinner, currencySpinner ;
    private TextView tv_selectedcoin,tv_selectedCurrency,tv_selectedCurrencyPrice,tv_coin_vr;
    String selectedCoinId = null;
    String selectedCurrency = null;
    private List<Coin> coinList= new ArrayList<>();
    private List<String> coinSymbolList = new ArrayList<>();
    private List<String> vsCurrencyList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger_entry);

        intUiComponent();
        fetchCurrency();
        fetchCompare();
    }
    private void intUiComponent() {
        tv_selectedcoin = (TextView) findViewById(R.id.tv_selectedcoin);
        tv_selectedCurrency = (TextView) findViewById(R.id.tv_selectedCurrency);
        tv_selectedCurrencyPrice = (TextView) findViewById(R.id.tv_selectedCurrencyPrice);
        tv_coin_vr = (TextView) findViewById(R.id.tv_coin_vr);
        coinSpinner = (SearchableSpinner) findViewById(R.id.ss_coin_id);
        currencySpinner = (SearchableSpinner) findViewById(R.id.ss_currency_id);
    }
    private void fetchCurrency() {
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.GET, Common.ConiFeckoGETLISTURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("HttpClient", "success! response: " + response.toString());
//                        Log.i("VOLLEY", response);
                        try {
                            JSONArray coinArray = new JSONArray(response);
                            for (int i = 0; i < coinArray.length(); i++) {
                                Coin coin = new Coin();
                                coin.setName(coinArray.getJSONObject(i).get("name").toString());
                                coin.setId(coinArray.getJSONObject(i).get("id").toString());
                                coin.setSymbol(coinArray.getJSONObject(i).get("symbol").toString());
                                if (coinArray.getJSONObject(i).getJSONObject("platforms").has("binance-smart-chain")
                                        || coinArray.getJSONObject(i).getJSONObject("platforms").has("binancecoin")
                                            || coinArray.getJSONObject(i).get("symbol").equals("btc")){
                                    coinSymbolList.add(coinArray.getJSONObject(i).get("symbol").toString());
                                    coinList.add(coin);
                                }

                            }
                            ArrayAdapter<String> adp1 = new ArrayAdapter<String>(LedgerEntryActivity.this,
                                    android.R.layout.simple_list_item_1, coinSymbolList);
                            adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            coinSpinner.setAdapter(adp1);
                            coinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    tv_selectedcoin.setText(coinSymbolList.get(position));
                                    tv_coin_vr.setText(tv_selectedcoin.getText().toString().toUpperCase()+"/"+tv_selectedCurrency.getText().toString().toUpperCase());
                                    if (!tv_selectedcoin.getText().toString().equals("") & !tv_selectedCurrency.getText().toString().equals("")){
                                        fetchMarketPrice(tv_selectedcoin.getText().toString(),tv_selectedCurrency.getText().toString());
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            coinSpinner.setDialogTitle("Select currency");
                            coinSpinner.setDismissText("Cancel");
                            coinSpinner.setSelection(3);

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

    private void fetchCompare() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.GET, Common.ConiFeckoGETVSCURRENCY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("HttpClient", "success! response: " + response.toString());
//                        Log.i("VOLLEY", response);
                        try {
                            JSONArray coinArray = new JSONArray(response);
                            for (int i = 0; i < coinArray.length(); i++) {

                                if (coinArray.get(i)!=null){
                                    vsCurrencyList.add(coinArray.get(i).toString());
                                }
                            }

                            ArrayAdapter<String> adp1 = new ArrayAdapter<String>(LedgerEntryActivity.this,
                                    android.R.layout.simple_list_item_1, vsCurrencyList);
                            adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            currencySpinner.setAdapter(adp1);
                            currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    tv_selectedCurrency.setText(vsCurrencyList.get(position));
                                    tv_coin_vr.setText(tv_selectedcoin.getText().toString().toUpperCase()+"/"+tv_selectedCurrency.getText().toString().toUpperCase());
                                    if (!tv_selectedcoin.getText().toString().equals("") & !tv_selectedCurrency.getText().toString().equals("")){
                                        fetchMarketPrice(tv_selectedcoin.getText().toString(),tv_selectedCurrency.getText().toString());
                                    }

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            currencySpinner.setDialogTitle("Select currency");
                            currencySpinner.setDismissText("Cancel");
                            currencySpinner.setSelection(11);

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

    private void fetchMarketPrice(String coin, String currency){

        selectedCurrency = currency;
        Coin selectedCoin = new Coin();
        for (Coin co:coinList){
            if (coin.equals(co.getSymbol())){
                selectedCoin = co;
                selectedCoinId = co.getId();
            }
        }
        String url = "https://api.coingecko.com/api/v3/simple/price?ids="+selectedCoinId+"&vs_currencies="+currency;

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("HttpClient", "success! response: " + response.toString());
//                        Log.i("VOLLEY", response);
                        try {
                            updateDollar(new JSONObject(response));
                        } catch (Exception e) {
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
    private void updateDollar(JSONObject jsonObject) throws JSONException {
        tv_selectedCurrencyPrice.setText( jsonObject.getJSONObject(selectedCoinId).get(selectedCurrency).toString());
    }

}