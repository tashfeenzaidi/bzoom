package com.example.bzoom;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bzoom.modal.firebase.AvailableCar;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;
import com.example.bzoom.modules.map.MapUtility;
import com.example.bzoom.modules.map.chauffeur.ChauffeurTimeFinish;

import java.util.ArrayList;

import static android.content.Context.ALARM_SERVICE;

public class OwnerRecyclerViewAddapter  extends  RecyclerView.Adapter<OwnerRecyclerViewAddapter.ViewHolder>  {
    ArrayList<AvailableCar> owners;
    Context context;
    MapUtility mapUtility;

    public OwnerRecyclerViewAddapter(ArrayList<AvailableCar> owner, Context context) {
        this.owners = owner;
        this.context = context;
        mapUtility = new MapUtility(context);
    }

    @NonNull
    @Override
    public OwnerRecyclerViewAddapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.owner_list, viewGroup, false);
        return new OwnerRecyclerViewAddapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerRecyclerViewAddapter.ViewHolder viewHolder, int i) {
        AvailableCar owner = owners.get(i);
        if(!owners.isEmpty()){
            viewHolder.car.setText(owner.getColor()+" "+owner.getModal()+" "+owner.getPlateNumber());
            viewHolder.name.setText(owner.getName());
            viewHolder.rating.setText(String.valueOf(owner.getRating()));
            viewHolder.ratingbar.setRating((float) owner.getRating());
            viewHolder.address.setText(getAddress(owner.getLat(),owner.getLon()));
        }
    }

    @Override
    public int getItemCount() {
        return owners.size();
    }

    public String getAddress(Double lat, Double lon){
        return mapUtility.getCompleteAddressString(lat,lon);
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
            name =  itemView.findViewById(R.id.name);
            address =  itemView.findViewById(R.id.address);
            car =  itemView.findViewById(R.id.txtph);
            rating =  itemView.findViewById(R.id.textView3);
            ratingbar =  itemView.findViewById(R.id.ratingBar);

        }



        @Override
        public void onClick(View v) {
             cont = v.getContext();
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION)
            {

                AvailableCar.activeAvailableId = owners.get(position).getAvailableId();
                AvailableCar.activeVehicalId = owners.get(position).getVehicalId();
                AvailableCar.activeOwnerId = owners.get(position).getOwnerId();
                AvailableCar.activelat = owners.get(position).getLat();
                AvailableCar.activelon = owners.get(position).getLon();
                AvailableCar.activeEndDate = owners.get(position).getEndDate();
                AvailableCar.activeEndTime = owners.get(position).getEndTime();
                AvailableCar.status = "timeStart";

            }
            new AsyncTask<Void,Void,Void>(){
                private boolean isEixt;

                @Override
                protected Void doInBackground(Void... voids) {

                    isEixt = RetrofitClass.bookCar("/book-car");

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if (isEixt){
                        Intent intent = new Intent(cont, CollectTheCarActivity.class);
                        cont.startActivity(intent);

                    }

                }
            }.execute();
        }
    }
}
