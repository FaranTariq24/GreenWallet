package com.cutezilla.cryptomanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cutezilla.cryptomanager.R;
import com.cutezilla.cryptomanager.model.Coins;
import com.cutezilla.cryptomanager.model.StatusUpdate;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private Context context;
    private List<StatusUpdate> allStatusList;
    private NewsAdapterListener listener;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, user,date;
        public ImageView im_image;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tv_title);
            user = view.findViewById(R.id.tv_user);
            date = view.findViewById(R.id.tv_date);
            im_image = view.findViewById(R.id.im_image);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(allStatusList.get(getAdapterPosition()));
                }
            });
        }
    }

    public NewsAdapter(Context context, List<StatusUpdate> statusList, NewsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.allStatusList = statusList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_horizental_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final StatusUpdate statusUpdate = allStatusList.get(position);
        holder.user.setText(statusUpdate.getUser());
        holder.date.setText(dateFormat.format(statusUpdate.getCreated_at()));
        holder.title.setText(statusUpdate.getDescription());

                Glide.with(context)
                .load(statusUpdate.getProject().getImage().getLarge())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.im_image);

    }


    @Override
    public int getItemCount() {
        return allStatusList.size();

    }

    public interface NewsAdapterListener {
        void onContactSelected(StatusUpdate statusUpdate);
    }
}
