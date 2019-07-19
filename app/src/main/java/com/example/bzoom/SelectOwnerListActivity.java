package com.example.bzoom;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectOwnerListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ArrayList<Owner> ownerArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_owner_list);
        ownerArrayList = new ArrayList<Owner>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView = findViewById(R.id.owner_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        OwnerRecyclerViewAddapter ownerrecyclerviewaddapter= new OwnerRecyclerViewAddapter(fillList(),this);
        recyclerView.setAdapter(ownerrecyclerviewaddapter);
    }

    public ArrayList<Owner> fillList(){
        ownerArrayList.add(new Owner("Jamal","03021533541","Defence phase VII, near Chai Shai hvhvhhjjjjjjjjjhhjvhvhjvjhjjhhjhvjhv","white mehran",3));
        ownerArrayList.add(new Owner("Jamal","03021533541","c-135,bl-4,gulshan","white mehran",3.5));
        ownerArrayList.add(new Owner("Jamal","03021533541","c-135,bl-4,gulshan","white mehran",4.2));
        ownerArrayList.add(new Owner("Jamal","03021533541","c-135,bl-4,gulshan","white mehran",2));
        ownerArrayList.add(new Owner("Jamal","03021533541","c-135,bl-4,gulshan","white mehran",5));
        ownerArrayList.add(new Owner("Jamal","03021533541","c-135,bl-4,gulshan","white mehran",4.5));
        return ownerArrayList ;
    }
}
