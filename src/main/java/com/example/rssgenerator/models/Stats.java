package com.example.rssgenerator.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Stats")
public class Stats {
    @Id
    String name ;
    Integer noOfItems ;
    Integer noOfFeeds ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoOfItems() {
        return noOfItems;
    }

    public void setNoOfItems(int noOfItems) {
        this.noOfItems = noOfItems;
    }

    public int getNoOfFeeds() {
        return noOfFeeds;
    }

    public void setNoOfFeeds(int noOfFeeds) {
        this.noOfFeeds = noOfFeeds;
    }
}
