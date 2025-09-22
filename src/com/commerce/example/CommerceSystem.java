package com.commerce.example;

import java.util.List;
import java.util.Scanner;

/* 프로그램 비즈니스 로직 클래스
* 커머스 플랫폼의 상품을 관리하고 사용자 입력을 처리하는 클래스
* Product를 관리하는 리스트가 필드로 존재
* Main.java의 입력과 반복문 기능을 여기로 이관 */
public class CommerceSystem {
    // 속성
    private List<Product> productList;
    Scanner sc = new Scanner(System.in); // 입력 처리는 CommerceSystem.java에서 진행될 수 있도록 함

    // 생성자
    public CommerceSystem(List<Product> productList) {
        this.productList = productList;
    }

    // 기능
    public void start() { // Main.java 에서 입력과 반복문 처리하는 기능을 이관
        boolean mainStatus = true;
        while (mainStatus) {
            printProducts(productList); // 배열 내 저장된 전체 리스트 출력하는 출력 메서드 호출
            System.out.println("0. 종료 | 프로그램 종료");
            int choice = sc.nextInt();

            if (choice == 0) {
                mainStatus = false;
                System.out.println("커머스 플랫폼을 종료합니다.");
            } else if (choice >= 1 && choice < productList.size() + 1) { //배열의 크기는 입력된 개수의 -1개이므로 저장된 배열값 끝까지 조회하기 위해 +1로 조건문 설정
                Product product = productList.get(choice - 1); // 배열은 0번째부터 저장
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

    private static void printProducts(List<Product> productList) {
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            String formatted = String.format( // 출력 양식은 맞추되 저장되어있는 배열 전체 화면 출력을 위한 용도
                    "%d. %-15s | %,10d원 | %s",
                    i + 1,
                    product.getPdName(),
                    product.getPdPrice(),
                    product.getPdDescription()
            );
            System.out.println(formatted);
        }
    }
}
