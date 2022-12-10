package com.example.assignment4;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {


    TextView name;
    TextView party;
    TextView position;
    TextView location;
    ImageView photo;


    MyViewHolder(View view) {
        super(view);
        name = view.findViewById(R.id.textViewName);
        position = view.findViewById(R.id.textViewPosition);
        photo = view.findViewById(R.id.imageViewOffPhoto);
    }


}

