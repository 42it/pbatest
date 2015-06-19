package com.paintingbuddha.video.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.paintingbuddha.video.domain.User;
import com.paintingbuddha.video.util.hull.HullAuthenticationToken;
import com.paintingbuddha.video.util.hull.HullConfiguration;
import com.paintingbuddha.video.util.hull.HullUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class HullUserFilter implements Filter
{
    public static final String HULL_USER_ID_KEY = "Hull-User-Id";
    private static final String HULL_COOKIE_PREFIX = "hull_";
    private final String hullCookie;
    
    private final HullConfiguration config;
    private final UserCache userCache;

    @Autowired
    public HullUserFilter(HullConfiguration config, UserCache userCache)
    {
        this.config = config;
        this.userCache = userCache;
        this.hullCookie = HULL_COOKIE_PREFIX + config.appId;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;

        String cookieVal = HullUtils.getURLDecodedCookieValue(req.getCookies(), hullCookie);
        if (cookieVal != null)
        {
            String hullUserId = HullUtils.authenticateUser(cookieVal, config.appSecret);
            User user = userCache.get(hullUserId);
            SecurityContextHolder.getContext().setAuthentication(new HullAuthenticationToken(user));
        }
        chain.doFilter(request, response);
        SecurityContextHolder.clearContext();
    }

    @Override
    public void destroy()
    {
    }
}