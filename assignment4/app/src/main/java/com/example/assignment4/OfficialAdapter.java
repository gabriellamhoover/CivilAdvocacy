package com.example.assignment4;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OfficialAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private static final String TAG = "OfficialAdapter";
    private final List<Official> officialList;
    private final MainActivity mainAct;
    private long start;
    ImageView photo;

    OfficialAdapter(List<Official> empList, MainActivity ma) {
        this.officialList = empList;
        mainAct = ma;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.official_entry, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Official official = officialList.get(position);
        holder.name.setText(official.getName() + " (" + official.getParty() + ")");
        //holder.party.setText(official.getParty());
        holder.position.setText(official.getPosition());
        if(official.getPhotoUrl() != null){
            downloadImage(holder, official);
        }
        else{
            holder.photo.setImageResource(R.drawable.missing);
        }

    }
    private void downloadImage(@NonNull MyViewHolder holder, Official o) {
        start = System.currentTimeMillis();
        Picasso.get().load(o.getPhotoUrl())
                .into(holder.photo, new Callback() {
                    @Override
                    public void onSuccess(){
                        long time = System.currentTimeMillis() - start;
                        Log.d(TAG, "onSuccess: " + time);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d(TAG, "onError: " + e);
                        holder.photo.setImageResource(R.drawable.brokenimage);
                    }
                });

    }

    @Override
    public int getItemCount() {
        return officialList.size();
    }
}

