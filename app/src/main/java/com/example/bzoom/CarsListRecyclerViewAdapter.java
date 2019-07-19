package com.example.bzoom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CarsListRecyclerViewAdapter extends RecyclerView.Adapter<CarsListRecyclerViewAdapter.ViewHolder> {

    ArrayList<Car> cars;
    Context context;

    public CarsListRecyclerViewAdapter( Context context,ArrayList<Car> data) {
        this.cars = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cars_list_view, viewGroup, false);
        return new CarsListRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarsListRecyclerViewAdapter.ViewHolder viewHolder, int i) {

        CarsListRecyclerViewAdapter.ViewHolder viewHolder1 = (CarsListRecyclerViewAdapter.ViewHolder)viewHolder;
        Car car = cars.get(i);
        if(!cars.isEmpty()){

            viewHolder1.name.setText(String.valueOf(car.getName()));
            viewHolder1.Img.setImageResource(R.drawable.car1);
            viewHolder1.rating.setText( String.valueOf(car.getRating()));
            viewHolder1.ratingBar.setRating(car.getRating());
            if(car.status == 1){
                viewHolder1.status.setText("on duty");
                viewHolder1.status.setTextColor(Color.parseColor("#006400"));
            }else {
                viewHolder1.status.setText("parked");
            }
        }
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public ImageView Img;
        public TextView rating;
        public RatingBar ratingBar;
        public TextView status;

        public ViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);
            name = (TextView) view.findViewById(R.id.name);
            Img= (ImageView) view.findViewById(R.id.image_rv);
            rating = (TextView) view.findViewById(R.id.textView3);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            status = (TextView) view.findViewById(R.id.textView2);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION)
            {

                Intent intent = new Intent(context,PickupLocationActivity.class);
                context.startActivity(intent);
//                AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                Fragment myFragment = new MapFragment();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
//                Fragment someFragment = new MapFragment();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.cars_recycler, someFragment ); // give your fragment container id in first parameter
//                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
//                transaction.commit();
            }
        }
    }
}
