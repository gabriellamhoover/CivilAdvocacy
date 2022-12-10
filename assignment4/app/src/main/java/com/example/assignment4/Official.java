package com.example.assignment4;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Official implements  Comparable<Official>, Serializable {
    private final String address;
    private final String name;
    private String party;
    private String phone;
    private String url;
    private String email;
    private String photoUrl;
    private String facebook;
    private String twitter;
    private String youtube;
    private String position;



    public Official(String address, String position, String name, String party, String phone, String url, String email, String photoUrl, String facebook, String twitter, String youtube) {
        this.address = address;
        this.name = name;
        this.party = party;
        this.phone = phone;
        this.url = url;
        this.email = email;
        this.photoUrl = photoUrl;
        this.facebook = facebook;
        this.twitter = twitter;
        this.youtube = youtube;
        this.position = position;

    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public String getPhone() {
        return phone;
    }

    public String getUrl() {
        return url;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getYoutube() {
        return youtube;
    }


    public String getPosition() {
        return position;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("name", name);
        obj.put("party", party);
        obj.put("address", address);
        obj.put("url", url);
        obj.put("photoUrl", photoUrl);
        obj.put("email", email);
        obj.put("facebook", facebook);
        obj.put("twitter", twitter);
        obj.put("youtube", youtube);
        obj.put("phone", phone);
        obj.put("position", position);
        return obj;
    }

    @Override
    public int compareTo(Official official) {
        return (this.name.compareTo(official.getName()));
    }
}

