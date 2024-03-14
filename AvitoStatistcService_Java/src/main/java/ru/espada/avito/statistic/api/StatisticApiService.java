package ru.espada.avito.statistic.api;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.espada.avito.statistic.StatisticService;
import ru.espada.avito.statistic.analyzer.StatisticAnalyzerService;
import ru.espada.avito.statistic.model.request.GetStatisticAboutCategoryInPeriodRequest;
import ru.espada.avito.statistic.model.request.UserAskPriceRequest;
import ru.espada.avito.statistic.model.response.GetStatisticAboutCategoryInPeriodResponse;
import ru.espada.avito.utils.DateUtils;

import java.util.List;

@Service
public class StatisticApiService {

    private StatisticService statisticService;
    private StatisticAnalyzerService statisticAnalyzerService;


    /**
     * Сохраняет статистику пользователя для каждой категории, если у пользователя нет групп (путой список), тогда будет созранена только одна запись
     * @param userAskPriceRequest запрос
     */
    @Transactional
    public void userAskPrice(UserAskPriceRequest userAskPriceRequest) {
        List<Long> userCategoryIds = userAskPriceRequest.getUserCategoryIds();
        long days = DateUtils.getDays(userAskPriceRequest.getDate());
        if (userCategoryIds.isEmpty()) {
            statisticService.addNewStatistic(userAskPriceRequest.getCategoryId(), userAskPriceRequest.getLocationId(), days);
        }
        for (Long userCategoryId : userCategoryIds) {
            statisticService.addNewStatistic(userCategoryId, userAskPriceRequest.getCategoryId(), userAskPriceRequest.getLocationId(), days);
        }
    }

    /**
     * Получает статистику по категории за период, предлагает изменить цену, если прослеживается большие изменения в статистике
     * @param getStatisticAboutCategoryInPeriodRequest запрос
     * @return статистика
     */
    public GetStatisticAboutCategoryInPeriodResponse getStatisticAboutCategoryInPeriod(GetStatisticAboutCategoryInPeriodRequest getStatisticAboutCategoryInPeriodRequest) {
        return statisticAnalyzerService.analyzeCategoryInPeriod(getStatisticAboutCategoryInPeriodRequest.getCategoryId(),
                getStatisticAboutCategoryInPeriodRequest.getDateStart(),
                getStatisticAboutCategoryInPeriodRequest.getDateEnd());
    }

    @Autowired
    public void setStatisticService(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @Autowired
    public void setStatisticAnalyzerService(StatisticAnalyzerService statisticAnalyzerService) {
        this.statisticAnalyzerService = statisticAnalyzerService;
    }
}
