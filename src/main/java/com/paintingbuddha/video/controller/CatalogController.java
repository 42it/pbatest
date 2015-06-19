package com.paintingbuddha.video.controller;

import com.paintingbuddha.video.backend.VideoBackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CatalogController
{
    private final VideoBackend videoBackend;

    @Autowired
    public CatalogController(VideoBackend videoBackend)
    {
        this.videoBackend = videoBackend;
    }
    
    @RequestMapping("/list")
    public String list(ModelMap modelMap)
    {
        modelMap.put("videos", videoBackend.listPublished());
        return "list";
    }

    @RequestMapping("/all")
    public String listAll(ModelMap modelMap)
    {
        modelMap.put("videos", videoBackend.listAll());
        return "listAll";
    }
}
