package ru.project.reserved.system.db.app.service.dto.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MetricType {

    CREATE_HOTEL("create_hotel"),
    UPDATE_HOTEL("update_hotel"),
    DELETE_HOTEL("delete_hotel"),
    ALL_HOTELS("all_hotels"),
    CREATE_ROOM("create_room"),
    UPDATE_ROOM("update_room"),
    DELETE_ROOM("delete_room"),
    ALL_ROOMS("all_rooms"),
    FIND_ROOMS("find_rooms"),
    CREATE_RESERVATION("create_reservation"),
    UPDATE_RESERVATION("update_reservation"),
    DELETE_RESERVATION("delete_reservation"),
    CREATE_COMMENT("create_comment"),
    UPDATE_COMMENT("update_comment"),
    DELETE_COMMENT("delete_comment"),
    SEARCH_HOTEL("search_hotel"),
    ERROR("error");


    private final String type;
}
