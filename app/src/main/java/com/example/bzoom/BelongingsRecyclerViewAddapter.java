package com.example.bzoom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bzoom.modules.map.Complaint;

import java.util.ArrayList;

public class BelongingsRecyclerViewAddapter extends  RecyclerView.Adapter<BelongingsRecyclerViewAddapter.ViewHolder>  {

    ArrayList<OwnerBelongings> belongins;
    Context context;
    boolean isAgree;

    public BelongingsRecyclerViewAddapter(ArrayList<OwnerBelongings> belongins, Context context, boolean isAgree) {
        this.belongins = belongins;
        this.context = context;
        this.isAgree = isAgree;
        Complaint.clauseList = new ArrayList<>();
    }

    @NonNull
    @Override
    public BelongingsRecyclerViewAddapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.owner_belongings_view, viewGroup, false);
        return new BelongingsRecyclerViewAddapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BelongingsRecyclerViewAddapter.ViewHolder viewHolder, int i) {
        BelongingsRecyclerViewAddapter.ViewHolder viewHolder1 = viewHolder;
        OwnerBelongings ownerBelongings = belongins.get(i);
        if(!belongins.isEmpty()){
            viewHolder1.textView.setText(ownerBelongings.getName());
            if (ownerBelongings.isStatus() == true){
                viewHolder1.checkBox.setChecked(true);

            }else {
                viewHolder1.checkBox.setChecked(false);
            }
        }

        viewHolder1.checkBox.setOnClickListener(v -> {
            if ( viewHolder1.checkBox.isChecked()){
                Complaint.clauseList.add(ownerBelongings.getId());
            }
        });
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

            textView =  itemView.findViewById(R.id.name);
            checkBox =  itemView.findViewById(R.id.switch1);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
