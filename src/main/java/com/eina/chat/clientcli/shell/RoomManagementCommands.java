package com.eina.chat.clientcli.shell;

import com.eina.chat.backendapi.protocol.packages.message.request.*;
import com.eina.chat.clientcli.services.BackEndCommunicator;
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

    @Autowired
    BackEndCommunicator backEndCommunicator;

    @ShellMethod("Create a new chat room")
    @SuppressWarnings("unused")
    public void createRoom(String roomName) {
        backEndCommunicator.getSessionUser().send("/app/message", new CreateRoomCommand(1, roomName));
    }

    @ShellMethod("Delete the chat room")
    @SuppressWarnings("unused")
    public void deleteRoom(String roomName) {
        backEndCommunicator.getSessionUser().send("/app/message", new DeleteRoomCommand(1, roomName));
    }

    @ShellMethod("Add user to chat room")
    @SuppressWarnings("unused")
    public void addUserToRoom(String roomName, String username) {
        backEndCommunicator.getSessionUser().send("/app/message", new AddUserToChatRoomCommand(1, username, roomName));
    }

    @ShellMethod("Remove user from chat room")
    @SuppressWarnings("unused")
    public void removeUserFromRoom(String roomName, String username) {
        backEndCommunicator.getSessionUser().send("/app/message", new RemoveUserFromChatRoom(1, username, roomName));
    }

    @ShellMethod("Get rooms that the user administer")
    @SuppressWarnings("unused")
    public void getAdministeredRooms() {
        backEndCommunicator.getSessionUser().send("/app/message", new GetAdministeredRoomsCommand(1));
    }

    @ShellMethod("Get rooms where the user is member")
    @SuppressWarnings("unused")
    public void getJoinedRooms() {
        backEndCommunicator.getSessionUser().send("/app/message", new GetJoinedRoomsCommand(1));
    }

    @ShellMethod("Get messages from room")
    @SuppressWarnings("unused")
    public void getMessagesFromRooms(String roomName) {
        backEndCommunicator.getSessionUser().send("/app/message", new GetMessageHistoryFromRoomCommand(1, roomName));
    }

    @ShellMethod("Get files from room")
    @SuppressWarnings("unused")
    public void getFilesFromRooms(String roomName) {
        backEndCommunicator.getSessionUser().send("/app/message", new GetFileHistoryFromRoomCommand(1, roomName));
    }

    @ShellMethodAvailability
    @SuppressWarnings("unused")
    public Availability messagingMethodsAvailability() {
        return stateKeeper.isAuthenticated() && !stateKeeper.isAdmin()
                ? Availability.available()
                : Availability.unavailable("you are not authenticated");
    }
}
