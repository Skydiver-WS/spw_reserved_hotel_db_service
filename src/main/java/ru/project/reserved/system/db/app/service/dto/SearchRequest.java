package ru.project.reserved.system.db.app.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRequest;
import ru.project.reserved.system.db.app.service.dto.room.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.type.SortType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchRequest {

    private SortType sortType;
    private List<HotelRequest> hotelRequestList;
    private List<RoomRequest> roomRequestList;
}
