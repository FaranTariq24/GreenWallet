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
import com.cutezilla.cryptomanager.model.Coins;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CoinsAdapter extends RecyclerView.Adapter<CoinsAdapter.MyViewHolder>
        implements Filterable {

    private Context context;
    private List<Coins> allCoinList;
    private List<Coins> coinsListFiltered;
    private CoinAdapterListener listener;


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
                    listener.onContactSelected(coinsListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public CoinsAdapter(Context context, List<Coins> coinsList, CoinAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.allCoinList = coinsList;
        this.coinsListFiltered = coinsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_coin_row_item, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        DecimalFormat formatter = new DecimalFormat("#,###,###.##############");
        final Coins coins = coinsListFiltered.get(position);
        holder.name.setText(coins.getItem().getSymbol());
        holder.phone.setText(coins.getItem().getName());
        holder.current_price.setText(formatter.format(coins.getItem().getPrice_btc()));

        Glide.with(context)
                .load(coins.getItem().getSmall())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);
    }
    @Override
    public int getItemCount() {

            return allCoinList.size();

     }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    coinsListFiltered = allCoinList;
                } else {
                    List<Coins> filteredList = new ArrayList<>();
                    for (Coins row : allCoinList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getItem().getSymbol().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    coinsListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = coinsListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                coinsListFiltered = (ArrayList<Coins>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public interface CoinAdapterListener {
        void onContactSelected(Coins coin);
    }
}
