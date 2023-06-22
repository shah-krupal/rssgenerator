package com.example.rssgenerator.services.serviceImpls;

import com.example.rssgenerator.models.RssFeed;
import com.example.rssgenerator.models.RssItem;
import com.example.rssgenerator.models.Stats;
import com.example.rssgenerator.repositories.RssFeedRepo;
import com.example.rssgenerator.repositories.RssItemRepo;
import com.example.rssgenerator.services.RssItemService;
import com.rometools.rome.feed.module.impl.ModuleUtils;
import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Guid;
import com.rometools.rome.feed.rss.Item;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.Date;

@Service
public class RssItemServiceImpl implements RssItemService {
    @Autowired
    RssItemRepo rssItemRepo;
    @Autowired
    RssFeedRepo rssFeedRepo;
    @Autowired
    MongoOperations mongoOperations;
    @Autowired
    MongoTemplate mongoTemplate;
    @Value("${spring.data.statName}")
    String statName;

    @Override
    @Transactional
    public String createItem(RssItem rssItem) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("name").is(statName));

            Update update = new Update();
            update.inc("noOfItems", 1);

            Stats newstats = mongoOperations.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
                    Stats.class);
            assert newstats != null;

            rssItem.setItemId(newstats.getNoOfItems());
            rssItem.setPubDate(new Date()); // today's date
            rssItemRepo.save(rssItem);

            System.out.println("Rss Item Added Successfully");
            Item item = convertToItem(rssItem);

            return "Item Added Successfully";
        } catch (Exception e) {
            System.out.println("Item Not Added");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "Item Not Added";
        }
    }

    @Override
    public Item convertToItem(RssItem rssItem) {
        Item item = new Item(); // ROME RSS item
        item.setAuthor(rssItem.getCreator());
        item.setTitle(rssItem.getTitle());
        Description description = new Description();
        description.setType("text/plain");
        description.setValue(rssItem.getDescription());
        item.setDescription(description);
        item.setPubDate(rssItem.getPubDate());
        Guid guid = new Guid();
        guid.setValue("https://permalink.com" + Integer.toString(rssItem.getItemId()));
        guid.setPermaLink(false); // Set to true if the GUID is a permalink
        item.setGuid(guid);
        item.setUri(rssItem.getLink());

        Query query = new Query();
        query.addCriteria(Criteria.where("feedId").is(rssItem.getFeedId()));

        Update update = new Update();
        update.addToSet("itemList").each(item);
        try {
            mongoTemplate.upsert(query, update, RssFeed.class); // Saving to corresp. feed
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public Channel convertToFeed(Item item, RssItem rssItem) {
        Channel channel = new Channel();
        channel.setFeedType("rss_2.0");
        Namespace atomNamespace = Namespace.getNamespace("atom", "http://www.w3.org/2005/Atom");
        Element atomLink = new Element("link", atomNamespace);
        atomLink.setAttribute("href", "https://rssproj.onrender.com/rss/1");
        atomLink.setAttribute("rel", "self");
        atomLink.setAttribute("type", "application/rss+xml");
        channel.getForeignMarkup().add(atomLink);
        RssFeed rssFeed = rssFeedRepo.findByFeedId(rssItem.getFeedId());
        channel.setTitle(rssFeed.getTitle());
        channel.setGenerator(rssFeed.getGenerator());
        channel.setDescription(rssFeed.getDescription());
        channel.setUri(rssFeed.getUrl());
        channel.setItems(Collections.singletonList(item));
        channel.setPubDate(new Date());
        channel.setLink("http://localhost.com"); // Not sure what should come
        System.out.println("rssItem " + rssFeed.getTitle());
        System.out.println("Channel " + channel.getTitle());
        return channel;
    }

    @Override
    public String notifyHook(Channel channel) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_RSS_XML);

        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Message converters
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        // Build the request entity
        HttpEntity<Channel> requestEntity = new HttpEntity<>(channel, requestHeaders);

        URI url = URI.create("http://localhost:8081/webhook"); // URL of webhook receiver

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                    String.class);
            System.out.println("successfully notified");
            return responseEntity.getBody();
        } catch (Exception e) {
            System.out.println("Not able to notify");
            e.printStackTrace();
            return e.getMessage();
        }

    }

    @Override
    public String createandnotify(RssItem rssItem) {
        try {
            createItem(rssItem);
            Item item = convertToItem(rssItem);
            Channel channel = convertToFeed(item, rssItem);
            String status = notifyHook(channel);
            return status;
        } catch (Exception e) {
            System.out.println("ERR!!");
            e.printStackTrace();
            return e.getMessage();
        }

    }

}
