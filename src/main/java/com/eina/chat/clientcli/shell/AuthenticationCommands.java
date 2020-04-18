package com.eina.chat.clientcli.shell;

import com.eina.chat.clientcli.services.StateKeeper;
import com.eina.chat.clientcli.utils.AsynchronousMessageWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
public class AuthenticationCommands {
    @Autowired
    StateKeeper stateKeeper;

    @Autowired
    AsynchronousMessageWriter asynchronousMessageWriter;

    @ShellMethod("Login in the chat service")
    @ShellMethodAvailability("unauthenticatedMethodsAvailability")
    @SuppressWarnings("unused")
    public String login(String username, String password) {
        // TODO: Implement
        stateKeeper.setAuthenticated(true);
        stateKeeper.setUsername(username);
        return "Try to authenticate with: " + username + " " + password;
    }

    @ShellMethod("Logout from the chat service")
    @ShellMethodAvailability("authenticatedMethodsAvailability")
    @SuppressWarnings("unused")
    public String logout() {
        // TODO: Implement
        stateKeeper.setAuthenticated(false);
        stateKeeper.setUsername(null);
        return "Try to logout";
    }

    @SuppressWarnings("unused")
    public Availability authenticatedMethodsAvailability() {
        return stateKeeper.isAuthenticated()
                ? Availability.available()
                : Availability.unavailable("you are not authenticated");
    }

    @SuppressWarnings("unused")
    public Availability unauthenticatedMethodsAvailability() {
        return !stateKeeper.isAuthenticated()
                ? Availability.available()
                : Availability.unavailable("you are authenticated yet. Please run logout first");
    }
}
