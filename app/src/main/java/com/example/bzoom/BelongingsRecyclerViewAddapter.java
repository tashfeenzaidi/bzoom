package com.example.bzoom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BelongingsRecyclerViewAddapter extends  RecyclerView.Adapter<BelongingsRecyclerViewAddapter.ViewHolder>  {

    ArrayList<OwnerBelongings> belongins;
    Context context;

    public BelongingsRecyclerViewAddapter(ArrayList<OwnerBelongings> belongins, Context context) {
        this.belongins = belongins;
        this.context = context;
    }

    @NonNull
    @Override
    public BelongingsRecyclerViewAddapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.owner_belongings_view, viewGroup, false);
        return new BelongingsRecyclerViewAddapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BelongingsRecyclerViewAddapter.ViewHolder viewHolder, int i) {
        BelongingsRecyclerViewAddapter.ViewHolder viewHolder1 = (BelongingsRecyclerViewAddapter.ViewHolder)viewHolder;
        OwnerBelongings ownerBelongings = belongins.get(i);
        if(!belongins.isEmpty()){
            viewHolder1.textView.setText(ownerBelongings.getName());
            if (ownerBelongings.isStatus() == true){
                viewHolder1.checkBox.setChecked(true);
            }else {
                viewHolder1.checkBox.setChecked(false);

            }

        }
    }

    @Override
    public int getItemCount() {
        return belongins.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView  textView;
        public CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.name);
            checkBox = (CheckBox) itemView.findViewById(R.id.switch1);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
