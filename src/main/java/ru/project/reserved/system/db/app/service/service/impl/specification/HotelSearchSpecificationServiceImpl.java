package ru.project.reserved.system.db.app.service.service.impl.specification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.project.reserved.system.db.app.service.entity.Hotel;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelSearchSpecificationServiceImpl {

    public static Specification<Hotel> hotelSearch(Long id){
        return (root, query, criteriaBuilder) -> {
            if(id == 0){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }
}
