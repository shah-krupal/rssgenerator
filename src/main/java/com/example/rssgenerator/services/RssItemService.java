package com.example.rssgenerator.services;

import com.example.rssgenerator.models.RssItem;
import com.example.rssgenerator.repositories.StatsRepo;
import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface RssItemService {

    public String createItem(RssItem rssItem) ;
    public Item convertToItem(RssItem rssItem) ;
    public Channel convertToFeed(Item item, RssItem rssItem) ;
    public String notifyHook(Channel channel) ;
    public String createandnotify(RssItem rssItem) ;
}
