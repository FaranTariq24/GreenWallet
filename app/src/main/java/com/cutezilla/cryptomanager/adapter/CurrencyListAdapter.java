package com.cutezilla.cryptomanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cutezilla.cryptomanager.R;
import com.cutezilla.cryptomanager.model.Coin;

import java.util.ArrayList;
import java.util.List;

public class CurrencyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Coin> items = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;


    public CurrencyListAdapter(Context context, List<Coin> items) {
        this.items = items;
        ctx = context;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, Coin obj, int position);
    }
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }
    public void AdapterListBasic(Context context, List<Coin> items) {
        this.items = items;
        ctx = context;
    }
    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_id;
        public TextView tv_name;
        public TextView tv_symbol;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            tv_id = (TextView) v.findViewById(R.id.tv_id);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            tv_symbol = (TextView) v.findViewById(R.id.tv_symbol);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coin, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            Coin coin = items.get(position);
            view.tv_name.setText(coin.getName());
            view.tv_id.setText(coin.getId());
            view.tv_symbol.setText(coin.getSymbol());
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
       return items.size();
    }
}
