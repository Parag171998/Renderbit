package com.example.renderbit.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.renderbit.MapsActivity;
import com.example.renderbit.Models.Order;
import com.example.renderbit.R;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

    List<Order> orderList;
    Context context;

    public OrdersAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrdersViewHolder(
                LayoutInflater.from(context).inflate(
                R.layout.custom_recycler_layout
                ,parent
                ,false
        )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {

        Glide.with(context).load(orderList.get(position).getAvatar()).into(holder.userImg);
        holder.userName.setText(orderList.get(position).getName());
        holder.time.setText(orderList.get(position).getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrdersViewHolder extends RecyclerView.ViewHolder{

        ImageView userImg;
        TextView userName;
        TextView time;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userImg = itemView.findViewById(R.id.custom_img);
            userName = itemView.findViewById(R.id.custom_name);
            time = itemView.findViewById(R.id.custom_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MapsActivity.class);
                    intent.putExtra("lat",orderList.get(getAdapterPosition()).getLatitude());
                    intent.putExtra("long",orderList.get(getAdapterPosition()).getLongitute());
                    intent.putExtra("otp",orderList.get(getAdapterPosition()).getOtp().intValue());
                    intent.putExtra("name",orderList.get(getAdapterPosition()).getName());
                    context.startActivity(intent);


                }
            });

        }
    }
}
