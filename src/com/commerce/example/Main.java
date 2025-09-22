package com.commerce.example;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 속성
        List<Product> productsList = new ArrayList<>();

        // 생성자
        productsList.add(new Product("Galaxy S25", 1200000, "최신 안드로이드 스마트폰", 0));
        productsList.add(new Product("iPhone 16", 1350000, "Apple의 최신 스마트폰", 0));
        productsList.add(new Product("MackBook Pro", 2400000, "M3 칩셋이 탑재된 노트북", 0));
        productsList.add(new Product("Airpods Pro", 350000, "노이즈 캔슬링 무선 이어폰", 0));

        System.out.println("[ 실시간 커머스 플랫폼 - 전자제품 ]");
        CommerceSystem system = new CommerceSystem(productsList);
        system.start();

        }
    }