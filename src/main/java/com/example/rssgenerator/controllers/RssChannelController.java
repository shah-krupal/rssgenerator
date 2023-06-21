package com.example.rssgenerator.controllers;

import com.example.rssgenerator.services.RssChannelService;
import com.rometools.rome.feed.rss.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RssChannelController {
    @Autowired
    RssChannelService rssChannelService ;
    @GetMapping("/rss/{feedId}")
    public Channel getRss(@PathVariable Integer feedId)
    {
        return rssChannelService.getRss(feedId) ;
    }
}
