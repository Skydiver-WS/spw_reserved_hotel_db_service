package ru.project.reserved.system.db.app.service.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.repository.CommentRepository;
import ru.project.reserved.system.db.app.service.repository.HotelRepository;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
public class UpdateRatingHotels {

    private final HotelRepository hotelRepository;
    private final CommentRepository commentRepository;

    @Scheduled(cron = "${scheduler.rating}")
    @Async
    @Transactional
    public void updateRatingHotels() {
        log.info("Start update rating hotels");
        Optional<List<Object[]>> dataOptional = commentRepository.getHotelAvgRatings();
        if (dataOptional.isPresent()) {
            List<Object[]> hotelAvgRatings = dataOptional.get();
            hotelAvgRatings.stream()
                    .filter(h -> !h[1].equals(h[2]))
                    .forEach(result -> {
                        Long hotelId = (Long) result[0];
                        Double avgRating = (Double) result[2];
                        Hotel hotel = hotelRepository.findById(hotelId).orElse(null);
                        hotel.setRating(avgRating);
                        hotel.setCountComments(commentRepository.countComments(hotelId));
                        hotelRepository.save(hotel);
                        log.info("Rating hotels {} updated success", hotelId);
                    });
        }
    }


}
