package com.eina.chat.clientcli.services;

import com.eina.chat.backendapi.protocol.packages.BasicPackage;
import com.eina.chat.backendapi.protocol.packages.common.response.OperationFailResponse;
import com.eina.chat.backendapi.protocol.packages.common.response.OperationSucceedResponse;
import com.eina.chat.backendapi.protocol.packages.message.response.*;
import com.eina.chat.backendapi.security.AccessLevels;
import com.eina.chat.clientcli.utils.AsynchronousMessageWriter;
import com.eina.chat.clientcli.utils.FileManagement;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BackEndCommunicator {
    /**
     * Received files path
     */
    @Value("${app.received-files-path:}")
    private String receivedFilesPath;

    @Autowired
    StateKeeper stateKeeper;

    @Autowired
    AsynchronousMessageWriter asynchronousMessageWriter;

    private StompSession sessionUser;

    private StompFrameHandler errorHandler;

    private boolean isRoleSet;

    public void start(StompSession sessionUser) {
        this.sessionUser = sessionUser;
        this.isRoleSet = false;
        this.errorHandler = new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return BasicPackage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                if (payload instanceof OperationSucceedResponse) {
                    asynchronousMessageWriter.println("Operation success");
                } else if (payload instanceof OperationFailResponse) {
                    asynchronousMessageWriter.println("Operation fail");
                } else {
                    asynchronousMessageWriter.println("Unidentified type of message received");
                }
            }

        };

        subscribeAuthLevelChannels();
    }

    private void subscribeAuthLevelChannels() {
        this.sessionUser.subscribe("/user/queue/error/auth-level", this.errorHandler);
        this.sessionUser.subscribe("/user/queue/auth-level", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return BasicPackage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                if (payload instanceof AuthLevelResponse) {
                    // Print auth level
                    asynchronousMessageWriter.println("Auth level: " + ((AuthLevelResponse) payload).getAuthLevel());

                    // Set role
                    if (!isRoleSet) {
                        isRoleSet = true;
                        boolean role = ((AuthLevelResponse) payload).getAuthLevel().equals(AccessLevels.ROLE_ADMIN);
                        stateKeeper.setAdmin(role);
                        if (role)
                            subscribeAdminChannels();
                        else
                            subscribeUserChannels();
                    }
                } else {
                    asynchronousMessageWriter.println("Auth-Level: Unidentified type of message received");
                }
            }
        });
    }

    private void subscribeUserChannels() {
        this.sessionUser.subscribe("/user/queue/error/message", this.errorHandler);
        this.sessionUser.subscribe("/user/queue/message", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return BasicPackage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                if (payload instanceof AdministeredRoomsResponse) {
                    List<String> roomsName = ((AdministeredRoomsResponse) payload).getRoomsName();
                    StringBuilder response = new StringBuilder("Administrated rooms:");

                    if (roomsName.isEmpty())
                        response.append("\n").append("Empty");

                    for (String roomName : roomsName)
                        response.append("\n").append(roomName);

                    asynchronousMessageWriter.println(response.toString());

                } else if (payload instanceof FileFromRoomResponse) {
                    String username = ((FileFromRoomResponse) payload).getFromUser();
                    String groupName = ((FileFromRoomResponse) payload).getFromRoom();
                    byte[] fileInBytesWithName = ((FileFromRoomResponse) payload).getFile();
                    Pair<String, byte[]> receivedFile = FileManagement.splitFileAndName(fileInBytesWithName);
                    FileManagement.createFolderIfNotExist(receivedFilesPath);
                    String fileName = Instant.now().getEpochSecond() + "_" + receivedFile.getLeft();
                    String receivedFilePath = Paths.get(receivedFilesPath, fileName).toString();

                    FileManagement.writeFile(receivedFilePath, receivedFile.getRight());

                    String response = "New file from group:\n" +
                            "From Group: " + groupName + "\n" +
                            "From User: " + username + "\n" +
                            "Saved in: " + receivedFilePath;
                    asynchronousMessageWriter.println(response);

                } else if (payload instanceof MessageFromRoomResponse) {
                    String username = ((MessageFromRoomResponse) payload).getFromUser();
                    String groupName = ((MessageFromRoomResponse) payload).getFromRoom();
                    String message = ((MessageFromRoomResponse) payload).getMessage();

                    String response = "New message from group:\n" +
                            "From Group: " + groupName + "\n" +
                            "From User: " + username + "\n" +
                            "Message: " + message;
                    asynchronousMessageWriter.println(response);

                } else if (payload instanceof FileFromUserResponse) {
                    String username = ((FileFromUserResponse) payload).getFrom();
                    byte[] fileInBytesWithName = ((FileFromUserResponse) payload).getFile();
                    Pair<String, byte[]> receivedFile = FileManagement.splitFileAndName(fileInBytesWithName);
                    FileManagement.createFolderIfNotExist(receivedFilesPath);
                    String fileName = Instant.now().getEpochSecond() + "_" + receivedFile.getLeft();
                    String receivedFilePath = Paths.get(receivedFilesPath, fileName).toString();
                    FileManagement.writeFile(receivedFilePath, receivedFile.getRight());

                    String response = "New file from user:\n" +
                            "From User: " + username + "\n" +
                            "Saved in: " + receivedFilePath;
                    asynchronousMessageWriter.println(response);

                } else if (payload instanceof MessageFromUserResponse) {
                    String username = ((MessageFromUserResponse) payload).getFrom();
                    String message = ((MessageFromUserResponse) payload).getMessage();

                    String response = "New file from user:\n" +
                            "From User: " + username + "\n" +
                            "Message: " + message;
                    asynchronousMessageWriter.println(response);

                } else if (payload instanceof FileHistoryFromRoomResponse) {
                    String roomName = ((FileHistoryFromRoomResponse) payload).getFromRoom();
                    StringBuilder response = new StringBuilder("File history from room:");
                    response.append("\n").append("From room: ").append(roomName);

                    List<Pair<String, byte[]>> groupFilesHistory = IntStream.range(0,
                            Math.min(((FileHistoryFromRoomResponse) payload).getFromUsers().size(),
                                    ((FileHistoryFromRoomResponse) payload).getFiles().size()))
                            .mapToObj(i -> new ImmutablePair<>(((FileHistoryFromRoomResponse) payload).getFromUsers().get(i),
                                    ((FileHistoryFromRoomResponse) payload).getFiles().get(i)))
                            .collect(Collectors.toList());

                    if (groupFilesHistory.isEmpty())
                        response.append("\n").append("Empty");

                    FileManagement.createFolderIfNotExist(receivedFilesPath);
                    String timeNow = String.valueOf(Instant.now().getEpochSecond());
                    for (int i = 0; i < groupFilesHistory.size(); i++) {
                        Pair<String, byte[]> usernameWithFile = groupFilesHistory.get(i);
                        String username = usernameWithFile.getLeft();
                        byte[] fileInBytesWithName = usernameWithFile.getRight();
                        Pair<String, byte[]> receivedFile = FileManagement.splitFileAndName(fileInBytesWithName);
                        String fileName = timeNow + "_" + i + "_" + receivedFile.getLeft();
                        String receivedFilePath = Paths.get(receivedFilesPath, fileName).toString();
                        FileManagement.writeFile(receivedFilePath, receivedFile.getRight());
                        response.append("\n").
                                append("----------------\n")
                                .append("From User: ").append(username).append("\n")
                                .append("Saved in: ").append(receivedFile.getLeft())
                                .append("----------------");

                    }
                    asynchronousMessageWriter.println(response.toString());

                } else if (payload instanceof MessageHistoryFromRoomResponse) {
                    String roomName = ((MessageHistoryFromRoomResponse) payload).getFromRoom();
                    StringBuilder response = new StringBuilder("Message history from room:");
                    response.append("\n").append("From room: ").append(roomName);

                    List<Pair<String, String>> groupMessageHistory = IntStream.range(0,
                            Math.min(((MessageHistoryFromRoomResponse) payload).getFromUsers().size(),
                                    ((MessageHistoryFromRoomResponse) payload).getMessages().size()))
                            .mapToObj(i -> new ImmutablePair<>(((MessageHistoryFromRoomResponse) payload).getFromUsers().get(i),
                                    ((MessageHistoryFromRoomResponse) payload).getMessages().get(i)))
                            .collect(Collectors.toList());

                    if (groupMessageHistory.isEmpty())
                        response.append("\n").append("Empty");

                    for (Pair<String, String> messageAndUser : groupMessageHistory) {
                        String username = messageAndUser.getLeft();
                        String message = messageAndUser.getRight();

                        response.append("\n").
                                append("----------------\n")
                                .append("From User: ").append(username).append("\n")
                                .append("Message: ").append(message)
                                .append("----------------");
                    }

                    asynchronousMessageWriter.println(response.toString());

                } else if (payload instanceof JoinedRoomsResponse) {
                    List<String> roomsName = ((JoinedRoomsResponse) payload).getRoomsName();
                    StringBuilder response = new StringBuilder("Joined rooms:");

                    if (roomsName.isEmpty())
                        response.append("\n").append("Empty");

                    for (String roomName : roomsName)
                        response.append("\n").append(roomName);

                    asynchronousMessageWriter.println(response.toString());
                } else {
                    asynchronousMessageWriter.println("Unidentified type of message received");
                }
            }
        });
    }

    private void subscribeAdminChannels() {
        this.sessionUser.subscribe("/user/queue/error/admin", this.errorHandler);
    }

    public StompSession getSessionUser() {
        return sessionUser;
    }

    public void stop() {
        sessionUser.disconnect();
    }
}
