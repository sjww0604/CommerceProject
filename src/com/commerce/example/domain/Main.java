package com.commerce.example.domain;

import com.commerce.example.service.CommerceSystem;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 속성 : 각 카테고리별 상품 리스트에 추가 및 객체 전달
        // 전자제품
        Category electronics = createCategory("전자제품",
            new Product("Galaxy S25", 1200000, "최신 안드로이드 스마트폰", 3),
            new Product("iPhone 15", 1000000, "Apple의 23년식 최신 스마트폰", 10),
            new Product("iPhone 16", 1200000, "Apple의 24년식 최신 스마트폰", 5),
            new Product("iPhone 17", 1350000, "Apple의 25년식 최신 스마트폰", 3),
            new Product("MacBook Pro", 2400000, "M3 칩셋 노트북", 2),
            new Product("AirPods Pro", 350000, "노이즈 캔슬링 무선 이어폰", 5)
        );

        // 의류
        Category clothes = createCategory("의류",
            new Product("Red T-Shirt", 15000, "빨간 반팔티", 5),
            new Product("Black Jean", 30000, "까만 청바지", 42),
            new Product("White Socks", 3000, "흰 양말", 10),
            new Product("Gray Cap", 25000, "회색 모자", 60)
        );

        // 식품
        Category foods = createCategory("식품",
            new Product("Chocolate Bar", 2000, "달콤한 초콜릿", 30),
            new Product("Milk 1L", 2500, "신선한 우유", 80),
            new Product("3minutes Curry", 2000, "3분 카레", 50),
            new Product("Ice Americano 1L", 4000, "아아 1리터", 10)
        );

        // 가구
        Category furniture = createCategory("가구",
            new Product("Desk", 120000, "원목 책상", 10),
            new Product("Chair", 60000, "인체공학 의자", 25),
            new Product("Bed", 700000, "푹신한 침대", 20),
            new Product("Sofa", 550000, "소파", 5)
        );

        // CommerceSystem 객체 생성(카테고리 주입) 및 프로그램 실행(start 루프)
        List<Category> categories = Arrays.asList(electronics, clothes, foods, furniture);
        CommerceSystem system = new CommerceSystem(categories);
        system.start();

        // === 대용량 데이터 자동 생성 및 성능 테스트 ===
        System.out.println("\n[Step 1] 대용량 데이터 테스트 시작");
        com.commerce.example.PerformanceTest pt = new com.commerce.example.PerformanceTest();
        pt.compareSearchPerformance();
        System.out.println("[Step 1 완료] 대용량 데이터 테스트 종료\n");

        com.commerce.example.PerformanceTest.runPrefixSearchFromCategories(categories);
    }
    /* 헬퍼 메서드 추가 */
    private static Category createCategory(String name, Product ... products) {
        Category category = new Category(name);
        for (Product product : products) {
            category.addProduct(product);
        }
        return category;
    }
}