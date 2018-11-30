package com.example.imaz.demo;

public class ShoeModel {
    private String imageUrl;
  private  int cost;
    private String id,name;

    public ShoeModel(String ImageUrl, int cost, String id, String name) {
        this.imageUrl = ImageUrl;
        this.cost = cost;
        this.id = id;
        this.name = name;
    }

    public ShoeModel(String name, String id, int cost) {
        this.name = name;
        this.id = id;
        this.cost = cost;
    }
    public ShoeModel() {
    }

    public void setImageUrl(String imageUrl) {
         this.imageUrl = imageUrl;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getCost() {
        return cost;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
