package com.paintingbuddha.video.domain;

public class Video
{
    private final long identifier;
    private final String filename;
    private final String description;
    private final boolean published;

    public Video(long identifier, String filename)
    {
        this(identifier, filename, null, false);
    }
    
    public Video(long identifier, String filename, String description, boolean published)
    {
        this.identifier = identifier;
        this.filename = filename;
        this.description = description;
        this.published = published;
    }
    
    public long getIdentifier()
    {
        return identifier;
    }

    public String getFilename()
    {
        return filename;
    }

    public String getDescription()
    {
        return description;
    }

    public boolean isPublished()
    {
        return published;
    }

    public Video withDescription(String description)
    {
        return new Video(this.identifier, this.filename, description, this.published);
    }

    public Video publish()
    {
        return new Video(this.identifier, this.filename, this.description, true);
    }
    
    public Video unpublish()
    {
        return new Video(this.identifier, this.filename, this.description, false);
    }
    
}
