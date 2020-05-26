package com.eina.chat.clientcli.services;

import org.springframework.stereotype.Service;

@Service
public class StateKeeper {
    private boolean isAuthenticated = false;
    private String username = null;
    private boolean isAdmin = false;

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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
