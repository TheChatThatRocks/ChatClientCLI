package com.eina.chat.clientcli.services;

import org.springframework.stereotype.Service;

@Service
public class StateKeeper {
    private boolean isAuthenticated;
    private String username;

    public StateKeeper() {
        isAuthenticated = false;
        username = null;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
