package com.example.rssgenerator.repositories;

import com.example.rssgenerator.models.RssFeed;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RssFeedRepo extends MongoRepository<RssFeed, Integer> {
    RssFeed findByFeedId(Integer feedId);
}
