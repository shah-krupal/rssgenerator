package com.example.rssgenerator.services;

import com.example.rssgenerator.models.RssFeed;
import com.example.rssgenerator.models.RssItem;
import com.rometools.rome.io.FeedException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface RssFeedService {
    public String createFeed(RssFeed rssFeed) ;
    public void informHook(RssFeed rssFeed) ;

    public void trying (RssItem rssItem) throws FeedException, IOException;
}
