package com.cutezilla.cryptomanager.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cutezilla.cryptomanager.Interface.ItemClickListener;
import com.cutezilla.cryptomanager.R;
import com.cutezilla.cryptomanager.model.Account;
import com.cutezilla.cryptomanager.model.Coin;
import com.cutezilla.cryptomanager.model.Currency;
import com.cutezilla.cryptomanager.model.Ledger;
import com.cutezilla.cryptomanager.model.LedgerEntry;
import com.cutezilla.cryptomanager.services.QueryService;
import com.cutezilla.cryptomanager.util.Common;
import com.cutezilla.cryptomanager.viewHolder.LedgerViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

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

public class MainActivity extends AppCompatActivity   implements NavigationView.OnNavigationItemSelectedListener{
    Query mainCardQuery;
    FirebaseRecyclerOptions<Ledger> FR_options;
    static FirebaseRecyclerAdapter<Ledger, LedgerViewHolder> FR_adapter;
    LinearLayout  ll_header;
    FirebaseAuth mAuth;
    BaseActivity baseActivity;
    NavigationView navigationView;
    QueryService queryService = new QueryService();
    String  email;
    CardView cv_btn_buy, cv_btn_sell;
    LottieAnimationView ltv_loading;
    TextView tv_walletBalance,tv_wl_am_pkr;
    RecyclerView recyclerView;
    DecimalFormat percentageFormat = new DecimalFormat("00.0000");
    DecimalFormat percentageFormatD = new DecimalFormat("00.00");
    SweetAlertDialog progressBar2;
    ImageView iv_history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniUiComponents();
        headerComponents();
        if (Common.readFile(getApplicationContext())==null){
             progressBar2 = baseActivity.progressDialog(MainActivity.this, "Please wait", "Updating data....");
            fetchLocalCurrency();
        }

        ll_header.setVisibility(View.VISIBLE);
        navigationView.setNavigationItemSelectedListener(this);
        if (mAuth.getCurrentUser() != null) {
            email = mAuth.getCurrentUser().getEmail();
            Common.email = email;
            fetchNameAndGender(email);
        }
        recyclerView = (RecyclerView) findViewById(R.id.rc_mainCard);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

    }

    private void loadMainCardItems() {
        Common.LEDG_LIST = new ArrayList<>();
        mainCardQuery = FirebaseDatabase.getInstance().getReference(Common.STR_Ledger)
                .orderByChild("ledger_id").equalTo(Common.removeSpecialCharacter(Common.userAccount.getEmail()));
        FR_options = new FirebaseRecyclerOptions.Builder<Ledger>()
                .setQuery(mainCardQuery, Ledger.class)
                .build();
        FR_adapter = new FirebaseRecyclerAdapter<Ledger, LedgerViewHolder>(FR_options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull  LedgerViewHolder holder, int i, @NonNull Ledger ledger) {

                Common.LEDG_LIST.add(ledger);
                holder.setViewData(ledger);
                String dollar = percentageFormatD.format(ledger.getTotalInvested()+Float.parseFloat(tv_walletBalance.getText().toString()));
                tv_walletBalance.setText(dollar);
                tv_wl_am_pkr.setText(String.valueOf(Float.parseFloat(dollar)*160)+" RPS");

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Common.SELECTED_CURRENCY = FR_adapter.getRef(position).getKey();
                        Common.STR_SELECTED_LEDGER_INT = FR_adapter.getItem(position);
                        Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public LedgerViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.main_small_card, parent, false);
//                int height = parent.getMeasuredHeight() / 2;
//                itemView.setMinimumHeight(height);

                return new LedgerViewHolder(itemView,MainActivity.this);
            }
        };
        FR_adapter.startListening();
        recyclerView.setAdapter(FR_adapter);


    }
    // Common.writefile(new Gson().toJson(coinList),getApplicationContext());
    private void fetchLocalCurrency(){
        ltv_loading.setVisibility(View.VISIBLE);
         List<Coin> coinList= new ArrayList<>();
         List<String> coinSymbolList = new ArrayList<>();
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
                            Common.writefile(new Gson().toJson(coinList),getApplicationContext());
                            progressBar2.dismiss();
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
    private void iniUiComponents() {
        tv_wl_am_pkr = (TextView) findViewById(R.id.tv_wl_am_pkr);
        tv_walletBalance = (TextView) findViewById(R.id.tv_walletBalance);
        baseActivity = new BaseActivity();
        cv_btn_sell =  (CardView) findViewById(R.id.btn_sell);
        cv_btn_buy = (CardView) findViewById(R.id.btn_buy);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        ltv_loading = (LottieAnimationView) findViewById(R.id.lav_loading);
        mAuth = FirebaseAuth.getInstance();
        iv_history = findViewById(R.id.iv_history);
        iv_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AllHistoryActivity.class);
                startActivity(intent);
            }
        });

        cv_btn_buy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(Common.LOGTRACE, this.getClass()+ " Buy clicked " );
               Intent intent = new Intent(MainActivity.this,LedgerEntryActivity.class);
               startActivity(intent);
               finish();
            }
        });
        cv_btn_sell.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(Common.LOGTRACE, this.getClass()+ " Sell clicked " );
                Intent intent = new Intent(MainActivity.this,LedgerEntrySellActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void headerComponents() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        View backPressed = findViewById(R.id.back_btn);
        backPressed.setVisibility(View.GONE);
        View home = findViewById(R.id.imageviewHome);
        home.setVisibility(View.GONE);
        ll_header = findViewById(R.id.ll_head);
        ll_header.setVisibility(View.INVISIBLE);
        View navBtn = findViewById(R.id.iv_navbar);
        navBtn.setVisibility(View.VISIBLE);
        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
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
                mAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LandingPage.class);
                sweetAlertDialog.cancel();
                startActivity(intent);
                finish();
            }
        });
        sweetAlertDialog.show();

    }

    //Predefined code
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        item.setCheckable(false);
//        if (id == R.id.nav_profile) {
//            Intent intent = new Intent(this, ProfileSettingActivity.class);
//            startActivity(intent);
//        }
//        else
            if (id == R.id.nav_changepassword) {
            showChangePasswordDialog();
        }
            else if (id==R.id.nav_history){
                Intent intent = new Intent(MainActivity.this,AllHistoryActivity.class);
                startActivity(intent);
            }
