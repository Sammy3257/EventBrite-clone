package com.EventBrite.model;

public class Venue {
    private int id;
    private String name;
    private String area;
    private String description;
    private double latitude;
    private double longitude;
    private int rating;
    private String imageUrl;

    // Constructor
    public Venue(int id, String name, String area, String description, double latitude, double longitude, int rating, String imageUrl) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}


