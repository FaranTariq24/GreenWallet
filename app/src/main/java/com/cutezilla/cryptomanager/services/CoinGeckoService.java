package com.cutezilla.cryptomanager.services;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cutezilla.cryptomanager.activity.LedgerEntryActivity;
import com.cutezilla.cryptomanager.model.Coin;
import com.cutezilla.cryptomanager.util.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoinGeckoService {

    public List<String> getCoinList(Context applicationContext){
         List<Coin> coinList= new ArrayList<>();
         List<String> coinSymbolList = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(applicationContext);
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
return coinSymbolList;
    }

    public void getVsCurrency(){

    }
    public void getPrice(String coin, String vsCurrency){

    }

}
