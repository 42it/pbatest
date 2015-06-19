package com.paintingbuddha.video.util.hull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HullClient
{
    private final static ObjectMapper MAPPER = new ObjectMapper();
    private final HullConfiguration config;
    private final HttpClient httpClient = new DefaultHttpClient();

    @Autowired
    public HullClient(HullConfiguration config)
    {
        this.config = config;
    }

    /**
     *
     * @param path
     * @return
     */
    public String get(String path)
    {
        return get(path, null);
    }

    /**
     *
     * @param path
     * @param params Query parameters to be appended to the path
     * @return
     */
    public String get(String path, Map<String, Object> params)
    {
        String queryString = null;

        if (params != null)
        {
            Iterator<String> iter = params.keySet().iterator();
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            while (iter.hasNext())
            {
                String key = iter.next();
                pairs.add(new BasicNameValuePair(key, String.valueOf(params.get(key))));
            }
            queryString = URLEncodedUtils.format(pairs, "utf-8");
        }

        HttpGet request = new HttpGet(apiPath(path, queryString));
        setDefaultHeaders(request);

        return executeRequest(request);
    }

    /**
     *
     * @param path
     * @return
     */
    public String delete(String path)
    {
        return delete(path, null);
    }

    /**
     *
     * @param path
     * @param params  Query parameters to be appended to the path
     * @return
     */
    public String delete(String path, Map<String, Object> params)
    {
        String queryString = null;

        if (params != null)
        {
            Iterator<String> iter = params.keySet().iterator();
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            while (iter.hasNext())
            {
                String key = iter.next();
                pairs.add(new BasicNameValuePair(key, String.valueOf(params.get(key))));
            }
            queryString = URLEncodedUtils.format(pairs, "utf-8");
        }

        HttpDelete request = new HttpDelete(apiPath(path, queryString));
        setDefaultHeaders(request);

        return executeRequest(request);
    }

    /**
     *
     * @param path
     * @return
     */
    public String post(String path)
    {
        return post(path, null);
    }

    /**
     *
     * @param path
     * @param params Body params to be included with the request
     * @return
     */
    public String post(String path, Map<String, Object> params)
    {
        HttpPost request = new HttpPost(apiPath(path, null));
        setDefaultHeaders(request);

        try
        {
            String x = MAPPER.writeValueAsString(params);
            request.setEntity(new StringEntity(x));
        }
        catch (Exception e)
        {
            throw new RuntimeException("Could not convert params to json string", e);
        }

        return executeRequest(request);
    }

    /**
     *
     * @param path
     * @return
     */
    public String put(String path)
    {
        return put(path, null);
    }

    /**
     *
     * @param path
     * @param params Body params to be included with the request
     * @return
     */
    public String put(String path, Map<String, Object> params)
    {
        HttpPut request = new HttpPut(apiPath(path, null));
        setDefaultHeaders(request);

        try
        {
            String x = MAPPER.writeValueAsString(params);
            request.setEntity(new StringEntity(x));
        }
        catch (Exception e)
        {
            throw new RuntimeException("Could not convert params to json string", e);
        }

        return executeRequest(request);
    }

    /**
     * Executes the HttpRequestBase and returns the response as a String
     * @param request
     * @return String representation of API response
     */
    public String executeRequest(HttpRequestBase request)
    {
        HttpResponse response = null;
        try
        {
            response = httpClient.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
            {
                result.append(line);
            }
            return result.toString();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Could not execute Hull request", e);
        }
    }

    /**
     * Constructs the full Hull API request URL
     * @param path
     * @param queryString
     * @return String of full request URL
     */
    protected String apiPath(String path, String queryString)
    {
        if (path != null && path.length() > 0 && !path.startsWith("/"))
        {
            path = "/" + path;
        }
        StringBuilder rval = new StringBuilder();
        rval.append(config.orgUrl).append("/api/v1").append(path);
        if (null != queryString && !queryString.isEmpty())
        {
            rval.append("?").append(queryString);
        }
        return rval.toString();
    }

    /**
     * Add default Hull headers to the request
     * @param req
     */
    private void setDefaultHeaders(HttpRequestBase req)
    {
        req.addHeader("Content-Type", "application/json");
        req.addHeader("Hull-App-Id", config.appId);
        req.addHeader("Hull-Access-Token", config.appSecret);
    }

}