package com.cutezilla.cryptomanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cutezilla.cryptomanager.R;
import com.cutezilla.cryptomanager.model.Currency;

import java.util.List;

public class MyCurrencyAdapter extends RecyclerView.Adapter<MyCurrencyAdapter.MyViewHolder>{

    Context context;
    List<Currency>  currencyList;

    public MyCurrencyAdapter(List<Currency> currList) {

        this.currencyList = currList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow, parent, false);

        MyViewHolder holder = new MyViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull  MyCurrencyAdapter.MyViewHolder holder, int position) {
        holder.name.setText(currencyList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Position clicked: "+position,Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;


        public MyViewHolder(View view) {
            super(view);

            context = view.getContext();
            name = (TextView) view.findViewById(R.id.tv_cr_name);
        }

    }
    public void filterList(List<Currency> filteredList){
        currencyList = filteredList;
        notifyDataSetChanged();
    }
}
