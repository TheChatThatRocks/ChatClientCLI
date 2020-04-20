package com.eina.chat.clientcli.shell;

import com.eina.chat.clientcli.services.StateKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class AdminMessagingCommands {
    @Autowired
    StateKeeper stateKeeper;

    @ShellMethod("Send message to all users")
    @SuppressWarnings("unused")
    public String sendMessageToAllUser(String message) {
        // TODO: Implement
        return "Try to send message to all users";
    }

    @SuppressWarnings("unused")
    public Availability sendMessageToAllUserAvailability() {
        return stateKeeper.isAuthenticated() && stateKeeper.isAdmin()
                ? Availability.available()
                : Availability.unavailable("you are not authenticated");
    }
}
