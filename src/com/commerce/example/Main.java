package com.commerce.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 속성
        Scanner sc = new Scanner(System.in);
        List<Product> productsList = new ArrayList<>();

        // 생성자
        productsList.add(new Product("Galaxy S25", 1200000, "최신 안드로이드 스마트폰", 0));
        productsList.add(new Product("iPhone 16", 1350000, "Apple의 최신 스마트폰", 0));
        productsList.add(new Product("MackBook Pro", 2400000, "M3 칩셋이 탑재된 노트북", 0));
        productsList.add(new Product("Airpods Pro", 350000, "노이즈 캔슬링 무선 이어폰", 0));


        // 기능
        System.out.println("[ 실시간 커머스 플랫폼 - 전자제품 ]");

        /* 향상된 for문으로 배열에 추가된 값을 처음부터 끝까지 반복해서 탐색하도록 설정 */
        boolean mainStatus=true;
        while (mainStatus) {
            int index = 1; // 저장된 배열의 순서번호를 쓰기 위해 선언
            for (Product product : productsList) {
                /* String.format을 활용한 자릿수 및 단위 표현 기능 추가
                * %-15s : 고정폭 15만큼 설정, 왼쪽 정렬로 나타냄
                * %,10d : 고정폭 10만큼 설정, 자릿수 , 표현 및 원 단위 붙이기 (Decimal 십진수의 글자를 딴 표현형태)
                * %s : 문자형(String)으로 표현*/
                String productResultList = String.format("%d. %-15s | %,10d원 | %s", index++, product.getPdName(), product.getPdPrice(), product.getPdDescription());
                System.out.println(productResultList);
            }
            System.out.println("0. 종료 | 프로그램 종료");
            int choice = sc.nextInt(); // 메뉴 선택한 값을 저장하는 변수 선언
            if (choice == 0) { // 받은 값이 0인 경우 반복문 실행을 종료시키는 연결과정으로 설정
                mainStatus=false; // 종료인 경우 상태값을 저장하는 mainStatus를 false로 설정 while문을 탈출
            }
        }
        System.out.println("커머스 플랫폼을 종료합니다.");

        // ++ 자릿수 표현 기능 검색해보고 사용해보기
    }
}
