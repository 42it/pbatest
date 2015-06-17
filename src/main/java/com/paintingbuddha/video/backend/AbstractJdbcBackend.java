package com.paintingbuddha.video.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public abstract class AbstractJdbcBackend
{
    private final JdbcTemplate template;

    protected AbstractJdbcBackend(JdbcTemplate template)
    {
        this.template = template;
    }

    public long insert(final String sql, final Object... parameters)
    {
        try
        {
            return insertWithoutCatchException(sql, parameters);
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    public long insertWithoutCatchException(final String sql, final Object... parameters)
    {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(
            new PreparedStatementCreator()
            {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
                {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);

                    for (int i = 1; i < parameters.length + 1; i++)
                    {
                        preparedStatement.setObject(i, parameters[i - 1]);
                    }

                    return preparedStatement;
                }
            }, keyHolder);

        return ((Number) keyHolder.getKeys().get("identifier")).longValue();
    }

    public <T> T getByIdentifier(String sql, long identifier, RowMapper<T> rowMapper)
    {
        try
        {
            return template.queryForObject(sql, rowMapper, identifier);
        }
        catch (EmptyResultDataAccessException e)
        {
            return null;
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    public <T> T get(String sql, Object[] parameters, RowMapper<T> rowMapper)
    {
        try
        {
            return template.queryForObject(sql, rowMapper, parameters);
        }
        catch (EmptyResultDataAccessException e)
        {
            return null;
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> find(String sql, Object[] parameters, RowMapper<T> rowMapper)
    {
        try
        {
            return template.query(sql, parameters, rowMapper);
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void delete(String sql, Object... parameters)
    {
        try
        {
            template.update(sql, parameters);
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void update(final String sql, Object... parameters)
    {
        try
        {
            template.update(sql, parameters);
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    public String getBooleanChar(boolean value)
    {
        return value ? "T" : "F";
    }

    public Timestamp getTimestamp(LocalDateTime localDateTime)
    {
        return localDateTime != null ? new Timestamp(localDateTime.toDateTime().getMillis()) : null;
    }
}
