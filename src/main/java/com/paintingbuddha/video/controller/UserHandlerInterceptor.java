package com.paintingbuddha.video.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.paintingbuddha.video.domain.User;
import com.paintingbuddha.video.util.hull.HullAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class UserHandlerInterceptor extends HandlerInterceptorAdapter
{
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication instanceof HullAuthenticationToken)
        {
            modelAndView.addObject("user", ((HullAuthenticationToken) authentication).getUser());
        }
        else
        {
            modelAndView.addObject("user", User.unauthenticated());
        }
        super.postHandle(request, response, handler, modelAndView);
    }
}
