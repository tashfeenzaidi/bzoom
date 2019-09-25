package com.example.bzoom;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.modal.firebase.Firebase;
import com.example.bzoom.modal.firebase.Ride;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;

import org.json.JSONException;

import java.util.List;

public class ChaufferMainFragment extends Fragment {

    Button Next;
    ViewFlipper viewFlipper;
    ImageView pre;
    ImageView next;
    ImageView carImg;
    TextView carTypeName;
    ProgressBar nDialog;
    private List<CarType> carTypes;
    private Keystore keystore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_chauffer_main, container, false);


        Next =  view.findViewById(R.id.Next);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SelectTimeActivity.class);
                intent.putExtra("activity",getString(R.string.chauffeur));
                startActivity(intent);
            }
        });
        viewFlipper =  view.findViewById(R.id.viewflipper);
        pre =  view.findViewById(R.id.pre);
        next =  view.findViewById(R.id.next);
        nDialog = view.findViewById(R.id.progress_loader);
        nDialog.setVisibility(View.VISIBLE);
        viewFlipper.getDisplayedChild();

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showPrevious();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showNext();

            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        keystore = Keystore.getInstance(getContext());

    }

    @Override
    public void onStart() {
        super.onStart();
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... voids) {

                carTypes = RetrofitClass.getRest(getString(R.string.getvahicaltype)+1);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                nDialog.setVisibility(View.INVISIBLE);


                for (CarType carType: carTypes) {
                    LayoutInflater inflater = (LayoutInflater) getActivity()
                            .getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.view_flipper_layout, null);

                    viewFlipper.addView(view);

                    carImg = view.findViewById(R.id.slider1);
                    carTypeName = view.findViewById(R.id.typeName);

                    carImg.setImageResource(R.drawable.slider01);
                    carTypeName.setText(carType.getName());

                }

            }
        }.execute();


        Next.setOnClickListener(v -> {
            if(carTypes == null){
                return;
            }
            int index =  viewFlipper.getDisplayedChild();
            try {
                Ride.setVehicalId(String.valueOf(CarType.carTypes.getJSONObject(index).getInt("id")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String topic = carTypes.get(index).getTopicname();
            CarType.activeCarTopicName = "c"+topic;
//            if (keystore.get("chauffeurTopicSubs") != null){
//                if (!topic.equals(keystore.get("chauffeurTopicSubs"))){
//                    Firebase.unsubscribeFromTopic(keystore.get("chauffeurTopicSubs"));
//                }
//            }

            Intent intent = new Intent(getContext(),SelectTimeActivity.class);
            intent.putExtra("activity",getString(R.string.chauffeur));
            startActivity(intent);

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (carTypes != null){
            carTypes.clear();
        }
    }
}
