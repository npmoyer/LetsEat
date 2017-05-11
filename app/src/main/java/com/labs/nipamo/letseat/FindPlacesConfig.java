package com.labs.nipamo.letseat;


public class FindPlacesConfig extends FindPlaces{
    // API Key
    public static final String APIKEY = "AIzaSyAZMaNIoaHaoakcShaJ8rfB8XkvcMK9CSc";

    // Public static variables
    public static final String RESULTS = "results";
    public static final String STATUS = "status";
    public static final String OK = "OK";
    public static final String ZERO_RESULTS = "ZERO_RESULTS";
    public static final String REQUEST_DENIED = "REQUEST_DENIED";
    public static final String OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
    public static final String UNKNOWN_ERROR = "UNKNOWN_ERROR";
    public static final String INVALID_REQUEST = "INVALID_REQUEST";

    // Key for nearby places json from google
    public static final String GEOMETRY = "geometry";
    public static final String LOCATION = "location";
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lng";
    public static final String NAME = "name";
    public static final String VICINITY = "vicinity";
    public static final String OPEN = "open_now";
    public static final String RATING = "rating";
    public static final String PRICE = "price_level";
    public static final String PAGE = "next_page_token";

    private int distance;

    public void setDistance(int dist) {
        this.distance = dist;
    }

    public int getDistance(){
        if (this.distance != 0) {
            return this.distance;
        }else {
            return this.distance = 804;
        }
    }
}
