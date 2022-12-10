package com.example.assignment4;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
public class OfficialDownloader {
    private static final String TAG = "OfficDownloadRunnable";

    private static MainActivity mainActivity;
    private static RequestQueue queue;


    private static final String officialURL = "https://www.googleapis.com/civicinfo/v2/representatives?key=";

    private static final String yourAPIKey = "AIzaSyAkAtw7_MiIIJcVWkYwdtp5Xmkm1j3kcIk";
    //
    //////////////////////////////////////////////////////////////////////////////////

    public static void getSourceData(MainActivity mainActivity, String address, boolean searched) {
        RequestQueue queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(officialURL + yourAPIKey + "&address=" + address).buildUpon();
        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONObject> listener =
                response -> handleResults(mainActivity, response.toString(), searched);

        Response.ErrorListener error = error1 -> {
            Log.d(TAG, "getSourceData: ");
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(new String(error1.networkResponse.data));
                Log.d(TAG, "getSourceData: " + jsonObject);
                handleResults(mainActivity, null, searched);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private static void handleResults(MainActivity mainActivity, String s, boolean searched) {

        if (s == null) {
            Log.d(TAG, "handleResults: Failure in data download");
            mainActivity.downloadFailed();
            return;
        }
        if(searched){
        String location = parseLocation(mainActivity, s);
        mainActivity.updateLocation(location);}

        ArrayList<Official> officials = parseJSON(mainActivity, s);
        if (officials != null)
            mainActivity.updateData(officials);
    }

    private static String parseLocation(MainActivity mainActivity, String s) {
        String location = "";
        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONObject jObjloc = jObjMain.getJSONObject("normalizedInput");
            String zipcode = "";
            String city = "";
            String state = "";
            if (jObjloc.has("zipcode")) {
                zipcode += jObjloc.getString("zipcode");
            }
            if (jObjloc.has("city")) {
                city += jObjloc.getString("city");
            }
            if (jObjloc.has("state")) {
                state += jObjloc.getString("state");
            }
            location = city + ", " + state + " " + zipcode;
            return location;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    private static ArrayList<Official> parseJSON(MainActivity mainActivity, String s) {
        //String name;
        //ArrayList<Stock> stockList = new ArrayList<>();
        ArrayList<Official> officialList = new ArrayList<>();
        ArrayList<String> officialIndices = new ArrayList<>();

        Official official = null;
        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONArray offices = jObjMain.getJSONArray("offices");
            int index = 0;
            String position = null;
            for(int k = 0; k < offices.length(); k++){
                JSONObject obj = offices.getJSONObject(k);
                JSONArray indicesArr = obj.getJSONArray("officialIndices");
                for(int n = 0; n < indicesArr.length(); n++){
                    position = obj.getString("name");
                    officialIndices.add(position);
                }
            }
            JSONArray officials = jObjMain.getJSONArray("officials");

            for (int i = 0; i < officials.length(); i++) {
                JSONObject jOfficial = officials.getJSONObject(i);
                //JSONObject nameObj = jStock.getJSONObject("name");
                //name = nameObj.getString("common");
                String name = jOfficial.getString("name");
                String party = jOfficial.getString("party");
                String photoUrl = null;
                if (jOfficial.has("photoUrl")) {
                    photoUrl = jOfficial.getString("photoUrl");
                }
                String address = "";
                if(jOfficial.has("address")){
                    JSONArray addressArr = jOfficial.getJSONArray("address");
                    JSONObject addressObj = addressArr.getJSONObject(0);
                    if (addressObj.has("line1")) {
                        address += addressObj.getString("line1") + " ";
                    }
                    if (addressObj.has("line2")) {
                        address += addressObj.getString("line2") + " ";
                    }
                    if (addressObj.has("line3")) {
                        address += addressObj.getString("line3") + " ";
                    }
                    address += addressObj.getString("city") + " ";
                    address += addressObj.getString("state") + " ";
                    address += addressObj.getString("zip") + " ";
                }
                String phone = null;
                if(jOfficial.has("phones")) {
                    JSONArray phoneArr = jOfficial.getJSONArray("phones");
                    phone = phoneArr.getString(0);
                }
                String facebook = null;
                String twitter = null;
                String youtube = null;
                String email = null;
                if (jOfficial.has("emails")) {
                    JSONArray emailArr = jOfficial.getJSONArray("emails");
                    email = emailArr.getString(0);
                }
                String url = null;
                if(jOfficial.has("urls")){
                    JSONArray urlArr = jOfficial.getJSONArray("urls");
                    url = urlArr.getString(0);
                }
                if(jOfficial.has("channels")) {
                    JSONArray channelArr = jOfficial.getJSONArray("channels");
                    for (int j = 0; j < channelArr.length(); j++) {
                        Object fbObj = channelArr.getJSONObject(j).get("type");
                        if (channelArr.getJSONObject(j).get("type").equals("Facebook")) {
                            facebook = channelArr.getJSONObject(j).getString("id");
                        }
                        if (channelArr.getJSONObject(j).get("type").equals("Youtube")) {
                            youtube = channelArr.getJSONObject(j).getString("id");
                        }
                        if (channelArr.getJSONObject(j).get("type").equals("Twitter")) {
                            twitter = channelArr.getJSONObject(j).getString("id");
                        }
                    }
                }
                position = officialIndices.get(i);
                official = new Official(address, position, name, party, phone, url, email, photoUrl, facebook, twitter, youtube);
                officialList.add(official);
            }
            //Collections.sort(stockList);
            return officialList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}



