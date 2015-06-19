package com.paintingbuddha.video.util.hull;

import java.util.Map;

import com.paintingbuddha.video.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HullUserRetriever
{
    private final HullClient hullClient;

    @Autowired
    public HullUserRetriever(HullClient hullClient)
    {
        this.hullClient = hullClient;
    }
    
    public User get(String id)
    {
        String string = hullClient.get(id);
        Map<String, Object> map = HullUtils.toMap(string);
        if ((Boolean) map.get("is_admin")) 
        {
            return User.admin((String) map.get("name"));
        }
        return User.user((String) map.get("name"));
    }

}
