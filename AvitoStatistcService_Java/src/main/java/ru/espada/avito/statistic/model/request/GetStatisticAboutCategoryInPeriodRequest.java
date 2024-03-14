package ru.espada.avito.statistic.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// Запрос на получение предложения об изменении цены на продукт в зависимости от времени
public class GetStatisticAboutCategoryInPeriodRequest {

    private long categoryId;
    private long dateStart;
    private long dateEnd;

}
