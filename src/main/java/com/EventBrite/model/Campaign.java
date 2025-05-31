package com.EventBrite.model;

public class Campaign {
    private String name;
    private int impressions;
    private int clicks;
    private int conversions;

    public Campaign(String emailCampaign, int i, int i1, int i2) {
    }

    // Constructors, Getters, and Setters

    public double getConversionRate() {
        return impressions == 0 ? 0 : ((double) conversions / impressions) * 100;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImpressions() {
        return impressions;
    }

    public void setImpressions(int impressions) {
        this.impressions = impressions;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public int getConversions() {
        return conversions;
    }

    public void setConversions(int conversions) {
        this.conversions = conversions;
    }
}

