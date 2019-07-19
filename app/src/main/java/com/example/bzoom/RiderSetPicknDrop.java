package com.example.bzoom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class RiderSetPicknDrop extends Fragment {

    private HomeFragment.OnFragmentInteractionListener mListener;
    View view_rent;
    View view_ride;
    Button ride;
    Button rent;
    public boolean check;
    EditText destination;
    View div;
    Button bzoom;
    ImageView arrow;
    ImageView curr_location;
    ImageView sourceanddestination;
    RelativeLayout cardView;
    ImageButton micro;
    ImageButton bzoomplus;
    ImageButton xclass;
    CardView cab_card;
    CardView rent_card;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rider_set_pickndrop, container, false);
        bzoom = (Button) view.findViewById(R.id.bzoom);
        check = true;
        cardView = (RelativeLayout) view.findViewById(R.id.releative);
        arrow =(ImageView) view.findViewById(R.id.end);
        curr_location =(ImageView) view.findViewById(R.id.currentlocation);
        sourceanddestination =(ImageView) view.findViewById(R.id.img);
        div =(View) view.findViewById(R.id.div);
        destination =(EditText) view.findViewById(R.id.destinaton);
        view_ride =(View) view.findViewById(R.id.cab_border);
        view_rent =(View) view.findViewById(R.id.rent_border);
        view_rent.setVisibility(View.INVISIBLE);
        ride = (Button) view.findViewById(R.id.ride);
        rent = (Button) view.findViewById(R.id.rent);
        micro = (ImageButton) view.findViewById(R.id.micro);
        bzoomplus = (ImageButton) view.findViewById(R.id.bzoomplus);
        xclass = (ImageButton) view.findViewById(R.id.xclass);

        cab_card = (CardView) view.findViewById(R.id.topcard);
        rent_card = (CardView) view.findViewById(R.id.topcardrent);

        micro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                micro.setImageResource(R.drawable.microctive);
                bzoomplus.setImageResource(R.drawable.bzoomplusdim);
                xclass.setImageResource(R.drawable.xclassdim);

            }
        });
        bzoomplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                micro.setImageResource(R.drawable.microdim);
                bzoomplus.setImageResource(R.drawable.bzoomplus);
                xclass.setImageResource(R.drawable.xclassdim);

            }
        });
        xclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                micro.setImageResource(R.drawable.microdim);
                bzoomplus.setImageResource(R.drawable.bzoomplusdim);
                xclass.setImageResource(R.drawable.xclassactive);

            }
        });
        ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check != true){
                    check = true;
                    //cab_card.setVisibility(View.VISIBLE);
                    //rent_card.setVisibility(View.INVISIBLE);
                    view_ride.setVisibility(View.VISIBLE);
                    view_rent.setVisibility(View.INVISIBLE);
                    bzoom.setText("Bzoom");
                }


            }
        });
        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check != false){

                    check = false;
                    //cab_card.setVisibility(View.INVISIBLE);
                    //rent_card.setVisibility(View.VISIBLE);
                    view_ride.setVisibility(View.INVISIBLE);
                    view_rent.setVisibility(View.VISIBLE);
                    bzoom.setText("Next");
                }

            }
        });
        bzoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check){
                    Intent intent = new Intent(getActivity(),ConfirmDetail.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(),SelectTimeActivity.class);
                    intent.putExtra("activity","rent");
                    startActivity(intent);
                }

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void buttonSwitchLayout(){
        if(check == false){
            div.setVisibility(View.GONE);
            destination.setVisibility(View.GONE);
            bzoom.setText("Next");
            arrow.setVisibility(View.GONE);
            sourceanddestination.setImageResource(R.drawable.destination);
            RelativeLayout.LayoutParams layout_description = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT ,
                    350);

            cardView.setLayoutParams(layout_description);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(curr_location.getLayoutParams());
            lp.addRule(RelativeLayout.ALIGN_PARENT_END);
            lp.setMargins(0, 70, 20, 0);
            curr_location.setLayoutParams(lp);
        }
    }
}
