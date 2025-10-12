package com.commerce.example.service;

import com.commerce.example.domain.Category;
import com.commerce.example.domain.Customer;
import com.commerce.example.domain.Product;
import com.commerce.example.view.Format;
import com.commerce.example.view.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

/* 프로그램 비즈니스 로직 클래스 */
public class CommerceSystem {
    private final List<Category> categories = new ArrayList<>(); // 카테고리 필드 -> 카테고리 리스트 형태로 전환 및 통합
    private final CartService cartService = new CartService(); // 장바구니 기능 위임
    private final ProductService productService = new ProductService(categories);  // 조회,검증,수정,삭제 로직 위임
    private final Scanner sc = new Scanner(System.in); // 입력 담당
    private final AdminService adminService = new AdminService(sc, productService, cartService); // 관리자모드 입력 흐름 제어
    private final OrderService orderService = new OrderService(sc, cartService); //주문처리 전담 서비스

    // 생성자
    public CommerceSystem(List<Category> initialCategories) {
        /* 카테고리 리스트에 등록 */
        this.categories.addAll(initialCategories);
    }

    /* 초기화면 기능 및 검증 정적 화면에서 동적 화면으로 구성 변경 */
    public void start() {
        boolean mainStatus = true;
        while (mainStatus) {
            displayMainMenu(); // 프로그램 초기화면 구성

            int categoryChoice = sc.nextInt();
            sc.nextLine();

            int categorySize = categories.size();
            int cartMenu = -1;
            int cancelMenu = -1;
            int adminMenu;

            if (cartService.getCartItems().isEmpty()) {
                adminMenu = categorySize + 1; // 장바구니가 비었을 때 카테고리 다음 순번이 관리자 모드가 나오도록 번호 저장
            } else {
                /* 장바구니가 있을 때 구현되어야 할 순번에 맞춰 값을 저장 */
                cartMenu = categorySize + 1;  // 장바구니 확인
                cancelMenu = categorySize + 2; // 주문 취소
                adminMenu = categorySize + 3; // 관리자 모드
            }

            if (categoryChoice == 0) {
                mainStatus = false;
                System.out.println("커머스 플랫폼을 종료합니다.");
                continue;
            }
            if (categoryChoice >= 1 && categoryChoice <= categorySize) {
                Category selectedCategory = categories.get(categoryChoice - 1);
                browseCategory(selectedCategory);

            } else if (cartMenu != -1 && categoryChoice == cartMenu) {
                showCart();
                orderService.showOrderMenu();

            } else if (cancelMenu != -1 && categoryChoice == cancelMenu) {
                System.out.println("주문을 취소했습니다.");
                cartService.clearCart();
                System.out.println();

            } else if (categoryChoice == adminMenu) {
                Customer.Account admin = Customer.ADMIN_ACCOUNT; // 마스터 계정 호출 및 정보를 admin에 담음
                boolean authed = false; // 인증 성공,실패의 상태값을 담기 위함
                for (int tries = 0; tries < 3; tries++) {
                    System.out.print("관리자 비밀번호를 입력하세요: ");
                    String password = sc.nextLine().trim();   // 매 시도마다 새로 입력

                    if (admin.getUserPassword().equals(password)) {
                        authed = true;
                        break;
                    } else {
                        System.out.println("비밀번호가 일치하지 않습니다! (남은 시도: " + (2 - tries) + ")");
                    }
                }

                if (authed) {
                    adminService.showMenu();   // 성공일 때만 진입
                } else {
                    System.out.println("관리자 모드 진입이 거부되었습니다.");
                }
            } else {
                System.out.println(Message.INVALID_NUMBER);
            }
        }
    }


