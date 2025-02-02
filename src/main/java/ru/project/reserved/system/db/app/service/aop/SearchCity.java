package ru.project.reserved.system.db.app.service.aop;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRequest;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.repository.HotelRepository;

import java.util.Optional;

@Aspect
@RequiredArgsConstructor
@Slf4j
public class SearchCity {
    private final HotelRepository hotelRepository;


    @Around(value = "@annotation(ru.project.reserved.system.db.app.service.aop.SearchEntity)")
    @SneakyThrows
    public Object searchCityInDb(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof HotelRequest hotelRequest) {
                Optional<Hotel> hotelOptional = hotelRepository.findHotelByName(hotelRequest.getName())
                        .filter(h ->
                                h.getCityList().stream()
                                        .filter(c -> c.getName().equals(hotelRequest.getCity().getName()))
                                        .isParallel()
                        );
                return hotelOptional.isPresent() ? hotelOptional.orElseThrow() : joinPoint.proceed();
            }
        }
        return joinPoint.proceed();
    }
}
