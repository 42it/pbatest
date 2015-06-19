package com.paintingbuddha.video.domain;

public class User
{
    private static final User UNAUTHENTICATED = new UnauthenticatedUser();
    private final String name;
    private final boolean admin;

    public static User user(String name)
    {
        return new User(name, false);
    }
    
    public static User admin(String name)
    {
        return new User(name, true);
    }

    public static User unauthenticated()
    {
        return UNAUTHENTICATED;
    }
    
    private User(String name, boolean admin)
    {
        this.name = name;
        this.admin = admin;
    }

    public String getName()
    {
        return name;
    }

    public boolean isAuthenticated()
    {
        return true;
    }
    
    public boolean isAdministrator()
    {
        return admin;
    }

    private static class UnauthenticatedUser extends User
    {
        public UnauthenticatedUser()
        {
            super("", false);
        }

        @Override
        public boolean isAuthenticated()
        {
            return false;
        }

    }
}
