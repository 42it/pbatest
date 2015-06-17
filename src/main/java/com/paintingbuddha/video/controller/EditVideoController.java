package com.paintingbuddha.video.controller;

import com.paintingbuddha.video.backend.VideoBackend;
import com.paintingbuddha.video.domain.Video;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EditVideoController
{
    private final VideoBackend videoBackend;

    @Autowired
    public EditVideoController(VideoBackend videoBackend)
    {
        this.videoBackend = videoBackend;
    }

    @RequestMapping(value = "/editVideo/{identifier}", method = RequestMethod.GET)
    public String edit(@PathVariable("identifier") long identifier, ModelMap modelMap)
    {
        modelMap.put("video", videoBackend.load(identifier));
        return "editVideo";
    }

    @RequestMapping(value = "/editVideo/{identifier}", method = RequestMethod.POST)
    public String editSave(@PathVariable("identifier") long identifier, @RequestParam("description") String description,
        @RequestParam(value = "published", required = false) boolean published, ModelMap modelMap)
    {
        Video video = videoBackend.load(identifier).withDescription(description);
        if (published)
        {
            video = video.publish();
        }
        else
        {
            video = video.unpublish();
        }
        videoBackend.save(video);
        modelMap.put("video", video);
        return "redirect:/editVideo/" + video.getIdentifier();
    }
}
