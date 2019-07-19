package com.example.bzoom;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OwnerRecyclerViewAddapter  extends  RecyclerView.Adapter<OwnerRecyclerViewAddapter.ViewHolder>  {
    ArrayList<Owner> owners;
    Context context;

    public OwnerRecyclerViewAddapter(ArrayList<Owner> owner, Context context) {
        this.owners = owner;
        this.context = context;
    }

    @NonNull
    @Override
    public OwnerRecyclerViewAddapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.owner_list, viewGroup, false);
        return new OwnerRecyclerViewAddapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerRecyclerViewAddapter.ViewHolder viewHolder, int i) {
        OwnerRecyclerViewAddapter.ViewHolder viewHolder1 = (ViewHolder)viewHolder;
        Owner owner = owners.get(i);
        if(!owners.isEmpty()){
            viewHolder1.name.setText(owner.getName());
            viewHolder1.address.setText(owner.getAddress());
            viewHolder1.car.setText(owner.getCar());
            viewHolder1.rating.setText(String.valueOf(owner.getRating()));
            viewHolder1.ratingbar.setRating((float) owner.getRating());
        }
    }

    @Override
    public int getItemCount() {
        return owners.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView address;
        public TextView car;
        public TextView rating;
        public RatingBar ratingbar;
        Context cont;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cont = context;
            itemView.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.name);
            address = (TextView) itemView.findViewById(R.id.address);
            car = (TextView) itemView.findViewById(R.id.txtph);
            rating = (TextView) itemView.findViewById(R.id.textView3);
            ratingbar = (RatingBar) itemView.findViewById(R.id.ratingBar);

        }

        @Override
        public void onClick(View v) {
             cont = v.getContext();
                Intent intent = new Intent(cont, CollectTheCarActivity.class);
            cont.startActivity(intent);
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION)
            {

            }
        }
    }
}
