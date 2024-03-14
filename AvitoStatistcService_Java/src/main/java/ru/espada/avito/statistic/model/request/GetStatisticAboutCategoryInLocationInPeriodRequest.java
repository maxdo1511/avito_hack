package ru.espada.avito.statistic.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// Запрос на получение предложения об изменении цены на продукт в локации в зависимости от времени
public class GetStatisticAboutCategoryInLocationInPeriodRequest {

    private long categoryId;
    private long locationId;
    private long dateStart;
    private long dateEnd;

}
