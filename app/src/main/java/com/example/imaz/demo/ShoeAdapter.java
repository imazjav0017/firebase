package com.example.imaz.demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ShoeAdapter extends RecyclerView.Adapter<ShoeAdapter.ViewHolder> {

    List<ShoeModel> shoeModelList;

    public ShoeAdapter(List<ShoeModel> shoeModelList) {
        this.shoeModelList = shoeModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shoe_list_item,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ShoeModel sm=shoeModelList.get(i);
        viewHolder.shoeID.setText(sm.getId());
        viewHolder.shoeCost.setText("₹ "+sm.getCost());
        viewHolder.shoeName.setText(sm.getName());
        Picasso.with(viewHolder.ctx).load(sm.getImageUrl()).into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return shoeModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        Context ctx;
        ImageView image;
        TextView shoeID,shoeName,shoeCost;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ctx=itemView.getContext();
            image=itemView.findViewById(R.id.shoeImage);
            shoeID=itemView.findViewById(R.id.shoeIdTv);
            shoeName=itemView.findViewById(R.id.shoeNameTv);
            shoeCost=itemView.findViewById(R.id.shoeCostTv);
        }
    }
}
