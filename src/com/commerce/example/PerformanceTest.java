package com.commerce.example;

public class PerformanceTest {
    public void compareSearchPerformance() {
        // 완전탐색 시간 측정
        long linearTime = measureLinearSearch();

        // 이진탐색 시간 측정
        long binaryTime = measureBinarySearch();

        // 결과 출력
        System.out.println("완전탐색: " + linearTime + "ns");
        System.out.println("이진탐색: " + binaryTime + "ns");
        System.out.println("성능 향상: " + (linearTime / binaryTime) + "배");
    }
}

