package com.cutezilla.cryptomanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cutezilla.cryptomanager.R;
import com.cutezilla.cryptomanager.model.AllCoin;

import java.util.ArrayList;
import java.util.List;

public class AllCoinAdapter extends RecyclerView.Adapter<AllCoinAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<AllCoin> allCoinList;
    private List<AllCoin> allCoinListFiltered;
    private AllCoinAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phone,current_price;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            current_price = view.findViewById(R.id.current_price);
            name = view.findViewById(R.id.name);
            phone = view.findViewById(R.id.phone);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(allCoinListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public AllCoinAdapter(Context context, List<AllCoin> allCoinList, AllCoinAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.allCoinList = allCoinList;
        this.allCoinListFiltered = allCoinList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_coin_row_item, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AllCoin allCoin = allCoinListFiltered.get(position);
        holder.name.setText(allCoin.getSymbol());
        holder.phone.setText(allCoin.getName());
        holder.current_price.setText(String.valueOf(allCoin.getCurrent_price()));

        Glide.with(context)
                .load(allCoin.getImage())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);
    }
    @Override
    public int getItemCount() {
        return allCoinListFiltered.size();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    allCoinListFiltered = allCoinList;
                } else {
                    List<AllCoin> filteredList = new ArrayList<>();
                    for (AllCoin row : allCoinList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getSymbol().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    allCoinListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = allCoinListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                allCoinListFiltered = (ArrayList<AllCoin>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public interface AllCoinAdapterListener {
        void onContactSelected(AllCoin coin);
    }
}
