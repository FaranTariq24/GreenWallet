package com.cutezilla.cryptomanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cutezilla.cryptomanager.R;
import com.cutezilla.cryptomanager.model.Currency;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

public class CurrencyAdapter  extends FirebaseRecyclerAdapter<Currency,CurrencyAdapter.myCurrencyviewholder> {


    public CurrencyAdapter(@NonNull  FirebaseRecyclerOptions<Currency> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull  CurrencyAdapter.myCurrencyviewholder holder, int i, @NonNull  Currency currency) {
        holder.name.setText(currency.getName());
    }

    @NonNull
    @Override
    public myCurrencyviewholder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myCurrencyviewholder(view);
    }


    class myCurrencyviewholder extends RecyclerView.ViewHolder
    {
//        CircleImageView img;
        TextView name;
        public myCurrencyviewholder(@NonNull View itemView)
        {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.tv_cr_name);
        }
    }
}
