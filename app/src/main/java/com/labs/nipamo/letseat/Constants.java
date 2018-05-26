package com.labs.nipamo.letseat;

final class Constants {
    // Prevent instantiation
    private Constants(){}

    static final class Request {
        static final String RESULTS = "results";
        static final String STATUS = "status";
        static final String OK = "OK";
        static final String ZERO_RESULTS = "ZERO_RESULTS";
        static final String REQUEST_DENIED = "REQUEST_DENIED";
        static final String OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
        static final String UNKNOWN_ERROR = "UNKNOWN_ERROR";
        static final String INVALID_REQUEST = "INVALID_REQUEST";
    }

    static final class PlacesAPI {
        // Key for nearby places json from google
        static final String GEOMETRY = "geometry";
        static final String LOCATION = "location";
        static final String LATITUDE = "lat";
        static final String LONGITUDE = "lng";
        static final String NAME = "name";
        static final String VICINITY = "vicinity";
        static final String OPEN = "open_now";
        static final String RATING = "rating";
        static final String PRICE = "price_level";
        static final String PAGE = "next_page_token";
    }

    static final class Settings {
        static final String CURRENTSAVE = "currentSave";
        static final String CUSTOMSAVE = "customSave";
        static final String ZIPCODESAVE = "zipcodeSave";
        static final String PREFERENCES = "Prefs";
    }

    static final class Permissions
    {
        static final int REQUEST_FINE_LOCATION = 1;
        static final int REQUEST_COARSE_LOCATION = 2;
        static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 100;

    }
}
