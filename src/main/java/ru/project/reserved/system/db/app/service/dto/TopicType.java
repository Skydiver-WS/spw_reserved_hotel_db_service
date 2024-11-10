package ru.project.reserved.system.db.app.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TopicType {
    CREATE_UPDATE_HOTEL("create-update-hotel"),
    FIND_ALL_HOTEL("find-all-hotel"),
    FIND_BY_PARAMETER_HOTEL("find-by-parameter-hotel"),
    REMOVE_HOTEL("remove-hotel"),
    CREATE_UPDATE_ROOM("create-update-room"),
    FIND_ALL_ROOM("find-all-room"),
    FIND_BY_PARAMETER_ROOM("find-by-parameter-room"),
    REMOVE_ROOM("remove-room"),
    HOTEL_RESPONSE("hotel-response"),
    ROOM_RESPONSE("room-response");

    private final String topic;
}
