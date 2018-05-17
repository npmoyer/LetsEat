package com.labs.nipamo.letseat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public abstract class FindPlaces extends FindLocation {

    private RequestQueue mRequestQueue;
    private static FindPlaces mInstance;

    private String name, location, rating, price, lat, lng;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized FindPlaces getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void setAll(String name, String location, String rating, String price, String lat, String lng){
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.price = price;
        this.lat = lat;
        this.lng = lng;
    }

    public String getNameText(){
        return this.name;
    }

    public String getLocationText(){
        return this.location;
    }

    public String getRatingText(){
        return this.rating;
    }

    public String getPriceText(){
        return this.price;
    }

    public double getLatNum(){
        return Double.parseDouble(this.lat);
    }

    public double getLngNum(){
        return Double.parseDouble(this.lng);
    }
}
