package com.example.rssgenerator.services;

import com.rometools.rome.feed.rss.Channel;
import org.springframework.stereotype.Service;

@Service
public interface RssChannelService {
    public Channel getRss(Integer feedId);
}
