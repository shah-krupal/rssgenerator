package com.example.rssgenerator.repositories;

import com.example.rssgenerator.models.RssItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RssItemRepo extends MongoRepository<RssItem, Integer> {
    Iterable<RssItem> findTop10ByFeedId(Integer feedId);
}
