package ru.project.reserved.system.db.app.service.aop;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRequest;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelResponse;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.exception.HotelException;
import ru.project.reserved.system.db.app.service.mapper.CityMapper;
import ru.project.reserved.system.db.app.service.mapper.HotelMapper;
import ru.project.reserved.system.db.app.service.repository.HotelRepository;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class SearchCity {
    private final HotelRepository hotelRepository;
    private final CityMapper cityMapper;


    @Around(value = "@annotation(ru.project.reserved.system.db.app.service.aop.SearchEntity)")
    @SneakyThrows
    public Object searchCityInDb(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof HotelRequest hotelRequest) {
                boolean findHotel = hotelRepository.existsByNameAndCity_Name(hotelRequest.getName(), hotelRequest.getCity().getName());
                if (findHotel) {
                    throw  new HotelException("Hotel " + hotelRequest.getName() + " already exists in city "
                            + hotelRequest.getCity().getName());
                }
            }
        }
        return joinPoint.proceed();
    }
}
