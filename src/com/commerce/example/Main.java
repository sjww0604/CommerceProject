package com.commerce.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 속성 : 각 카테고리별 상품 리스트에 추가 및 객체 전달
        // 전자제품
        Category electronics = new Category("전자제품");
        electronics.addProduct(new Product("Galaxy S25", 1200000, "최신 안드로이드 스마트폰", 30));
        electronics.addProduct(new Product("iPhone 16", 1350000, "Apple의 최신 스마트폰", 25));
        electronics.addProduct(new Product("MacBook Pro", 2400000, "M3 칩셋 노트북", 12));
        electronics.addProduct(new Product("AirPods Pro", 350000, "노이즈 캔슬링 무선 이어폰", 50));

        // 의류
        Category clothes = new Category("의류");
        clothes.addProduct(new Product("Red T-Shirt", 15000, "빨간 반팔티", 5));
        clothes.addProduct(new Product("Black Jean", 30000, "까만 청바지", 42));
        clothes.addProduct(new Product("White Socks", 3000, "흰 양말", 10));
        clothes.addProduct(new Product("Gray Cap", 25000, "회색 모자", 60));

        // 식품
        Category foods = new Category("식품");
        foods.addProduct(new Product("Chocolate Bar", 2000, "달콤한 초콜릿", 30));
        foods.addProduct(new Product("Milk 1L", 2500, "신선한 우유", 80));
        foods.addProduct(new Product("3minutes Curry", 2000, "3분 카레", 50));
        foods.addProduct(new Product("Ice Americano 1L", 4000, "아아 1리터", 10));

        // 가구
        Category furniture = new Category("가구");
        furniture.addProduct(new Product("Desk", 120000, "원목 책상", 10));
        furniture.addProduct(new Product("Chair", 60000, "인체공학 의자", 25));
        furniture.addProduct(new Product("Bed", 700000, "푹신한 침대", 20));
        furniture.addProduct(new Product("Sofa", 550000, "소파", 5));

        // CommerceSystem 객체 생성(카테고리 주입) 및 프로그램 실행(start 루프)
        List<Category> categories = Arrays.asList(electronics, clothes, foods, furniture);
        CommerceSystem system = new CommerceSystem(categories);
        system.start();
    }
}