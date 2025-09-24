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

    /* 장바구니 필드 */
    private final List<Product> cart = new ArrayList<>(); // 단일 배열로 수정

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
                case 5:
                    showCart();
                    order();
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
        displayCartMenu();
        System.out.println("0. 종료 | 프로그램 종료");
    }

    // 장바구니 관련 메뉴 출력
    private void displayCartMenu() {
        if (!cart.isEmpty()) {
            System.out.println();
            System.out.println("[ 주문 관리 ]");
            System.out.println("5. 장바구니 확인 | 장바구니를 확인 후 주문합니다.");
            System.out.println("6. 주문 취소  | 진행중인 주문을 취소합니다. ");
        }
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
                System.out.println("위 상품을 장바구니에 추가하시겠습니까?");
                String addCheck = String.format("%-10s %-15s","1. 확인","2. 취소");
                System.out.println(addCheck);
                int actionChoice = sc.nextInt();
                switch (actionChoice) {
                    case 1:
                        addCart(product);
                        break;
                    case 2:
                        continue; // 카테고리 화면으로 돌아가기
                    default:
                        System.out.println("올바른 숫자를 입력하세요!");
                        continue; // 잘못 입력 → 다시 카테고리 루프 반복
                }
            } else {
                System.out.println("올바른 숫자를 입력하세요! ");
            }
        }
    }

    // 장바구니 추가 기능
    private void addCart(Product product) {
        int index = cart.indexOf(product);
        int currentStock; // 장바구니에 담는 수량이 보유재고수량보다 많을 때 경고문을 나타낼 수 있도록 검증기능 추가
        if (index>= 0) {
            currentStock = cartCounts.get(index);
        } else {
            currentStock = 0;
        }
        int stock = product.getPdStock();

        if (currentStock >= stock) {
            String alertStock = String.format("판매가능한 재고가 부족합니다. 현재 재고 %d개, 장바구니 수량 %d개",
                    stock,
                    currentStock
            );
            System.out.println(alertStock);
            return;
        }

        if (index >= 0) {
            //이미 담긴 상품이면 수량 증가
            cartCounts.set(index, cartCounts.get(index) + 1);
        } else {
            // 새 상품이면 리스트에 추가
            cart.add(product);
            cartCounts.add(1);
        }
        System.out.println("=======================================");
        String addCartItem = String.format(" %s 가 장바구니에 추가되었습니다.", product.getPdName());
        System.out.println(addCartItem);
    }

    // 장바구니 출력 기능
    private void showCart() {
        System.out.println("아래와 같이 주문 하시겠습니까? ");
        for (int i =0; i < cart.size(); i++) {
            Product product = cart.get(i);
            int cartStock = cartCounts.get(i); // 장바구니에 담긴 수량
            String cartList = String.format("%-13s | %,10d원 | %s | 수량: %d개",
                    product.getPdName(),
                    product.getPdPrice(),
                    product.getPdDescription(),
                    cartStock
            );
            System.out.println(cartList);
        }
            String total = String.format("%,d원",getCartTotalPrice()); // 장바구니에 담긴 상품의 총 가격을 출력
            System.out.println();
            System.out.println("[ 총 주문 금액 ]");
            System.out.println(total);
            System.out.println();
        }
    // 총 금액 계산 전용 기능 (외부 선언 및 호출 가능하도록 수정)
    private int getCartTotalPrice() {
        int totalPrice = 0;
        for (int i=0; i< cart.size(); i++) {
            Product product = cart.get(i);
            int cartStock = cartCounts.get(i);
            totalPrice += product.getPdPrice() * cartStock; // 장바구니 상품 * 장바구니에 담긴 상품의 수량을 곱한 값을 총합으로 합침
        }
        return totalPrice;
    }

    // 주문 기능
    private void order() {
        String orderCheck = String.format("%-10s %-15s","1. 주문 확정","2. 메인으로 돌아가기");
        System.out.println(orderCheck);
        int orderChoice = sc.nextInt();
        if (orderChoice == 1) {
            String orderTotal = String.format("%,d원",getCartTotalPrice());
            System.out.println("주문이 완료되었습니다! 총 금액 : " + orderTotal);

            // 장바구니의 주문수량만큼 재고 차감
            for (int i = 0; i < cart.size(); i++) {
                Product product = cart.get(i);
                int cartStock = cartCounts.get(i);

                int beforeStock = product.getPdStock();
                int afterStock = beforeStock - cartStock;
                product.setPdStock(afterStock);

                String stockStatus = String.format("%s 재고가 %d개 → %d개로 업데이트 되었습니다.",
                        product.getPdName(),
                        beforeStock,
                        afterStock
                );
                System.out.println(stockStatus);
            }
            System.out.println();

            // 주문 처리 이후 장바구니 비우기
            cart.clear();
            cartCounts.clear();

        } else if (orderChoice == 2) {
            start(); //
        } else {
            System.out.println("올바른 숫자를 입력하세요!");
            return;
        }
    }

    // CartItem 클래스 생성 (병렬배열 제거 및 단일배열로 수정하기 위함)
    public class CartItem {
        private  Product product;
        private int cartStock;
        public CartItem(Product product, int cartStock) {
            this.product = product;
            this.cartStock = cartStock;
        }
        public Product getProduct() {
            return product;
        }
        public int getCartStock() {
            return cartStock;
        }
    }
}

