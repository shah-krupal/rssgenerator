package com.example.rssgenerator.controllers;

import com.example.rssgenerator.models.RssFeed;
import com.example.rssgenerator.models.RssItem;
import com.example.rssgenerator.services.RssFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RssFeedController {
    @Autowired
    RssFeedService rssFeedService ;
    @PostMapping("/createfeed")
    public String generateItem(@RequestBody RssFeed rssFeed)
    {
        return rssFeedService.createFeed(rssFeed) ;
    }
}
