package ru.project.reserved.system.db.app.service.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum KeyType {
    CREATE_ROOM("create-room"),
    UPDATE_ROOM("update-room"),
    REMOVE_ROOM("delete-room"),
    READ_ROOM("read-room"),

    CREATE_HOTEL("create-hotel"),
    UPDATE_HOTEL("update-hotel"),
    REMOVE_HOTEL("delete-hotel"),
    READ_HOTEL("read-hotel");
    
    private final String key;
}
