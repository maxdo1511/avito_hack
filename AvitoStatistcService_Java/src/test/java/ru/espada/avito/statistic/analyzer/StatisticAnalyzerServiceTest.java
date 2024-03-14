package ru.espada.avito.statistic.analyzer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class StatisticAnalyzerServiceTest {


    private StatisticAnalyzerService statisticAnalyzerService;

    @Test
    void getTrendType() {
        List<Integer> longIncreasingList = IntStream.rangeClosed(1, 100).boxed().collect(Collectors.toList());
        List<Integer> longDecreasingList = IntStream.iterate(100, i -> i - 1).limit(100).boxed().collect(Collectors.toList());
        List<Integer> longPeakList = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            longPeakList.add(i);
        }
        for (int i = 49; i >= 1; i--) {
            longPeakList.add(i);
        }
        List<Integer> longRandomList = Arrays.asList(3, 7, 2, 9, 5, 8, 6, 4, 1, 10, 11, 12, 13, 8, 7, 6, 20, 15, 14, 16, 18, 19, 22, 21, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60);


        System.out.println(statisticAnalyzerService.getTrendType(longIncreasingList, 5));
        System.out.println(statisticAnalyzerService.getTrendType(longDecreasingList, 5));
        System.out.println(statisticAnalyzerService.getTrendType(longPeakList, 5));
        System.out.println(statisticAnalyzerService.getTrendType(longRandomList, 5));
    }

    @Autowired
    public void setStatisticAnalyzerService(StatisticAnalyzerService statisticAnalyzerService) {
        this.statisticAnalyzerService = statisticAnalyzerService;
    }
}
