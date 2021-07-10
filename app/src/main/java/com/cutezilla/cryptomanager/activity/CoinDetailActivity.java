package com.cutezilla.cryptomanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cutezilla.cryptomanager.R;
import com.cutezilla.cryptomanager.application.MyApplication;
import com.cutezilla.cryptomanager.model.AllCoin;
import com.cutezilla.cryptomanager.model.CoinDetail;
import com.cutezilla.cryptomanager.util.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CoinDetailActivity extends AppCompatActivity {


    private String URL = "https://api.coingecko.com/api/v3/coins/"+Common.SELECTED_COIN_DES.getId()+"?tickers=false&market_data=false&community_data=false&developer_data=false&sparkline=false";
    private List<CoinDetail> contactList = new ArrayList<>();
    TextView tv_name,tb_symbol,tv_currentPrice,tv_totalVolume,tv_marketCapRank,tv_marketCap,tv_des;
    WebView simpleWebView;
    ImageView im_image;
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_detail);
        initLayout();
        headerComponents();
        updateLayout();
        fetchCoinDetails();
    }
    private void headerComponents() {

        View backPressed = findViewById(R.id.back_btn);
        backPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        View homeScreen = findViewById(R.id.imageviewHome);
        homeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoinDetailActivity.this, LandingPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                finish();
            }
        });
        View logout = findViewById(R.id.powerImage);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();
            }
        });
    }
    private void updateLayout() {
        tv_currentPrice.setText(String.valueOf(Common.SELECTED_COIN_DES.getCurrent_price()));
        tv_name.setText(String.valueOf(Common.SELECTED_COIN_DES.getName()));
        tb_symbol.setText(String.valueOf(Common.SELECTED_COIN_DES.getSymbol()));
        tv_totalVolume.setText(formatter.format(Common.SELECTED_COIN_DES.getTotal_volume()));
        tv_marketCapRank.setText(formatter.format(Common.SELECTED_COIN_DES.getMarket_cap_rank()));
        tv_marketCap.setText(formatter.format(Common.SELECTED_COIN_DES.getMarket_cap()));
        Glide.with(this)
                .load(Common.SELECTED_COIN_DES.getImage())
                .apply(RequestOptions.circleCropTransform())
                .into(im_image);

    }

    private void initLayout() {
        tv_currentPrice = findViewById(R.id.tv_currentPrice);
        tv_name = findViewById(R.id.tv_name);
        tb_symbol = findViewById(R.id.tb_symbol);
        tv_totalVolume = findViewById(R.id.tv_totalVolume);
        tv_marketCapRank = findViewById(R.id.tv_marketCapRank);
        tv_marketCap = findViewById(R.id.tv_marketCap);
        simpleWebView = findViewById(R.id.simpleWebView);
        im_image = findViewById(R.id.im_image);
    }

    private void fetchCoinDetails(){
    JsonObjectRequest request = new JsonObjectRequest(
            Request.Method.GET,
            URL,
            null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    CoinDetail cd = new Gson().fromJson(response.toString(), CoinDetail.class);
                    simpleWebView.loadDataWithBaseURL(null, cd.getDescription().getEn(), "text/html", "utf-8", null);
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    });
    MyApplication.getInstance().addToRequestQueue(request);
}

    private void saveData(String result) {
        try {
            JSONObject json= (JSONObject) new JSONTokener(result).nextValue();
            JSONObject json2 = json.getJSONObject("results");
            String test = (String) json2.get("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void logoutDialog() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.setTitleText("Logout");
        sweetAlertDialog.setContentText("Are you sure you want to Logout?");
        sweetAlertDialog.setConfirmText("Yes");
        sweetAlertDialog.setCancelText("No");
        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
            }
        });
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CoinDetailActivity.this, LandingPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                sweetAlertDialog.cancel();
                startActivity(intent);
                finish();
            }
        });
        sweetAlertDialog.show();

    }

}