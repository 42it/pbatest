package com.paintingbuddha.video.controller;

import java.net.URL;

import com.amazonaws.services.s3.AmazonS3;
import com.paintingbuddha.video.backend.VideoBackend;
import com.paintingbuddha.video.domain.Video;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PlayVideoController
{
    private final AmazonS3 s3Client;
    private final VideoBackend videoBackend;

    @Autowired
    public PlayVideoController(AmazonS3 s3Client, VideoBackend videoBackend)
    {
        this.s3Client = s3Client;
        this.videoBackend = videoBackend;
    }

    @RequestMapping("/play")
    public String playRandalf(ModelMap modelMap)
    {
        URL presignedUrl = s3Client.generatePresignedUrl("pbatest", "videos/sd/randalf.mp4", new DateTime().plusMinutes(3).toDate());
        modelMap.put("videoUrl", presignedUrl.toExternalForm());
        return "play";
    }
    
    @RequestMapping("/play/{identifier}")
    public String play(@PathVariable("identifier") long identifier, ModelMap modelMap)
    {
        Video video = videoBackend.load(identifier);
        URL presignedUrl = s3Client.generatePresignedUrl("pbatest", "videos/upload/" + video.getFilename(), new DateTime().plusMinutes(3).toDate());
        modelMap.put("videoUrl", presignedUrl.toExternalForm());
        return "play";
    }
}