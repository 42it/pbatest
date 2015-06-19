package com.paintingbuddha.video.filter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.paintingbuddha.video.domain.User;
import com.paintingbuddha.video.util.hull.HullUserRetriever;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserCache
{
    private static Map<String, User> USERS = new ConcurrentHashMap<String, User>();
    private final HullUserRetriever userRetriever;

    @Autowired
    public UserCache(HullUserRetriever userRetriever)
    {
        this.userRetriever = userRetriever;
    }
    
    public User get(String id)
    {
        User user = USERS.get(id);
        if (user == null)
        {
            user = userRetriever.get(id);
            USERS.put(id, user);
        }
        return user;
    }
}
