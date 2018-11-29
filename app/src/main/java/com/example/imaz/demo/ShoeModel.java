package com.example.imaz.demo;

public class ShoeModel {
    String name,id,url;
    int cost;

    public ShoeModel(String name, String id, String url, int cost) {
        this.name = name;
        this.id = id;
        this.url = url;
        this.cost = cost;
    }
    public ShoeModel(String name, String id, int cost) {
        this.name = name;
        this.id = id;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public int getCost() {
        return cost;
    }
}
