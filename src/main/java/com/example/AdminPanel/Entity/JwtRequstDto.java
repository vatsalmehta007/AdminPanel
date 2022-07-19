package com.example.AdminPanel.Entity;

import java.io.Serializable;

public class JwtRequstDto implements Serializable
{
    private static final long serialVersionUID = 5926468583005150707L;

    public String Username;
    public String Password;

    public JwtRequstDto( )
    {


    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
