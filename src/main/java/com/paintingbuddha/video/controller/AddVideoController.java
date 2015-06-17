package com.paintingbuddha.video.controller;

import java.net.URL;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.paintingbuddha.video.backend.VideoBackend;
import com.paintingbuddha.video.domain.Video;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AddVideoController
{
    private final AmazonS3 s3Client;
    private final VideoBackend videoBackend;

    @Autowired
    public AddVideoController(AmazonS3 s3Client, VideoBackend videoBackend)
    {
        this.s3Client = s3Client;
        this.videoBackend = videoBackend;
    }
    
    @RequestMapping("/upload")
    public void upload()
    {
    }
    
    @RequestMapping("/generatePresignedUploadUrl")
    @ResponseBody
    public String generatePresignedUploadUrl(@RequestParam("file") String fileName, @RequestParam("mime") String mimeType)
    {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest("pbatest", "videos/upload/" + fileName);
        request.setExpiration(new DateTime().plusMinutes(60).toDate());
        request.setMethod(HttpMethod.PUT);
        request.setContentType(mimeType);
        
        URL presignedUrl = s3Client.generatePresignedUrl(request);
        return presignedUrl.toExternalForm();
    }
    
    @RequestMapping(value = "/uploadSuccessful", method = RequestMethod.PUT)
    @ResponseBody
    public long uploadSuccessful(@RequestParam("file") String fileName)
    {
        Video video = videoBackend.create(fileName);
        return video.getIdentifier();
    }
    
}
