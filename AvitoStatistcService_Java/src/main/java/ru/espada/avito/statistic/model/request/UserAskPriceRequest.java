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
// Зпрос для изменеия статистики пользователей (после того как пользователь запросил цену)
public class UserAskPriceRequest {

    private ArrayList<Long> userCategoryIds;
    private long categoryId;
    private long locationId;
    private long date;

}