    // 메인 메뉴 화면 출력 categories 리스트 내 정보를 모두 나타냄
    private void displayMainMenu() {
        System.out.println("[ 실시간 커머스 플랫폼 메인 ]");
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            System.out.println((i + 1) +". " + category.getCategoryName());
        }
        displayCartMenu();
        displayAdminMenu();
        System.out.println("0. 종료 | 프로그램 종료");
        System.out.print(Message.PROMPT_SELECT);
    }

    // 장바구니 관련 메뉴 출력
    private void displayCartMenu() {
        if (!cartService.getCartItems().isEmpty()) {
            int cartMenu = categories.size() + 1;
            int cancelMenu = categories.size() + 2;

            System.out.println();
            System.out.println("[ 주문 관리 ]");
            System.out.println(cartMenu + ". 장바구니 확인 | 장바구니를 확인 후 주문합니다.");
            System.out.println(cancelMenu + ". 주문 취소  | 진행중인 주문을 취소합니다. ");
        }
    }

    // 관리자 모드 메뉴 출력
    private void displayAdminMenu() {
        int base = categories.size();
        int adminMenu = cartService.getCartItems().isEmpty() ? (base + 1) : (base + 3); // 장바구니가 비었을 땐
            System.out.println(adminMenu + ". 관리자 모드");
    }

    //화면: 카테고리 내부(상품 목록 및 상품 상세)
    private void browseCategory(Category category) {
        boolean subStatus = true;
        while (subStatus) {
            System.out.println();
            System.out.printf("[ %s 카테고리 ]%n", category.getCategoryName());
            System.out.println("1. 전체 상품 보기");
            System.out.println("2. 가격대별 필터링 (100만원 이하)");
            System.out.println("3. 가격대별 필터링 (100만원 초과)");
            System.out.println("0. 뒤로가기 ");
            System.out.print(Message.PROMPT_SELECT);

            int submenu = sc.nextInt();
            sc.nextLine();
            if (submenu == 0) {
                subStatus = false; // 뒤로가기
                continue;
            }

            // 카테고리 내 상품 목록 (임시 기준 리스트)
            List<Product> base = IntStream.range(0, category.size())
                    .mapToObj(category::getProduct)
                    .toList();

            // 선택에 따라 임시 디스플레이 리스트 구성
            List<Product> display = switch (submenu) {
                case 1 -> base; // 전체 리스트
                case 2 -> base.stream()
                        .filter(p -> p.getPdPrice() <= 1_000_000)
                        .toList();
                case 3 -> base.stream()
                        .filter(p -> p.getPdPrice() > 1_000_000)
                        .toList();
                default -> {
                    System.out.println(Message.INVALID_NUMBER);
                    yield List.of();
                }
            };

            if (display.isEmpty()) {
                System.out.println("조건에 맞는 상품이 없습니다.");
                continue;
            }

            //필터링 결과 출력
            System.out.println();
            String title = switch (submenu) {
                case 1 -> "[ 전체 상품 목록 ]";
                case 2 -> "[ 100만원 이하 상품 목록 ]";
                case 3 -> "[ 100만원 초과 상품 목록 ]";
                default -> "[ 상품 목록 ]";
            };
            System.out.println(title);
            for (int i = 0; i < display.size(); i++) {
                Product p = display.get(i);
                System.out.println((i+1) + ". " + Format.productLine(p));
            }
            System.out.println("0. 뒤로가기");
            System.out.print(Message.PROMPT_SELECT);

            // 개별 상품 선택 -> 장바구니 추가 확인
            int productChoice = sc.nextInt();
            sc.nextLine();
            if (productChoice == 0) {
                continue;
            }
            if (productChoice < 1 || productChoice > display.size()) {
                System.out.println(Message.INVALID_NUMBER);
                continue;
            }

            Product selected = display.get(productChoice - 1);
            String productResultList = String.format(
                    "%s | %,d원 | %s | 재고: %d개",
                    selected.getPdName(), selected.getPdPrice(), selected.getPdDescription(), selected.getPdStock()
            );
            System.out.println("선택한 상품: " + productResultList);
            System.out.println("위 상품을 장바구니에 추가하시겠습니까?");
            System.out.printf("%-10s %-10s%n", "1.확인", "2. 취소");
            System.out.print(Message.PROMPT_SELECT);

            int actionChoice = sc.nextInt();
            sc.nextLine();
            switch (actionChoice) {
                case 1 -> cartService.addCart(selected);
                case 2 -> {}
                default -> System.out.println(Message.INVALID_NUMBER);
            }
        }
    }
    // 장바구니 출력 기능
    private void showCart() {
        cartService.showCart();
    }

}

