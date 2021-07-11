package com.cutezilla.cryptomanager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cutezilla.cryptomanager.R;
import com.cutezilla.cryptomanager.model.Ledger;
import com.cutezilla.cryptomanager.model.LedgerEntry;
import com.cutezilla.cryptomanager.util.Common;
import com.cutezilla.cryptomanager.viewHolder.LedgerEntryViewHolder;
import com.cutezilla.cryptomanager.viewHolder.LedgerViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AllHistoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<LedgerEntry> FR_options;
    static FirebaseRecyclerAdapter<LedgerEntry, LedgerEntryViewHolder> FR_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_history);

        initComponent();
        loadCardItems();
        headerComponents();
    }



    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.rc_all_history);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void loadCardItems() {
        Query mainQuery= FirebaseDatabase.getInstance().getReference(Common.STR_LedgerEntry)
                .orderByChild("email").equalTo(Common.email);
        FR_options = new FirebaseRecyclerOptions.Builder<LedgerEntry>()
                .setQuery(mainQuery, LedgerEntry.class)
                .build();
        FR_adapter = new FirebaseRecyclerAdapter<LedgerEntry, LedgerEntryViewHolder>(FR_options) {
            @Override
            protected void onBindViewHolder(@NonNull  LedgerEntryViewHolder holder, int i, @NonNull  LedgerEntry ledgerEntry) {
                holder.setViewData(ledgerEntry);
            }

            @NonNull
            @Override
            public LedgerEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.history_small_card, parent, false);
                return new LedgerEntryViewHolder(itemView,AllHistoryActivity.this,false);
            }
        };
        FR_adapter.startListening();
        recyclerView.setAdapter(FR_adapter);
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
                Intent intent = new Intent(AllHistoryActivity.this, LandingPage.class);
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
                Intent intent = new Intent(AllHistoryActivity.this, LandingPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                sweetAlertDialog.cancel();
                startActivity(intent);
                finish();
            }
        });
        sweetAlertDialog.show();

    }
}