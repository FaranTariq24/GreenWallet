package com.cutezilla.cryptomanager.viewHolder;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cutezilla.cryptomanager.Interface.ItemClickListener;
import com.cutezilla.cryptomanager.R;
import com.cutezilla.cryptomanager.model.LedgerEntry;

public class LedgerEntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ItemClickListener itemClickListener;
    View view;
    TextView status,time,maxBuy,minBuy,totalCur,totalInvested;
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public LedgerEntryViewHolder(@NonNull View itemView) {
        super(itemView);

        view = (View) itemView.findViewById(R.id.v_status);
        status = (TextView) itemView.findViewById(R.id.tv_status);
        time = (TextView) itemView.findViewById(R.id.tv_time);
        maxBuy = (TextView) itemView.findViewById(R.id.tv_maxBuyValue);
        minBuy = (TextView) itemView.findViewById(R.id.tv_minBuyValue);
        totalCur = (TextView) itemView.findViewById(R.id.tv_currencyAmount);
        totalInvested = (TextView) itemView.findViewById(R.id.tv_total_investedAmount);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition());
    }

    @SuppressLint("SetTextI18n")
    public void setViewData(LedgerEntry ledgerEntry) {
        status.setText(ledgerEntry.getStatus());
        time.setText(ledgerEntry.getDate());
        totalCur.setText(ledgerEntry.getCryptoAmount() +" " + ledgerEntry.getCurrency());
        totalInvested.setText(String.valueOf(ledgerEntry.getInvestedAmount())+"$");

    }
}
