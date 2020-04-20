package com.eina.chat.clientcli.shell;

import com.eina.chat.clientcli.services.StateKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
public class RoomManagementCommands {
    @Autowired
    StateKeeper stateKeeper;

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

    @ShellMethod("Get rooms that the user administer")
    @SuppressWarnings("unused")
    public String getAdministeredRooms() {
        // TODO: Implement
        return "Try to get owned rooms";
    }

    @ShellMethod("Get rooms where the user is member")
    @SuppressWarnings("unused")
    public String getJoinedRooms() {
        // TODO: Implement
        return "Try to get joined rooms";
    }

    @ShellMethod("Get messages from room")
    @SuppressWarnings("unused")
    public String getMessagesFromRooms(String roomName) {
        // TODO: Implement wait until messages arrive
        return "Try to get joined rooms";
    }

    @ShellMethod("Get files from room")
    @SuppressWarnings("unused")
    public String getFilesFromRooms(String roomName) {
        // TODO: Implement wait until messages arrive
        return "Try to get joined rooms";
    }

    @ShellMethodAvailability
    @SuppressWarnings("unused")
    public Availability messagingMethodsAvailability() {
        return stateKeeper.isAuthenticated() && !stateKeeper.isAdmin()
                ? Availability.available()
                : Availability.unavailable("you are not authenticated");
    }
}
