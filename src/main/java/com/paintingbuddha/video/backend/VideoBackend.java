package com.paintingbuddha.video.backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.paintingbuddha.video.domain.Video;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class VideoBackend extends AbstractJdbcBackend
{
    private static final String CREATE_SQL = "INSERT INTO videos (filename) VALUES (?)";
    private static final String LOAD_BY_IDENTIFIER_SQL = "SELECT * from videos where identifier=?";
    private static final String LOAD_PUBLISHED_SQL = "SELECT * from videos where published=true";
    private static final String LOAD_ALL_SQL = "SELECT * from videos";
    private static final String SAVE_SQL = "UPDATE videos set description=?, published=? where identifier=?";
    
    @Autowired
    public VideoBackend(JdbcTemplate jdbcTemplate)
    {
        super(jdbcTemplate);
    }
    
    @Transactional
    public Video create(String filename)
    {
        long id = insert(CREATE_SQL, filename);
        return new Video(id, filename);
    }

    public Video load(long identifier)
    {
        return getByIdentifier(LOAD_BY_IDENTIFIER_SQL, identifier, new VideoRowMapper());
    }

    @Transactional
    public void save(Video video)
    {
        update(SAVE_SQL, video.getDescription(), video.isPublished(), video.getIdentifier());
    }

    public List<Video> listPublished()
    {
        return find(LOAD_PUBLISHED_SQL, null, new VideoRowMapper());
    }

    public List<Video> listAll()
    {
        return find(LOAD_ALL_SQL, null, new VideoRowMapper());
    }

    private static class VideoRowMapper implements RowMapper<Video>
    {
        @Override
        public Video mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            return new Video(rs.getLong("identifier"), rs.getString("filename"), rs.getString("description"), rs.getBoolean("published"));
        }
    }

}
