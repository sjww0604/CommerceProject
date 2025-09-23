package com.commerce.example;

import java.util.ArrayList;
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

    /* 장바구니 필드 추가 */
    private final List<Product> cart = new ArrayList<>();

    // 생성자
    public CommerceSystem(Category electronics, Category clothes, Category foods, Category furniture) {
        this.electronics = electronics;
        this.clothes = clothes;
        this.foods = foods;
        this.furniture = furniture;
    }

    // 초기화면 기능 및 검증
    public void start() {
        boolean mainStatus = true;
        while (mainStatus) {
            displayMainMenu(); // 프로그램 초기화면 구성
            int categoryChoice = sc.nextInt();

            switch (categoryChoice) {
                case 0:
                    mainStatus = false;
                    System.out.println("커머스 플랫폼을 종료합니다.");
                    break;
                case 1:
                    browseCategory(electronics);
                    break;
                case 2:
                    browseCategory(clothes);
                    break;
                case 3:
                    browseCategory(foods);
                    break;
                case 4:
                    browseCategory(furniture);
                    break;
                default:
                    System.out.println("올바른 숫자를 입력하세요!");
                    continue; // 메인 메뉴로 복귀
                    //화면: 카테고리 내부(상품 목록 및 상품 상세)
            }
        }
    }

    // 메인 메뉴 화면 출력
    private void displayMainMenu() {
        System.out.println("[ 실시간 커머스 플랫폼 메인 ]");
        System.out.println("1. " + electronics.getCategoryName());
        System.out.println("2. " + clothes.getCategoryName());
        System.out.println("3. " + foods.getCategoryName());
        System.out.println("4. " + furniture.getCategoryName());
        System.out.println("0. 종료 | 프로그램 종료");

    }

    //화면: 카테고리 내부(상품 목록 및 상품 상세)
    private void browseCategory (Category category){
        boolean subStatus = true;
        while (subStatus) {
            System.out.println();
            String catTitle = String.format("[ %s 카테고리 ]", category.getCategoryName());
            System.out.println(catTitle);
            category.printProducts(); // 전체 리스트 출력
            System.out.println("0. 뒤로가기 ");

            int productChoice = sc.nextInt();
            if (productChoice == 0) {
                subStatus = false; // 뒤로가기
            } else if (productChoice >= 1 && productChoice <= category.size()) {
                Product product = category.getProduct(productChoice - 1);
                String productResultList = String.format(
                        "%d. %-15s | %,10d원 | %s | %d개",
                        productChoice,
                        product.getPdName(),
                        product.getPdPrice(),
                        product.getPdDescription(),
                        product.getPdStock()
                );
                System.out.println(productResultList);
                System.out.println("========================================================");
            } else {
                System.out.println("올바른 숫자를 입력하세요! ");
            }
        }
    }

    // 장바구니 추가 기능
    private void addCart(Product product) {
        cart.add(product);
        String addCartItem = String.format(" %s 가 장바구니에 추가되었습니다.", product.getPdName());
        System.out.println(addCartItem);
    }
}
