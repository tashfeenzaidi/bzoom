package com.example.bzoom;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ChaufferMainFragment extends Fragment {

    Button Next;
    ViewFlipper viewFlipper;
    ImageView pre;
    ImageView next;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_chauffer_main, container, false);

        Next = (Button) view.findViewById(R.id.Next);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ChaufferEndTime.class);
                startActivity(intent);
            }
        });
        viewFlipper = (ViewFlipper) view.findViewById(R.id.viewflipper);
        pre = (ImageView) view.findViewById(R.id.pre);
        next = (ImageView) view.findViewById(R.id.next);
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
}
