package com.eina.chat.clientcli.shell;

import com.eina.chat.clientcli.services.StateKeeper;
import com.eina.chat.clientcli.utils.AsynchronousMessageWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class AuthenticationCommands {
    @Autowired
    StateKeeper stateKeeper;

    @Autowired
    AsynchronousMessageWriter asynchronousMessageWriter;

    @ShellMethod("Login in the chat service")
    @SuppressWarnings("unused")
    public String login(String username, String password) {
        // TODO: Implement
        stateKeeper.setAuthenticated(true);
        stateKeeper.setUsername(username);
        if(username.equals("admin"))
            stateKeeper.setAdmin(true);
        return "Try to authenticate with: " + username + " " + password;
    }

    @ShellMethod("Sign-up in the chat service")
    @SuppressWarnings("unused")
    public String getAuthLevel() {
        // TODO: Implement get auth level
        return "Try to get auth level";
    }

    @ShellMethod("Logout from the chat service")
    @SuppressWarnings("unused")
    public String logout() {
        // TODO: Implement
        stateKeeper.setAuthenticated(false);
        stateKeeper.setUsername(null);
        return "Try to logout";
    }

    @SuppressWarnings("unused")
    public Availability loginAvailability() {
        return !stateKeeper.isAuthenticated() && !stateKeeper.isAdmin()
                ? Availability.available()
                : Availability.unavailable("you are authenticated yet. Please run logout first");
    }

    @SuppressWarnings("unused")
    public Availability getAuthLevelAvailability() {
        return !stateKeeper.isAuthenticated() && !stateKeeper.isAdmin()
                ? Availability.available()
                : Availability.unavailable("you are authenticated yet. Please run logout first");
    }

    @SuppressWarnings("unused")
    public Availability logoutAvailability() {
        return stateKeeper.isAuthenticated()
                ? Availability.available()
                : Availability.unavailable("you are not authenticated");
    }
}
