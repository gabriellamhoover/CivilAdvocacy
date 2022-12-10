package com.example.assignment4;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {
    String party;
    String photo;
    String name;
    private final String TAG = getClass().getSimpleName();
    private long start;
    ImageView imageView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        setTitle("Civil Advocacy");
        Intent intent = getIntent();
        if (intent.hasExtra(Official.class.getName())) {

            Official o = (Official) intent.getSerializableExtra(Official.class.getName());
            if (o == null)
                return;
            if (o.getParty().equals("Democratic Party")) {
                ConstraintLayout cl = findViewById(R.id.photoLayout);
                cl.setBackgroundColor(Color.BLUE);

            }
            if (o.getParty().equals("Republican Party")) {
                ConstraintLayout cl = findViewById(R.id.photoLayout);
                cl.setBackgroundColor(Color.RED);

            }
            this.name = o.getName();
            this.party = o.getParty();
            this.photo = o.getPhotoUrl();
            ImageView partyIcon = findViewById(R.id.photoParty);
            TextView name = findViewById(R.id.photoName);
            TextView position = findViewById(R.id.photoPosition);
            name.setText(o.getName());
            position.setText(o.getParty());
            this.imageView = findViewById(R.id.photoPhoto);
            if(this.party.equals("Republican Party")){
                partyIcon.setImageResource(R.drawable.rep_logo);
            }
            else if(!this.party.equals("Democratic Party")){
                partyIcon.setVisibility(View.GONE);
            }
            downloadImage();
            if (intent.hasExtra("LOCATION")) {
                TextView loc = findViewById(R.id.textViewPhotoLoc);
                String location  = (String)intent.getSerializableExtra("LOCATION");
                loc.setText(location);
            }
        }
    }

    private void downloadImage() {
        start = System.currentTimeMillis();
        Picasso.get().load(this.photo)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess(){
                        long time = System.currentTimeMillis() - start;
                        Log.d(TAG, "onSuccess: " + time);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d(TAG, "onError: " + e);
                        imageView.setImageResource(R.drawable.brokenimage);
                    }
                });

    }
    public void clickPartyIcon (View v){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if(this.party.equals("Democratic Party")){
            intent.setData(Uri.parse("https://democrats.org/"));
        }
        else if(this.party.equals("Republican Party")){
            intent.setData(Uri.parse("https://gop.org/"));
        }
        else{
            return;
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }
}
