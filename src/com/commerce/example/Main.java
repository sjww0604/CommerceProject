package com.commerce.example;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 속성
        List<Product> productsList = new ArrayList<>();

        // 생성자
        productsList.add(new Product("Gaxlay S25", 1200000, "최신 안드로이드 스마트폰", 0));
        productsList.add(new Product("iPhone 16", 1350000, "Apple의 최신 스마트폰", 0));
        productsList.add(new Product("MackBook Pro", 2400000, "M3 칩셋이 탑재된 노트북", 0));
        productsList.add(new Product("Airpods Pro", 350000, "노이즈 캔슬링 무선 이어폰", 0));


        // 기능
        System.out.println("[ 실시간 커머스 플랫폼 - 전자제품 ]");

        int index = 1; // 저장된 배열의 순서번호를 쓰기 위해 선언
        /* 향상된 for문으로 배열에 추가된 값을 처음부터 끝까지 반복해서 탐색하도록 설정 */
        for (Product product : productsList) {
            System.out.println(index++ + ". " + product.getPdName() + " | " + product.getPdPrice() + " | " + product.getPdDescription());
        }
    }
}