//            else if (id == R.id.nav_addCurrency) {
//                showAddCurrencyPopup();
//            }
//            else if (id == R.id.nav_terms) {
//            Toast.makeText(this, "Terms and condition", Toast.LENGTH_SHORT).show();
//        }
//            else if (id == R.id.nav_feedback) {
//            Intent intent = new Intent(MainActivity.this, LedgerEntrySellActivity.class);
//            startActivity(intent);
//        }
//            else {
//                Toast.makeText(IndexActivity.this, "Device is already attached", Toast.LENGTH_SHORT).show();
//            }
//        } else if (id == R.id.nav_terms) {
//            Toast.makeText(this, "Terms and condition", Toast.LENGTH_SHORT).show();
//        }
////        else if (id == R.id.nav_faqs) {
////            Toast.makeText(this, "FAQs", Toast.LENGTH_SHORT).show();
////        }
//        else if (id == R.id.nav_about_us) {
//            Intent intent = new Intent(IndexActivity.this, AboutUsActivity.class);
//            startActivity(intent);
//        } else if (id == R.id.nav_feedback) {
////            Intent intent = new Intent(IndexActivity.this, CustomerFeedBackActivity.class);
//            Intent intent = new Intent(IndexActivity.this, RateAppActivity.class);
//            startActivity(intent);
//        } else if (id == R.id.nav_contact) {
//            Intent intent = new Intent(IndexActivity.this, ContactUs.class);
//            startActivity(intent);
//        }
        else if (id == R.id.nav_logout) {
            if (mAuth.getCurrentUser() != null) {
                logoutDialog();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showAddCurrencyPopup() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.add_currency_dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final EditText currencyName = (EditText) dialog.findViewById(R.id.et_addCurrencyName);

        ((AppCompatButton) dialog.findViewById(R.id.addcur_bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((AppCompatButton) dialog.findViewById(R.id.addcur_bt_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (currencyName.getText().toString().equals("")){
                   Toast.makeText(MainActivity.this,"Add currency Name",Toast.LENGTH_SHORT);
               }else{

                   FirebaseDatabase.getInstance().getReference(Common.STR_Currency)
                           .child(Objects.requireNonNull(FirebaseDatabase.getInstance().getReference(Common.STR_Currency).push().getKey()))
                           .setValue(new Currency(Common.userAccount.getEmail(),currencyName.getText().toString(),false))
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull  Task<Void> task) {
                                   baseActivity.sucessDialog(MainActivity.this, "Currency added successful", "Currency", null);
                                   dialog.dismiss();
                               }
                           });

               }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    private boolean validateBuyLedgerData(String buyDate, String currency, String buyPrice, String investedAmount) {
        boolean validated = true;
        if(buyDate.trim().isEmpty()){
            Toast.makeText(MainActivity.this, "Enter Buy date", Toast.LENGTH_SHORT).show();
            validated =false;
        }
        else if(currency.trim().isEmpty()){
            Toast.makeText(MainActivity.this, "select currency", Toast.LENGTH_SHORT).show();
            validated =false;
        }
        else if(buyPrice.trim().isEmpty()){
            Toast.makeText(MainActivity.this, "Enter Bought price", Toast.LENGTH_SHORT).show();
            validated =false;
        }
        else if(investedAmount.trim().isEmpty()){
            Toast.makeText(MainActivity.this, "Enter Buy date", Toast.LENGTH_SHORT).show();
            validated =false;
        }
        return validated;
    }


    private void showChangePasswordDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.change_password_dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final EditText oldPassword = (EditText) dialog.findViewById(R.id.et_oldPassword);
        final EditText newPassword = (EditText) dialog.findViewById(R.id.et_newpassword);
        final EditText confirmPassword = (EditText) dialog.findViewById(R.id.confirmPassword);
//        final CircularImageView civ_profile = (CircularImageView) dialog.findViewById(R.id.civ_dialog_avatar);
        final TextView tv_name = (TextView) dialog.findViewById(R.id.tv_name);
        final TextView tv_email = (TextView) dialog.findViewById(R.id.tv_email);


        tv_name.setText(Common.userAccount.getName());
        tv_email.setText(Common.userAccount.getEmail());

        ((AppCompatButton) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((AppCompatButton) dialog.findViewById(R.id.bt_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newPassword.getText().toString().trim().equals(confirmPassword.getText().toString().trim())
                        & !newPassword.getText().toString().trim().equals("")
                        & !newPassword.getText().toString().trim().isEmpty()
                        & !confirmPassword.getText().toString().trim().isEmpty()
                        & !confirmPassword.getText().toString().trim().equals("")
                        & !oldPassword.getText().toString().trim().isEmpty()) {
                    updatePasswordInDb(newPassword.getText().toString().trim(), oldPassword.getText().toString().trim());
                    dialog.dismiss();
                } else if (oldPassword.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter old password", Toast.LENGTH_SHORT).show();
                } else if (newPassword.getText().toString().trim().isEmpty()
                        || confirmPassword.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter new or confirm password", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Password must match confirm password", Toast.LENGTH_SHORT).show();
                    newPassword.setText("");
                    confirmPassword.setText("");
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
    private void updatePasswordInDb(final String newPassword, String oldPassword) {
        final SweetAlertDialog prgressDialog = baseActivity.progressDialog(MainActivity.this, "Please Wait", "Processing...");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(Common.userAccount.getEmail(), oldPassword);
        if (user != null) {
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    prgressDialog.cancel();
                                    baseActivity.sucessDialog(MainActivity.this, "Password change successful", "Successful", null);

                                } else {
                                    prgressDialog.cancel();
                                    baseActivity.errorDialog(MainActivity.this, "Failed to change password", "Failed");
                                }
                            }
                        });
                    } else {
                        prgressDialog.cancel();
                        baseActivity.errorDialog(MainActivity.this, "Old password is wrong", "Failed");
                    }
                }
            });

        }
    }
    private void fetchNameAndGender(String email) {
        ltv_loading.setVisibility(View.VISIBLE);
        Query accountQuery;
        if (email != null) {

//            query = FirebaseDatabase.getInstance().getReference(Common.STR_Account)
////                    .orderByChild("email")
//                    .equalTo(email);

            accountQuery = queryService.getAccount(email);
        } else {
            return;
        }

        accountQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot accountDataSnapShot : dataSnapshot.getChildren()) {
                    Account account = accountDataSnapShot.getValue(Account.class);

                    Common.userAccount = account;

                    if (account != null) {
                        //do something
                        ltv_loading.setVisibility(View.GONE);
                    }
                }
                loadMainCardItems();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().show();
                    }
                }
                ll_header.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Database Error: " + databaseError, Toast.LENGTH_SHORT).show();
                ltv_loading.setVisibility(View.GONE);
            }
        });
    }
    public void refreshActivity(){
        startActivity(getIntent());
    }
}