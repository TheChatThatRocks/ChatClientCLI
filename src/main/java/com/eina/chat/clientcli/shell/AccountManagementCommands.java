package com.eina.chat.clientcli.shell;

import com.eina.chat.clientcli.services.StateKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class AccountManagementCommands {
    @Autowired
    StateKeeper stateKeeper;

    @ShellMethod("Sign-up in the chat service")
    @SuppressWarnings("unused")
    public String signUp(String username, String password) {
        // TODO: Implement sign-up, login, and get auth level
        stateKeeper.setAuthenticated(true);
        stateKeeper.setUsername(username);
        if(username.equals("admin"))
            stateKeeper.setAdmin(true);
        return "Try to authenticate with: " + username + " " + password;
    }

    @ShellMethod("Delete account in the chat service")
    @SuppressWarnings("unused")
    public String deleteAccount() {
        // TODO: Implement sign-up and login
        stateKeeper.setAuthenticated(false);
        stateKeeper.setUsername(null);
        return "Try to delete account";
    }

    @SuppressWarnings("unused")
    public Availability signUpAvailability() {
        return !stateKeeper.isAuthenticated()
                ? Availability.available()
                : Availability.unavailable("you are authenticated yet. Please run logout first");
    }

    @SuppressWarnings("unused")
    public Availability deleteAccountAvailability() {
        return stateKeeper.isAuthenticated() && !stateKeeper.isAdmin()
                ? Availability.available()
                : Availability.unavailable("you are not authenticated");
    }
}
