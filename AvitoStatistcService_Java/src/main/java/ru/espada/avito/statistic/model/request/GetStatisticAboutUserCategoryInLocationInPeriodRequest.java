package ru.espada.avito.statistic.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// Запрос на получение предложения об изменении цены на продукт для конкретных групп пользователей в категории для локации в зависимости от времени
public class GetStatisticAboutUserCategoryInLocationInPeriodRequest {

    private ArrayList<Long> userCategoryIds;
    private long categoryId;
    private long locationId;
    private long dateStart;
    private long dateEnd;

}
