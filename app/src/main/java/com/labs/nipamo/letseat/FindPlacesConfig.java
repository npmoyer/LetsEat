package com.labs.nipamo.letseat;


public class FindPlacesConfig extends FindPlaces{
    private int distance;
    private String category, rating, price;

    private String[] places = new String[50];

    public void setDistance(int dist) {
        this.distance = dist;
    }

    public void setCategory(String cat){
        this.category = cat;
    }

    public void setRating (String rat){
        this.rating = rat;
    }

    public void setPrice (String pri){
        this.price = pri;
    }

    public int getDistance(){
        if (this.distance != 0) {
            return this.distance;
        }else {
            return this.distance = 804;
        }
    }

    public void setPlaces(String place, int index){
        this.places[index] = place;
    }

    public String getCategory(){
        return this.category;
    }

    public String getRating(){
        return this.rating;
    }

    public String getPrice(){
        return this.price;
    }

    public String[] getPlaces(){
        return this.places;
    }
}
