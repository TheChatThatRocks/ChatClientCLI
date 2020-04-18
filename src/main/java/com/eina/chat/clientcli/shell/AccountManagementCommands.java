package com.eina.chat.clientcli.shell;

import com.eina.chat.clientcli.services.StateKeeper;
import com.eina.chat.clientcli.utils.AsynchronousMessageWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
public class AccountManagementCommands {
    @Autowired
    StateKeeper stateKeeper;

    @Autowired
    AsynchronousMessageWriter asynchronousMessageWriter;

    @ShellMethod("Sign-up in the chat service")
    @ShellMethodAvailability("unauthenticatedMethodsAvailability")
    @SuppressWarnings("unused")
    public String signUp(String username, String password) {
        // TODO: Implement sign-up and login
        stateKeeper.setAuthenticated(true);
        stateKeeper.setUsername(username);
        return "Try to authenticate with: " + username + " " + password;
    }

    @ShellMethod("Sign-up in the chat service")
    @ShellMethodAvailability("authenticatedMethodsAvailability")
    @SuppressWarnings("unused")
    public String deleteAccount() {
        // TODO: Implement sign-up and login
        stateKeeper.setAuthenticated(false);
        stateKeeper.setUsername(null);
        return "Try to delete account";
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
