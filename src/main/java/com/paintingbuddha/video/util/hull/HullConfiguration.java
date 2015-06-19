package com.paintingbuddha.video.util.hull;

public class HullConfiguration
{
    public final String appId;
    public final String appSecret;
    public final String orgUrl;

    public HullConfiguration(String appId, String appSecret, String orgUrl)
    {
        this.appId = appId;
        this.appSecret = appSecret;
        this.orgUrl = orgUrl;
    }
}
