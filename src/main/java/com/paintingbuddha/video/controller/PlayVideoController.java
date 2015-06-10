package com.paintingbuddha.video.controller;

import java.net.URL;

import com.amazonaws.services.s3.AmazonS3;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PlayVideoController
{
    private final AmazonS3 s3Client;

    @Autowired
    public PlayVideoController(AmazonS3 s3Client)
    {
        this.s3Client = s3Client;
    }

    @RequestMapping("/play")
    public void play(ModelMap modelMap)
    {
        URL presignedUrl = s3Client.generatePresignedUrl("pbatest", "videos/sd/randalf.mp4", new DateTime().plusMinutes(3).toDate());
        modelMap.put("videoUrl", presignedUrl.toExternalForm());
    }
}