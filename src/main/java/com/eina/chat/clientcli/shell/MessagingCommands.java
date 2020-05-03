package com.eina.chat.clientcli.shell;

import com.eina.chat.backendapi.protocol.packages.message.request.GetAuthLevelCommand;
import com.eina.chat.clientcli.services.BackEndCommunicator;
import com.eina.chat.clientcli.services.StateKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
public class MessagingCommands {

    @Autowired
    BackEndCommunicator backEndCommunicator;

    @Autowired
    StateKeeper stateKeeper;

    @ShellMethod("Send message to user")
    @SuppressWarnings("unused")
    public void sendMessageToUser(String username, String message) {
        backEndCommunicator.getSessionUser().send("/app/message", new GetAuthLevelCommand(1));
    }

    @ShellMethod("Send message to chat room")
    @SuppressWarnings("unused")
    public String sendMessageToRoom(String roomName, String message) {
        // TODO: Implement
        return "Try to create room";
    }

    @ShellMethod("Send file to user")
    @SuppressWarnings("unused")
    public String sendFileToUser(String username, String filePath) {
        // TODO: Implement
        return "Try to create room";
    }

    @ShellMethod("Send file to chat room")
    @SuppressWarnings("unused")
    public String sendFileToRoom(String roomName, String filePath) {
        // TODO: Implement
        return "Try to create room";
    }

    @ShellMethodAvailability
    @SuppressWarnings("unused")
    public Availability messagingMethodsAvailability() {
        return stateKeeper.isAuthenticated() && !stateKeeper.isAdmin()
                ? Availability.available()
                : Availability.unavailable("you are not authenticated");
    }
}
