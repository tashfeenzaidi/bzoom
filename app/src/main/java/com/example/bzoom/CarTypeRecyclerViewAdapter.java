package com.example.bzoom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CarTypeRecyclerViewAdapter extends RecyclerView.Adapter<CarTypeRecyclerViewAdapter.ViewHolder> {

    List<CarType> carType;
    Context context;
    private ViewHolder preViewHolder;
    private int preCarTypeId = -1;
    ImageView preCarImage;

    public CarTypeRecyclerViewAdapter(List<CarType> carType, Context context) {
        this.carType = carType;
        this.context = context;
    }

    @NonNull
    @Override
    public CarTypeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_type_recycler_view, parent, false);
        return new CarTypeRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarTypeRecyclerViewAdapter.ViewHolder holder, int position) {

        CarType carType = this.carType.get(position);


        if(!this.carType.isEmpty()){
            //Glide.with(context).load("http://i.imgur.com/DvpvklR.png").into(viewHolder.car_image);
            holder.car_image.setImageResource(R.drawable.microdim);
            holder.time.setText("");
            holder.rate.setText(carType.getName());

            if (carType.isStatus()){
                holder.car_image.setImageResource(R.drawable.microctive);
                CarType.activeCarId = carType.getId();
                preCarTypeId = position;
                preCarImage = holder.car_image;

            }
            holder.car_image.setOnClickListener(v -> {

                if (preCarTypeId != -1 && this.carType.size() !=0){

                    this.carType.get(preCarTypeId).setStatus(false);
                    preCarImage.setImageResource(R.drawable.microdim);
                }

                if (!carType.isStatus()){
                    holder.car_image.setImageResource(R.drawable.microctive);
                    carType.setStatus(true);
                    CarType.activeCarId = carType.getId();
                    preCarTypeId = position;
                    preCarImage = holder.car_image;

                }else {
                    holder.car_image.setImageResource(R.drawable.microdim);
                    carType.setStatus(false);
                }
            });
        }
    }

    @Override
    public int getItemCount() {return carType.size();}



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView car_image;
        TextView  rate;
        TextView  time;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.view = itemView;
            car_image = itemView.findViewById(R.id.micro);
            rate= itemView.findViewById(R.id.price);
            time= itemView.findViewById(R.id.time);

        }

        @Override
        public void onClick(View v) {


            int pos = getAdapterPosition();
            Toast.makeText(context,"here",Toast.LENGTH_LONG);
        }
    }
}
