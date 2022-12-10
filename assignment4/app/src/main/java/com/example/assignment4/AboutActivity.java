package com.example.assignment4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("Civil Advocacy");

    }

    public void clickWebsite (View v){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://developers.google.com/civic-information/"));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }
}
