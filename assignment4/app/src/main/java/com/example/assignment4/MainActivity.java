package com.example.assignment4;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    // The above lines are important - them make this class a listener
    // for click and long click events in the ViewHolders (in the recycler


    // Don't forget you need:
    //      <uses-permission android:name="android.permission.INTERNET"/>

    private final List<Official> officialList = new ArrayList<>();  // Main content is here
    private RecyclerView recyclerView; // Layout's recyclerview
    private OfficialAdapter mAdapter; // Data to recyclerview adapter
    private final String TAG = getClass().getSimpleName();
    private String zipcode;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private TextView locationText;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;
    private String city;

    private static String locationString = "Unspecified Location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Civil Advocacy");
        this.locationText = findViewById(R.id.textViewLoc);
        this.zipcode = "46845";
        recyclerView = findViewById(R.id.recycler);

        mAdapter = new OfficialAdapter(officialList, this);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);
        if (!checkNet(null)) {
            dialogNoNetwork();
        } else {
            mFusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(this);
            determineLocation();

        }
    }
    public void dialogNoNetwork() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Data cannot be accessed/loaded without an internet connection");
        builder.setTitle("No Network Connection");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void dialogNoLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Location cannot be found at the moment");
        builder.setTitle("No Location Found");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void handleResult(ActivityResult result) {
        if (result == null || result.getData() == null) {
            Log.d(TAG, "handleResult: NULL ActivityResult received");
            return;
        }

        Intent data = result.getData();
    }
    public boolean checkNet(View v) {
        if (hasNetworkConnection())
            return true;
        else
            return false;
    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            connectivityManager = getSystemService(ConnectivityManager.class);
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try {
            if (item.getItemId() == R.id.menu_search) {
                dialogSearch();
            }else if

            (item.getItemId() == R.id.menu_about) {
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        int pos = recyclerView.getChildLayoutPosition(view);
        Official o = officialList.get(pos);
        Intent intent = new Intent(MainActivity.this, OfficialActivity.class);
        intent.putExtra(Official.class.getName(), o);
        intent.putExtra("LOCATION", this.locationText.getText());
        startActivity(intent);

    }
    @Override
    public boolean onLongClick(View view) {

        return true;
    }
    
  public void updateData(ArrayList<Official> officials) {
      officialList.addAll(officials);
      mAdapter.notifyItemRangeChanged(0, officialList.size());
  }
  public void updateLocation(String location){
    this.locationText.setText(location);
  }

    public void downloadFailed() {
        officialList.clear();
        mAdapter.notifyItemRangeChanged(0, officialList.size());
    }

    private void determineLocation() {
        // Check perm - if not then start the  request and return
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some situations this can be null.
                    if (location != null) {
                        locationString = getPlace(location);
                        city = locationString;
                        OfficialDownloader.getSourceData(this,city, false );
                        mAdapter.notifyItemRangeChanged(0, officialList.size());
                        locationText.setText(locationString);
                    }
                })
                .addOnFailureListener(this, e ->
                        Toast.makeText(MainActivity.this,
                                e.getMessage(), Toast.LENGTH_LONG).show());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                } else {
                    locationText.setText("Request denied");
                }
            }
        }
    }


    private String getPlace(Location loc) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            String addr = addresses.get(0).getAddressLine(0);
            return addr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void dialogSearch() {
        // Single input value dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Create an edittext and set it to be the builder's view
        final EditText et = new EditText(this);
        et.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(et);

        // lambda can be used here (as is below)
        builder.setPositiveButton("OK", (dialog, id) -> {
            this.city = et.getText().toString();
            officialList.clear();
            mAdapter.notifyItemRangeChanged(0, officialList.size());
            OfficialDownloader.getSourceData(this, city, true);
            mAdapter.notifyItemRangeChanged(0, officialList.size());
        });

        // lambda can be used here (as is below)
        builder.setNegativeButton("CANCEL", (dialog, id) -> {
        });


        builder.setTitle("Enter Address");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}