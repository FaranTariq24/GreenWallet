package com.cutezilla.cryptomanager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

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
}