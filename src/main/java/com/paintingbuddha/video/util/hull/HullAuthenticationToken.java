package com.paintingbuddha.video.util.hull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.paintingbuddha.video.domain.User;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class HullAuthenticationToken extends AbstractAuthenticationToken
{
    private final User user;

    public HullAuthenticationToken(User user)
    {
        super(buildAuthorities(user));
        this.user = user;
        setAuthenticated(true);
    }
    
    private static Collection<? extends GrantedAuthority> buildAuthorities(User user)
    {
        Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (user.isAdministrator())
        {
            roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return roles;
    }

    @Override
    public Object getCredentials()
    {
        return user.getName();
    }

    @Override
    public Object getPrincipal()
    {
        return user.getName();
    }
    
    public User getUser()
    {
        return user;
    }

}
