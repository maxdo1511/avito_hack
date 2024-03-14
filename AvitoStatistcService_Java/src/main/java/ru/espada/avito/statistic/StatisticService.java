package ru.espada.avito.statistic;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.espada.avito.statistic.exception.StatisticException;

import java.util.List;

@Service
public class StatisticService {


    private StatisticRepository statisticRepository;

    public void addNewStatistic(long categoryId, long locationId, long date) {
        int count = statisticRepository.countAllByMicroCategoryIdAndLocationIdAndDate(categoryId, locationId, date);
        StatisticEntity statisticEntity = StatisticEntity.builder()
                .userGroupId(null)
                .microCategoryId(categoryId)
                .locationId(locationId)
                .date(date)
                .count(count+1)
                .build();

        statisticRepository.save(statisticEntity);
    }

    public void addNewStatistic(long userGroupId, long categoryId, long locationId, long date) {
        int count = statisticRepository.countAllByMicroCategoryIdAndLocationIdAndDate(categoryId, locationId, date);
        StatisticEntity statisticEntity = StatisticEntity.builder()
                .userGroupId(userGroupId)
                .microCategoryId(categoryId)
                .locationId(locationId)
                .date(date)
                .count(count+1)
                .build();

        statisticRepository.save(statisticEntity);
    }

    public List<StatisticEntity> getAllStatistics() {
        return statisticRepository.findAll();
    }

    public List<StatisticEntity> getAllCategoryStatisticInPeriod(long categoryId, long dateStart, long dateEnd) {
        return statisticRepository.findStatisticEntitiesByMicroCategoryIdAndDateBetween(categoryId, dateStart, dateEnd)
                .orElseThrow(() -> new StatisticException("Не удалось получить статистику"));
    }

    @Autowired
    public void setStatisticRepository(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }
}
