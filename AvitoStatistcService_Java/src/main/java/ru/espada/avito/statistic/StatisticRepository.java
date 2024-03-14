package ru.espada.avito.statistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatisticRepository extends JpaRepository<StatisticEntity, Long> {

    Optional<List<StatisticEntity>> findStatisticEntitiesByMicroCategoryIdAndDateBetween(long microCategoryId, long dateStart, long dateEnd);
    int countAllByMicroCategoryIdAndLocationIdAndDate(long microCategoryId, long locationId, long date);
    int countAllByMicroCategoryIdAndLocationIdAndUserGroupIdAndDate(long microCategoryId, long locationId, long userGroupId, long date);

}
