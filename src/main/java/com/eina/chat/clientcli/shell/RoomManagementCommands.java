package com.eina.chat.clientcli.shell;

import com.eina.chat.clientcli.services.StateKeeper;
import com.eina.chat.clientcli.utils.AsynchronousMessageWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
public class RoomManagementCommands {
    @Autowired
    StateKeeper stateKeeper;

    @Autowired
    AsynchronousMessageWriter asynchronousMessageWriter;

    @ShellMethod("Create a new chat room")
    @SuppressWarnings("unused")
    public String createRoom(String roomName) {
        // TODO: Implement
        return "Try to create room";
    }

    @ShellMethod("Delete the chat room")
    @SuppressWarnings("unused")
    public String deleteRoom(String roomName) {
        // TODO: Implement
        return "Try to delete room";
    }

    @ShellMethod("Add user to chat room")
    @SuppressWarnings("unused")
    public String addUserToRoom(String roomName, String username) {
        // TODO: Implement
        return "Try to create room";
    }

    @ShellMethod("Remove user from chat room")
    @SuppressWarnings("unused")
    public String removeUserFromRoom(String roomName, String username) {
        // TODO: Implement
        return "Try to create room";
    }

    @ShellMethodAvailability
    @SuppressWarnings("unused")
    public Availability authenticatedMethodsAvailability() {
        return stateKeeper.isAuthenticated()
                ? Availability.available()
                : Availability.unavailable("you are not authenticated");
    }
}
