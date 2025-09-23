package com.commerce.example;

import java.util.List;
import java.util.Scanner;

/* 프로그램 비즈니스 로직 클래스 */
public class CommerceSystem {
    /* 속성 4개의 카테고리 속성 추가 */
    private final Category electronics;
    private final Category clothes;
    private final Category foods;
    private final Category furniture;
    private final Scanner sc = new Scanner(System.in); // 입력 담당

    // 생성자
    public CommerceSystem(Category electronics, Category clothes, Category foods, Category furniture) {
        this.electronics = electronics;
        this.clothes = clothes;
        this.foods = foods;
        this.furniture = furniture;
    }

    // 기능
    public void start() { // Main.java 에서 입력과 반복문 처리하는 기능을 이관
        boolean mainStatus = true;
        while (mainStatus) {
            category.printProducts(); // 배열 내 저장된 전체 리스트 출력하는 출력 메서드 호출
            System.out.println("0. 종료 | 프로그램 종료");
            int choice = sc.nextInt();

            if (choice == 0) {
                mainStatus = false;
                System.out.println("커머스 플랫폼을 종료합니다.");
            } else if (choice >= 1 && choice < category.size() + 1) { //배열의 크기는 입력된 개수의 -1개이므로 저장된 배열값 끝까지 조회하기 위해 +1로 조건문 설정
                Product product = category.getProduct(choice - 1); // Category에서 Product를 관리하므로 getter 선언
                /* String.format을 활용한 자릿수 및 단위 표현 기능 추가
                 * %-15s : 고정폭 15만큼 설정, 왼쪽 정렬로 나타냄
                 * %,10d : 고정폭 10만큼 설정, 자릿수 , 표현 및 원 단위 붙이기 (Decimal 십진수의 글자를 딴 표현형태)
                 * %s : 문자형(String)으로 표현*/
                String productResultList = String.format("%d. %-15s | %,10d원 | %s", choice, product.getPdName(), product.getPdPrice(), product.getPdDescription());
                // 출력 양식을 준수하되 입력값에 따라 저장되어있는 배열 한줄만 나타내기 위해 사용
                System.out.println(productResultList);
                System.out.println("========================================================"); // 1~4번 한줄씩 출력될 때 구분되지 않아 구분선 추가
                System.out.println();

            } else {
                System.out.println("올바른 숫자를 입력하세요! ");
                return; // 배열에 저장된 값 번호를 올바르게 입력하지 않으면 프로그램 종료
            }
        }
    }
}
