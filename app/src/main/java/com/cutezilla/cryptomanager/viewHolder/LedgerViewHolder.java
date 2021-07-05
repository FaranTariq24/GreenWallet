package com.cutezilla.cryptomanager.viewHolder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cutezilla.cryptomanager.Interface.ItemClickListener;
import com.cutezilla.cryptomanager.R;
import com.cutezilla.cryptomanager.activity.BaseActivity;
import com.cutezilla.cryptomanager.activity.MainActivity;
import com.cutezilla.cryptomanager.model.Ledger;

import java.text.DecimalFormat;

public class LedgerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ItemClickListener itemClickListener;
    TextView currName,time,maxBuy,minBuy,totalCur,totalInvested;
    ImageView iv_delete;
    DecimalFormat percentageFormat = new DecimalFormat("00.0000");
    DecimalFormat percentageFormatD = new DecimalFormat("00.00");
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public LedgerViewHolder(@NonNull View itemView, Context ct) {
        super(itemView);
        itemView.setOnClickListener(this);
        iv_delete = itemView.findViewById(R.id.iv_delete);
        currName = (TextView) itemView.findViewById(R.id.tv_msc_curr_name);
        time = (TextView) itemView.findViewById(R.id.tv_time);
        maxBuy = (TextView) itemView.findViewById(R.id.tv_maxBuyValue);
        minBuy = (TextView) itemView.findViewById(R.id.tv_minBuyValue);
        totalCur = (TextView) itemView.findViewById(R.id.tv_total_Currency);
        totalInvested = (TextView) itemView.findViewById(R.id.tv_total_investedAmount);
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ct,String.valueOf(getAdapterPosition()),Toast.LENGTH_SHORT).show();
                BaseActivity baseActivity = new BaseActivity();
                baseActivity.deleteLedger(ct,getAdapterPosition());

            }
        });

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition());
    }
    @SuppressLint("SetTextI18n")
    public void setViewData(Ledger ledger){
        currName.setText(ledger.getCurrency_name());
        time.setText(ledger.getTime());
        maxBuy.setText(String.valueOf(ledger.getHighestBuyingPrice()));
        minBuy.setText(String.valueOf(ledger.getLowestBuyingPrice()));
        totalInvested.setText(percentageFormatD.format(ledger.getTotalInvested())+"$");
        totalCur.setText(percentageFormat.format(ledger.getTotalCryptoAmount()));

    }
}
