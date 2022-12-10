package com.example.assignment4;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Locale;

public class OfficialActivity extends AppCompatActivity {

    String website;
    String phone;
    String email;
    String address;
    String fb;
    String yt;
    String tw;
    String party;
    String photo;
    String name;
    String location;
    private final String TAG = getClass().getSimpleName();
    private long start;
    ImageView imageView;
    private Official o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        setTitle("Civil Advocacy");
        Intent intent = getIntent();
        if (intent.hasExtra(Official.class.getName())) {

            o = (Official) intent.getSerializableExtra(Official.class.getName());
            if (o == null)
                return;
            if (o.getParty().equals("Democratic Party")) {
                ConstraintLayout cl = findViewById(R.id.official_layout);
                cl.setBackgroundColor(Color.BLUE);

            }
            if (o.getParty().equals("Republican Party")) {
                ConstraintLayout cl = findViewById(R.id.official_layout);
                cl.setBackgroundColor(Color.RED);

            }
            this.website = o.getUrl();
            this.address = o.getAddress();
            this.email = o.getEmail();
            this.phone = o.getPhone();
            this.fb = o.getFacebook();
            this.yt = o.getYoutube();
            this.tw = o.getTwitter();
            this.party = o.getParty();
            this.photo = o.getPhotoUrl();
            this.imageView = findViewById(R.id.imageViewPhoto);
            this.name = o.getName();
            TextView position = findViewById(R.id.textViewOffPosition);
            position.setText(o.getPosition());
            TextView name = findViewById(R.id.textViewOffName);
            name.setText(o.getName());
            TextView party = findViewById(R.id.textViewOffParty);
            party.setText("(" + o.getParty() + ")");
            TextView phone = findViewById(R.id.textViewOffPhone);
            ImageView facebook = findViewById(R.id.imageViewFb);
            ImageView youtube = findViewById(R.id.imageViewYt);
            ImageView twitter = findViewById(R.id.imageViewTw);
            ImageView partyIcon = findViewById(R.id.imageViewDem);
            if(this.party.equals("Republican Party")){
                partyIcon.setImageResource(R.drawable.rep_logo);
            }
            else if(!this.party.equals("Democratic Party")){
                partyIcon.setVisibility(View.GONE);
            }
            if(this.fb == null){
                facebook.setVisibility(View.GONE);
            }
            if(this.yt == null){
                youtube.setVisibility(View.GONE);
            }
            if(this.tw == null){
                twitter.setVisibility(View.GONE);
            }
            if (this.phone != null) {
                phone.setText(o.getPhone());
            }
            else{
                phone.setVisibility(View.GONE);
                TextView phoneText =  findViewById(R.id.textViewPhone);
                phoneText.setVisibility(View.GONE);
            }
            TextView email = findViewById(R.id.textViewOffEmail);
            if (this.email != null) {
                email.setText(o.getEmail());
            }
            else{
                email.setVisibility(View.GONE);
                TextView emailText =  findViewById(R.id.textViewEmail);
                emailText.setVisibility(View.GONE);
            }
            TextView website = findViewById(R.id.textViewOffWeb);
            if (this.website != null) {
                website.setText(o.getUrl());
            }
            else{
                website.setVisibility(View.GONE);
                TextView websiteText =  findViewById(R.id.textViewWebsite);
                websiteText.setVisibility(View.GONE);
            }
            TextView address = findViewById(R.id.textViewOffAddress);
            if (this.address != null) {
                address.setText(o.getAddress());
            }
            else{
                address.setVisibility(View.GONE);
                TextView addressText =  findViewById(R.id.textViewAddress);
                addressText.setVisibility(View.GONE);
            }
            if(o.getPhotoUrl() != null){
                downloadImage();
            }
            if (intent.hasExtra("LOCATION")) {
                TextView loc = findViewById(R.id.textViewLocation);
                location  = (String)intent.getSerializableExtra("LOCATION");
                loc.setText(location);
            }
            }
        }

        public void clickWebsite (View v){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(this.website));

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }

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


    public void clickPhone(View v) {
        if(this.phone == null){
            return;
        }

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + this.phone));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_DIAL (tel) intents");
        }

    }
    public void clickEmail(View v) {
        if(this.email == null){
            return;
        }
        String[] addresses = new String[]{this.email};

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));

        intent.putExtra(Intent.EXTRA_EMAIL, addresses);

        // Check if there is an app that can handle mailto intents
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles SENDTO (mailto) intents");
        }

    }
    public void clickAddress(View v) {
        if(this.address == null){
            return;
        }

        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(this.address));

        Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);

        // Check if there is an app that can handle geo intents
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (geo) intents");
        }

    }
    public void youTubeClicked(View v) {
        if(this.yt == null){
            return;
        }
        String name = this.yt;
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + name)));
        }
    }
    public void clickFacebook(View v) {
        if(this.fb == null){
            return;
        }
        // You need the FB user's id for the url
        String FACEBOOK_URL = this.fb;

        Intent intent;

        // Check if FB is installed, if not we'll use the browser
        if (isPackageInstalled("com.facebook.katana")) {
            String urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToUse));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/" + FACEBOOK_URL));
        }

        // Check if there is an app that can handle fb or https intents
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (fb/https) intents");
        }

    }
    public void clickTwitter(View v) {
        if(this.tw == null){
            return;
        }
        String user = this.tw;
        String twitterAppUrl = "twitter://user?screen_name=" + user;
        String twitterWebUrl = "https://twitter.com/" + user;

        Intent intent;
        // Check if Twitter is installed, if not we'll use the browser
        if (isPackageInstalled("com.twitter.android")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterAppUrl));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterWebUrl));
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (twitter/https) intents");
        }
    }

    public void clickPhoto(View v){
        if(this.photo == null){
            return;
        }
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra(Official.class.getName(), o);
        intent.putExtra("LOCATION", location);
        startActivity(intent);
    }

    private void makeErrorAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle("No App Found");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public boolean isPackageInstalled(String packageName) {
        try {
            return getPackageManager().getApplicationInfo(packageName, 0).enabled;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
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
}
