package com.cutezilla.cryptomanager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cutezilla.cryptomanager.Interface.ItemClickListener;
import com.cutezilla.cryptomanager.R;
import com.cutezilla.cryptomanager.model.Ledger;
import com.cutezilla.cryptomanager.model.LedgerEntry;
import com.cutezilla.cryptomanager.util.Common;
import com.cutezilla.cryptomanager.viewHolder.LedgerEntryViewHolder;
import com.cutezilla.cryptomanager.viewHolder.LedgerViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HistoryActivity extends AppCompatActivity {
    Query mainCardQuery;
    FirebaseRecyclerOptions<LedgerEntry> FR_options;
    static FirebaseRecyclerAdapter<LedgerEntry, LedgerEntryViewHolder> FR_adapter;
    RecyclerView recyclerView;
    TextView tv_crr_name,tv_walletBalance,tv_inv_amount,tv_wl_am_pkr;
    DecimalFormat percentageFormat = new DecimalFormat("00.0000");
    DecimalFormat percentageFormatD = new DecimalFormat("00.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Common.isReversed = false;
        Common.LEDG_ENTRY_LIST = new ArrayList<>();
        initUiComponent();
        loadCardItem();
        headerComponents();
        populateUiComponent();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(HistoryActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void refreshActivity(){
        startActivity(getIntent());
    }
    private void populateUiComponent() {

        tv_inv_amount.setText(String.valueOf(percentageFormatD.format(Common.STR_SELECTED_LEDGER_INT.getTotalInvested())));
        tv_walletBalance.setText(String.valueOf(percentageFormat.format(Common.STR_SELECTED_LEDGER_INT.getTotalCryptoAmount())));
        tv_wl_am_pkr.setText(percentageFormatD.format(Common.STR_SELECTED_LEDGER_INT.getTotalInvested()*160)+" RPS");
    }

    private void initUiComponent() {
        tv_wl_am_pkr = (TextView) findViewById(R.id.tv_wl_am_pkr);
        tv_inv_amount = (TextView) findViewById(R.id.tv_inv_amount);
        tv_walletBalance = (TextView) findViewById(R.id.tv_walletBalance);
        tv_crr_name = (TextView) findViewById(R.id.tv_crr_name);
        recyclerView = (RecyclerView) findViewById(R.id.rc_hsCard);
        recyclerView.setHasFixedSize(true);

//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
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
                Intent intent = new Intent(HistoryActivity.this, LandingPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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
                Intent intent = new Intent(HistoryActivity.this, LandingPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                sweetAlertDialog.cancel();
                startActivity(intent);
                finish();
            }
        });
        sweetAlertDialog.show();

    }
    private void loadCardItem() {
        mainCardQuery = FirebaseDatabase.getInstance().getReference(Common.STR_LedgerEntry)
                .orderByChild("ledger_id").equalTo(Common.SELECTED_CURRENCY);
        loadStaticList(mainCardQuery);


        FR_options = new FirebaseRecyclerOptions.Builder<LedgerEntry>()
                .setQuery(mainCardQuery, LedgerEntry.class)
                .build();

        FR_adapter = new FirebaseRecyclerAdapter<LedgerEntry, LedgerEntryViewHolder>(FR_options){
            @NonNull
            @Override
            public LedgerEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.history_small_card, parent, false);
                return new LedgerEntryViewHolder(itemView,HistoryActivity.this,true);
            }


            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull LedgerEntryViewHolder holder, int position, @NonNull LedgerEntry ledgerEntry) {
                holder.setViewData(ledgerEntry);

                tv_crr_name.setText(ledgerEntry.getCurrency());

                holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(HistoryActivity.this, SweetAlertDialog.WARNING_TYPE);
                        sweetAlertDialog.setCanceledOnTouchOutside(false);
                        sweetAlertDialog.setTitleText("Delete");
                        sweetAlertDialog.setContentText("Are you sure you want to Delete?");
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
//                                Toast.makeText(HistoryActivity.this,String.valueOf(ledgerEntry.getInvestedAmount()),Toast.LENGTH_SHORT).show();
                                FR_adapter.getRef(position).removeValue();
                                FR_adapter .notifyItemRemoved(position);
                                FR_adapter .notifyDataSetChanged();
                                updateLedger(sweetAlertDialog,ledgerEntry);
                            }
                        });

                        sweetAlertDialog.show();


                    }
                });

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Toast.makeText(HistoryActivity.this,FR_adapter.getRef(position).getKey(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        };
        FR_adapter.startListening();
        recyclerView.setAdapter(FR_adapter);
    }
    private void updateLedger(SweetAlertDialog sweetAlertDialog, LedgerEntry lbe){
        Ledger updatedLedger = new Ledger();
        for (Ledger ld: Common.LEDG_LIST){
            if (ld.getLedgerEntry_id().equals(lbe.getLedger_id())){
                if (lbe.getStatus().equals(Common.STR_BUY)){
                    ld.setTotalCryptoAmount(ld.getTotalCryptoAmount()-lbe.getCryptoAmount());
                    ld.setTotalInvested(ld.getTotalInvested()-lbe.getInvestedAmount());
                }else if (lbe.getStatus().equals(Common.STR_SELL)){
                    ld.setTotalCryptoAmount(ld.getTotalCryptoAmount()+lbe.getCryptoAmount());
                    ld.setTotalInvested(ld.getTotalInvested()+lbe.getInvestedAmount());
                }
                updatedLedger = ld;
            }
        }
        FirebaseDatabase.getInstance().getReference(Common.STR_Ledger)
                .child(lbe.getLedger_id())
                .setValue(updatedLedger)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(HistoryActivity.this,"Ledger updated",Toast.LENGTH_SHORT).show();
                        sweetAlertDialog.dismiss();
                        HistoryActivity.this.refreshActivity();
                        HistoryActivity.this.finish();
                    }
                });
    }
    private void loadStaticList(Query mainCardQuery) {
        mainCardQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                assert Common.LEDG_ENTRY_LIST != null;
                if (  Common.LEDG_ENTRY_LIST.size()>0){
                    Common.LEDG_ENTRY_LIST.clear();
                }

                for (DataSnapshot snap: snapshot.getChildren()){
                    Common.LEDG_ENTRY_LIST.add(snap.getValue(LedgerEntry.class));
                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }
}