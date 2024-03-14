package ru.espada.avito.statistic.analyzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.espada.avito.statistic.StatisticEntity;
import ru.espada.avito.statistic.StatisticService;
import ru.espada.avito.statistic.model.response.GetStatisticAboutCategoryInPeriodResponse;
import ru.espada.avito.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Service
public class StatisticAnalyzerService {

    private StatisticService statisticService;

    public GetStatisticAboutCategoryInPeriodResponse analyzeCategoryInPeriod(long microCategoryId, long dateStart, long dateEnd) {
        // Преобразовываем даты в дни
        dateStart = DateUtils.getDays(dateStart);
        dateEnd = DateUtils.getDays(dateEnd);

        // Получаем статистику по категории за период
        List<StatisticEntity> statisticEntities = statisticService.getAllCategoryStatisticInPeriod(microCategoryId, dateStart, dateEnd);

        // Узнаём за какой период проверяем статистику
        // Для того, чтобы определить временные рамки сравнения статистики
        // Например если есть статистика за 10 дней, то сравниваем статистику за месяц, а если за месяц то проверяем статистику за год
        int deltaDays = DateUtils.getDeltaDays(dateStart, dateEnd);

        // сравнимаем месяца
        if (deltaDays < 30) {
            List<Integer> comparedStatistic = compareStatisticEntitiesMonth(statisticEntities, microCategoryId, dateStart, dateEnd);
            // Проверяем есть ли закономерность изменения статистики

        }
        return null;
    }

    /**
     * Сравниваем статистику за месяц
     * @param statisticEntities
     * @param microCategoryId
     * @param dateStart
     * @param dateEnd
     * @return массив сравнений статистики по количеству запросов. Каждый элемент массива - изменение статистики за день относительно самого мальенького количества просмотров в этот период
     */
    private List<Integer> compareStatisticEntitiesMonth(List<StatisticEntity> statisticEntities, long microCategoryId, long dateStart, long dateEnd) {
        long dateMonthBefore = dateStart - 30;
        long dateMonthAfter = dateEnd + 30;

        List<StatisticEntity> statisticEntitiesBefore = statisticService.getAllCategoryStatisticInPeriod(microCategoryId, dateMonthBefore, dateStart);
        List<StatisticEntity> statisticEntitiesAfter = statisticService.getAllCategoryStatisticInPeriod(microCategoryId, dateEnd, dateMonthAfter);

        // сравнимаем месяца по количеству запросов в категории

        // ищем самый низкий показатель просмотров
        AtomicInteger minCount = new AtomicInteger(Integer.MAX_VALUE);
        Stream.of(
                statisticEntitiesBefore.parallelStream(),
                statisticEntitiesAfter.parallelStream(),
                statisticEntities.parallelStream()
        ).flatMap(stream -> stream).forEach(statisticEntity ->
                minCount.set(Math.min(statisticEntity.getCount(), minCount.get()))
        );

        // Создаём массив изменений
        List<Integer> result = new ArrayList<>();

        for (StatisticEntity statisticEntity : statisticEntitiesBefore) {
            result.add(statisticEntity.getCount() - minCount.get());
        }

        for (StatisticEntity statisticEntity : statisticEntities) {
            result.add(statisticEntity.getCount() - minCount.get());
        }

        for (StatisticEntity statisticEntity : statisticEntitiesAfter) {
            result.add(statisticEntity.getCount() - minCount.get());
        }

        return result;
    }

    public String getTrendType(List<Integer> values, int delta) {
        if (values.size() <= 60) {
            return "Недостаточно значений для анализа";
        }

        List<Integer> valuesWithinContext = values.subList(30, values.size() - 30);
        int increasingCount = 0;
        int decreasingCount = 0;
        int previousValue = valuesWithinContext.get(0);
        int trendDelta = 0;

        for (int i = 1; i < valuesWithinContext.size(); i++) {
            int currentValue = valuesWithinContext.get(i);
            if (currentValue > previousValue + delta) {
                increasingCount++;
                trendDelta = Math.max(trendDelta, currentValue - previousValue);
            } else if (currentValue < previousValue - delta) {
                decreasingCount++;
                trendDelta = Math.max(trendDelta, previousValue - currentValue);
            }
            previousValue = currentValue;
        }

        if (increasingCount > 0 && decreasingCount == 0) {
            return "Монотонное возрастание с максимальной дельтой: " + trendDelta;
        } else if (decreasingCount > 0 && increasingCount == 0) {
            return "Монотонное убывание с максимальной дельтой: " + trendDelta;
        } else if (increasingCount == 0 && decreasingCount == 0) {
            return null;
        } else {
            return "Горка";
        }
    }

    @Autowired
    public void setStatisticService(StatisticService statisticService) {
        this.statisticService = statisticService;
    }
}
