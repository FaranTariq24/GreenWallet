package com.cutezilla.cryptomanager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.cutezilla.cryptomanager.model.Ledger;
import com.cutezilla.cryptomanager.model.LedgerEntry;
import com.cutezilla.cryptomanager.services.CoinGeckoService;
import com.cutezilla.cryptomanager.services.QueryService;
import com.cutezilla.cryptomanager.util.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pl.utkala.searchablespinner.SearchableSpinner;

public class LedgerEntryActivity extends AppCompatActivity {

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yy-hh-mm");
    private SearchableSpinner coinSpinner, currencySpinner ;
    private Button bt_curr_max_btn,buyDate;
    private EditText et_buyprice,et_investedamount;
    private TextView tv_selectedcoin,tv_selectedCurrency,tv_selectedCurrencyPrice,tv_coin_vr;
    String selectedCoinId = null;
    String selectedCurrency = null;
    SweetAlertDialog progressBar;
    long date_ship_millis;
    Calendar calendar;
    private List<Coin> coinList= new ArrayList<>();
    private List<String> coinSymbolList = new ArrayList<>();
    private List<String> vsCurrencyList = new ArrayList<>();
    private AppCompatButton bt_cancel,bt_submit;
    BaseActivity baseActivity;
    DecimalFormat percentageFormat = new DecimalFormat("00.0000");
    QueryService queryService = new QueryService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        calendar = Calendar.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger_entry);
         baseActivity = new BaseActivity();
        progressBar = baseActivity.progressDialog(LedgerEntryActivity.this, "Please wait", "Fetching currency data....");
        intUiComponent();
        fetchLocalCurrency();
        fetchCompare();
    }
    private void intUiComponent() {
        et_investedamount = findViewById(R.id.et_investedamount);
        bt_cancel = findViewById(R.id.bt_cancel);
        bt_submit = findViewById(R.id.bt_submit);
        buyDate = (Button) findViewById(R.id.et_buy_date);
        bt_curr_max_btn = (Button) findViewById(R.id.bt_curr_max_btn);
        et_buyprice = (EditText) findViewById(R.id.et_buyprice);
        tv_selectedcoin = (TextView) findViewById(R.id.tv_selectedcoin);
        tv_selectedCurrency = (TextView) findViewById(R.id.tv_selectedCurrency);
        tv_selectedCurrencyPrice = (TextView) findViewById(R.id.tv_selectedCurrencyPrice);
        tv_coin_vr = (TextView) findViewById(R.id.tv_coin_vr);
        coinSpinner = (SearchableSpinner) findViewById(R.id.ss_coin_id);
        currencySpinner = (SearchableSpinner) findViewById(R.id.ss_currency_id);
        et_buyprice.requestFocus();
        buyDate.setText(dateFormat.format(calendar.getTime()));
        buyDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDatePickerLight((Button) v);
            }
        });
        bt_curr_max_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_buyprice.setText(tv_selectedCurrencyPrice.getText().toString());
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });
    }

    private void submitData() {
        if (buyDate.getText().toString().equals("") || coinSpinner.getSelectedItem().toString().equals("") || currencySpinner.getSelectedItem().toString().equals("")
             || et_buyprice.getText().toString().equals("") || et_investedamount.getText().toString().equals("")){
            Toast.makeText(LedgerEntryActivity.this,"Kindly fill the required fields",Toast.LENGTH_SHORT).show();
            return;
        }
        SweetAlertDialog progressBar2 = baseActivity.progressDialog(LedgerEntryActivity.this, "Please wait", "Updating data....");
        String strBuyDate,strCurrency,strBuyPrice,strInvestedAmount,strCryptoAmount;
        strBuyDate = buyDate.getText().toString();
        strCurrency = tv_coin_vr.getText().toString();
        strBuyPrice = et_buyprice.getText().toString();
        strInvestedAmount = et_investedamount.getText().toString();
        strCryptoAmount = String.valueOf(percentageFormat.format(Float.parseFloat(strInvestedAmount)/Float.parseFloat(strBuyPrice)));

        LedgerEntry LBE = new LedgerEntry();
        String ledgerBuyId = Common.createLedgerEntryId(strCurrency);
        LBE.setLedger_id(ledgerBuyId);
        LBE.setDate(strBuyDate);
        LBE.setCurrency(strCurrency);
        LBE.setCryptoAmount(Float.parseFloat(strCryptoAmount));
        LBE.setPrice(Float.parseFloat(strBuyPrice));
        LBE.setInvestedAmount(Float.parseFloat(strInvestedAmount));
        LBE.setStatus(Common.STR_BUY);

                FirebaseDatabase.getInstance().getReference(Common.STR_LedgerEntry)
                        .child(Objects.requireNonNull(FirebaseDatabase.getInstance().getReference(Common.STR_LedgerEntry).push().getKey()))
                        .setValue(LBE).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Ledger ledger = new Ledger();

                        Query ledgerQuery = queryService.getLedgerByLedgerId(ledgerBuyId);
                        ledgerQuery
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists()){
                                            queryService.getLedgerObjectByLedgerId(ledgerBuyId)
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull  DataSnapshot snapshot) {
                                                            Ledger ledger1 = new Ledger();
                                                            for(DataSnapshot dataSnapshot1: snapshot.getChildren()){
                                                                ledger1 = dataSnapshot1.getValue(Ledger.class);
                                                            }
                                                            if(ledger1!=null){


                                                                    ledger1.setTotalCryptoAmount(ledger1.getTotalCryptoAmount()+Float.parseFloat(strCryptoAmount));
                                                                    ledger1.setTotalInvested(ledger1.getTotalInvested()+LBE.getInvestedAmount());
                                                                    if (ledger1.getHighestBuyingPrice()<LBE.getPrice()){
                                                                        ledger1.setHighestBuyingPrice(LBE.getPrice());
                                                                    }
                                                                    else if (ledger1.getLowestBuyingPrice()>LBE.getPrice()){
                                                                        ledger1.setLowestBuyingPrice(LBE.getPrice());
                                                                    }


                                                                FirebaseDatabase.getInstance().getReference(Common.STR_Ledger)
                                                                        .child(ledgerBuyId)
                                                                        .setValue(ledger1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull  Task<Void> task) {
                                                                        if (task.isComplete()) {
                                                                            showSucessDialog();
                                                                        }
                                                                    }
                                                                });
                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });

                                        }else{


                                            ledger.setLedger_id(Common.removeSpecialCharacter(Common.userAccount.getEmail()));
                                            ledger.setLedgerEntry_id(ledgerBuyId);
                                            ledger.setCurrency_name(strCurrency);
                                            ledger.setHighestBuyingPrice(Float.parseFloat(strBuyPrice));
                                            ledger.setLowestBuyingPrice(Float.parseFloat(strBuyPrice));
                                            ledger.setTime(dateFormat2.format(calendar.getTime()).toString());
                                            ledger.setTotalInvested(Float.parseFloat(strInvestedAmount));
                                            ledger.setTotalCryptoAmount(Float.parseFloat(strCryptoAmount));

                                            FirebaseDatabase.getInstance().getReference(Common.STR_Ledger)
                                                    .child(ledgerBuyId)
                                                    .setValue(ledger).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull  Task<Void> task) {
                                                    showSucessDialog();

                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                    }
                });

    }

    private void showSucessDialog(){

        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("BUY");
        sweetAlertDialog.setContentText("Record updated");
        sweetAlertDialog.setConfirmText("Ok");
        sweetAlertDialog.setCanceledOnTouchOutside(false);
//        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                sweetAlertDialog.cancel();
//            }
//        });
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Intent intent = new Intent(LedgerEntryActivity.this,MainActivity.class);
                startActivity(intent);
                sweetAlertDialog.cancel();

            }
        });


        sweetAlertDialog.show();





    }
    private void fetchLocalCurrency(){
        try {
            String read = Common.readFile(getApplicationContext());
            JSONArray coinArray = new JSONArray(read);
            for (int i = 0; i < coinArray.length(); i++) {
                Coin coin = new Coin();
                coin.setName(coinArray.getJSONObject(i).get("name").toString());
                coin.setId(coinArray.getJSONObject(i).get("id").toString());
                coin.setSymbol(coinArray.getJSONObject(i).get("symbol").toString());
                coinSymbolList.add(coinArray.getJSONObject(i).get("symbol").toString());
                coinList.add(coin);


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
        }catch (Exception e){
            e.printStackTrace();
        }
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
        progressBar.dismiss();
    }
    private void dialogDatePickerLight(final Button bt) {
        Calendar cur_calender = Calendar.getInstance();

        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        date_ship_millis = calendar.getTimeInMillis();


//                        Toast.makeText(ProfileEditActivity.this, Tools.getFormattedDateSimple(date_ship_millis), Toast.LENGTH_SHORT).show();
                        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
                        bt.setText(date);

                    }

                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark light
        datePicker.setThemeDark(true);

        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
//        datePicker.setMinDate(cur_calender);
        datePicker.setMaxDate(cur_calender);
        datePicker.show(getFragmentManager(), "Datepickerdialog");
    }
}