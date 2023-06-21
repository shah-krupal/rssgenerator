package com.example.rssgenerator.repositories;

import com.example.rssgenerator.models.Stats;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatsRepo extends MongoRepository<Stats, String> {
}
