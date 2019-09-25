package com.example.bzoom.modules.map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bzoom.BelongingsRecyclerViewAddapter;
import com.example.bzoom.Belongins;
import com.example.bzoom.MainActivity;
import com.example.bzoom.OwnerBelongings;
import com.example.bzoom.R;
import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.modal.firebase.AvailableCar;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;


public class DisagreeAgreement extends AppCompatActivity {

    Keystore keystore;
    private RecyclerView recyclerView;
    Button disagree;
    EditText description;
    private boolean isExist;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disagree_complaint);

        keystore = Keystore.getInstance(this);
        recyclerView = findViewById(R.id.belongings_recycler);
        disagree = findViewById(R.id.disagree);
        description = findViewById(R.id.editText);
        for (OwnerBelongings belongings: Belongins.belongingsArrayList) {
            belongings.setStatus(false);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        recyclerView.setLayoutManager(new GridLayoutManager(DisagreeAgreement.this,1));
        BelongingsRecyclerViewAddapter belongingsRecyclerViewAddapter = new BelongingsRecyclerViewAddapter(Belongins.belongingsArrayList,DisagreeAgreement.this,false);
        recyclerView.setAdapter(belongingsRecyclerViewAddapter);


        disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text =description.getText().toString();
                if (text.equals("")){
                    return;
                }
                Complaint.setAvailableId(AvailableCar.activeAvailableId);
                Complaint.setDescription(text);
                if (keystore.get("role").equals("owner")){
                    Complaint.setIsOwner(true);
                     title = getString(R.string.owner_disagree);
                }else {
                     title = getString(R.string.chauffeur_disagree);
                    Complaint.setIsOwner(false);
                }

                new AsyncTask<Void,Void,Void>(){
                    @Override
                    protected Void doInBackground(Void... voids) {

                        isExist = RetrofitClass.postComplaint("/booking_complain",title);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        if (isExist){
                            Intent intent = new Intent(DisagreeAgreement.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }


                    }
                }.execute();
            }
        });
    }
}
