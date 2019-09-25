package com.example.bzoom;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.modal.firebase.Firebase;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;
import com.example.bzoom.modules.map.driver.Driver;

import org.json.JSONException;

public class Rider_Home_Fragment extends Fragment {

    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private HomeFragment.OnFragmentInteractionListener mListener;
    private Keystore keystore;
    private TextView totalRides;
    private TextView totalMonth;
    private TextView totalReview;
    private TextView totalEarned;
    private boolean isExist;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.driver_home, container, false);
        totalEarned = view.findViewById(R.id.money);
        totalMonth  = view.findViewById(R.id.months);
        totalRides  = view.findViewById(R.id.rides);
        totalReview  = view.findViewById(R.id.reviews);


        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... voids) {

              isExist =  RetrofitClass.getDriverHomeScreen("/get-wallet-data",keystore.get("userId"));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (isExist){
                            totalEarned.setText(String.valueOf(Driver.totalEarned));
                             totalMonth.setText(String.valueOf(Driver.totalMonth));
                             totalRides.setText(String.valueOf(Driver.totalRides));
                            totalReview.setText(String.valueOf(Driver.totalReview));
                }

            }
        }.execute();

        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        keystore = Keystore.getInstance(getContext());
        if (MainActivity.cancel){
            androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Rider cancel the ride!");
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        }


    }

    @Override
    public void onStart() {
        super.onStart();

        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... voids) {

                RetrofitClass.getDriverVehicalData("/get-driver-data",keystore.get("userId"));

                return null;
            }
        }.execute();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
}
