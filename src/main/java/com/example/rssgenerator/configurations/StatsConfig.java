package com.example.rssgenerator.configurations;

import com.example.rssgenerator.models.Stats;
import com.example.rssgenerator.repositories.StatsRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
// This is original!!!!!
@Controller
public class StatsConfig {
    @Value("${spring.data.statName}")
    public String statName  ;
    public int noOfFeeds = 0 ;
    public int noOfItems = 0 ;
    @Autowired
    StatsRepo statsRepo ;
    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {   // Sets initial default value
        Boolean exists = true;
        try{
            exists = statsRepo.existsById(statName) ;
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        if(!exists)
        {
            try{
                Stats stats = new Stats() ;
                stats.setName(statName);
                stats.setNoOfFeeds(noOfFeeds);
                stats.setNoOfItems(noOfItems);
                statsRepo.save(stats) ;
                System.out.println("Default set");
            }catch (Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

    }
}
