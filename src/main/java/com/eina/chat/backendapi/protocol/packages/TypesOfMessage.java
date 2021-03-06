package com.eina.chat.backendapi.protocol.packages;
public interface TypesOfMessage{
    String ADD_ACCOUNT = "ADD_ACCOUNT";
    String CREATE_ROOM = "CREATE_ROOM";
    String DELETE_ROOM = "DELETE_ROOM";
    String ADD_USER_TO_CHAT_ROOM = "ADD_USER_TO_CHAT_ROOM";
    String DELETE_USER_FROM_CHAT_ROOM = "DELETE_USER_FROM_CHAT_ROOM";
    String SEND_MESSAGE_TO_USER = "SEND_MESSAGE_TO_USER";
    String SEND_MESSAGE_TO_ROOM = "SEND_MESSAGE_TO_ROOM";
    String SEND_FILE_TO_USER = "SEND_FILE_TO_USER";
    String SEND_FILE_TO_ROOM = "SEND_FILE_TO_ROOM";
    String DELETE_ACCOUNT = "DELETE_ACCOUNT";
    String OPERATION_SUCCEED = "OPERATION_SUCCEED";
    String OPERATION_FAIL = "OPERATION_FAIL";
    String MESSAGE_FROM_USER = "MESSAGE_FROM_USER";
    String MESSAGE_FROM_ROOM = "MESSAGE_FROM_ROOM";
    String FILE_FROM_USER = "FILE_FROM_USER";
    String FILE_FROM_ROOM = "FILE_FROM_ROOM";
    String SEND_MESSAGE_TO_ALL = "SEND_MESSAGE_TO_ALL";
    String CHECK_STATS = "CHECK_STATS";
    String STATS = "STATS";
    String GET_AUTH_LEVEL = "GET_AUTH_LEVEL";
    String GET_FILE_HISTORY_FROM_ROOM = "GET_FILE_HISTORY_FROM_ROOM";
    String GET_MESSAGE_HISTORY_FROM_ROOM = "GET_MESSAGE_HISTORY_FROM_ROOM";
    String GET_ADMINISTERED_ROOMS = "GET_ADMINISTERED_ROOMS";
    String GET_JOINED_ROOMS = "GET_JOINED_ROOMS";
    String AUTH_LEVEL = "AUTH_LEVEL";
    String FILE_HISTORY_FROM_ROOM = "FILE_HISTORY_FROM_ROOM";
    String MESSAGE_HISTORY_FROM_ROOM = "MESSAGE_HISTORY_FROM_ROOM";
    String ADMINISTERED_ROOMS = "ADMINISTERED_ROOMS";
    String JOINED_ROOMS = "JOINED_ROOMS";
}
