package com.example.rssgenerator.controllers;

import com.example.rssgenerator.models.RssItem;
import com.example.rssgenerator.services.RssItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RssItemController {
    @Autowired
    private RssItemService rssItemService ;

    @PostMapping("/createitem")
    public String createItem(@RequestBody RssItem rssItem)
    {
        return rssItemService.createItem(rssItem) ;
    }

    @PostMapping("/createandnotify")
    public String createandnotify(@RequestBody RssItem rssItem)
    {
        return rssItemService.createandnotify(rssItem) ;
    }

}
