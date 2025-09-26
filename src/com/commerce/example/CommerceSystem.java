package com.commerce.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/* 프로그램 비즈니스 로직 클래스 */
public class CommerceSystem {
    private final List<Category> categories = new ArrayList<>(); // 카테고리 필드 -> 카테고리 리스트 형태로 전환 및 통합
    private final Scanner sc = new Scanner(System.in); // 입력 담당

    /* 장바구니 필드 */
    private final List<CartItem> cart = new ArrayList<>(); // 병렬 배열 제거 : CartItem 기반으로 변경

    // 생성자
    public CommerceSystem(List<Category> initialCategories) {
        /* 카테고리 리스트에 등록 */
        this.categories.addAll(initialCategories);
    }
    // 현재 고객 등급
    private CustomerRank defaultCustomerRank;

    /* 초기화면 기능 및 검증 정적 화면에서 동적 화면으로 구성 변경 */
    public void start() {
        boolean mainStatus = true;
        while (mainStatus) {
            displayMainMenu(); // 프로그램 초기화면 구성
            int categoryChoice = sc.nextInt();
            int categorySize = categories.size();
            int cartMenu = -1;
            int cancelMenu = -1;
            int adminMenu;
            if (cart.isEmpty()) {
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
                order();
            } else if (cancelMenu != -1 && categoryChoice == cancelMenu) {
                System.out.println("주문을 취소했습니다.");
                cart.clear();
                System.out.println();
                continue;
            } else if (categoryChoice == adminMenu) {
                Customer.Account admin = Customer.ADMIN_ACCOUNT; // 마스터 계정 호출 및 정보를 admin에 담음
                sc.nextLine(); // 개행 제거
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
                    showAccountMenu();   // 성공일 때만 진입
                } else {
                    System.out.println("관리자 모드 진입이 거부되었습니다.");
                }
            } else {
                System.out.println("올바른 숫자를 입력하세요!");
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
    }

    // 장바구니 관련 메뉴 출력
    private void displayCartMenu() {
        if (!cart.isEmpty()) {
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
        int adminMenu = cart.isEmpty() ? (base + 1) : (base + 3); // 장바구니가 비었을 땐
            System.out.println(adminMenu + ". 관리자 모드");
    }

    //화면: 카테고리 내부(상품 목록 및 상품 상세)
    private void browseCategory(Category category) {
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
                String addCheck = String.format("%-10s %-15s", "1. 확인", "2. 취소");
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
        int foundIndex = -1; // 장바구니 리스트에 담긴 상품의 리스트 번호를 찾기 위해 설정
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).product().equals(product)) {
                foundIndex = i;
                break;
            }
        }

        // 2) 장바구니 수량/보유 재고 파악 (루프 밖에서 1회)
        int inCartStock = (foundIndex >= 0) ? cart.get(foundIndex).cartStock() : 0;
        int stock = product.getPdStock();

        // 3) 재고 검증: 장바구니 수량 + 1이 재고를 초과하면 거부
        if (inCartStock >= stock) {
            String alertStock = String.format(
                    "판매가능한 재고가 부족합니다. 현재 재고 %d개, 장바구니 수량 %d개",
                    stock, inCartStock
            );
            System.out.println(alertStock);
            return; // 중요: 추가하지 않고 종료
            }

        // 4) 추가/증량 처리 (루프 밖에서 1회)
        if (foundIndex >= 0) {
            CartItem item = cart.get(foundIndex);
            cart.set(foundIndex, new CartItem(item.product(), item.cartStock() + 1));
        } else {
            cart.add(new CartItem(product, 1));
        }

        // 5) 안내 출력
        System.out.println("=======================================");
        String addCartItem = String.format(" %s 가 장바구니에 추가되었습니다.", product.getPdName());
        System.out.println(addCartItem);
    }


    // 장바구니 출력 기능
    private void showCart() {
            System.out.println("아래와 같이 주문 하시겠습니까? ");
            for (int i = 0; i < cart.size(); i++) {
                CartItem item = cart.get(i);
                Product product = item.product();
                int cartStock = item.cartStock();
                String cartList = String.format("%-13s | %,10d원 | %s | 수량: %d개",
                        product.getPdName(),
                        product.getPdPrice(),
                        product.getPdDescription(),
                        cartStock
                );
                System.out.println(cartList);
            }

        int subtotal = getCartTotalPrice(); // 장바구니 총액을 우선 할인 계산하기 위한 subtotal에 저장
        System.out.println("[ 총 주문 금액 ]");
        System.out.printf("%,d원%n", subtotal);
    }

    // 총 금액 계산 전용 기능 (외부 선언 및 호출 가능하도록 수정)
    private int getCartTotalPrice() {
        int totalPrice = 0;
        for (int i = 0; i < cart.size(); i++) {
            CartItem item = cart.get(i);
            totalPrice += item.product().getPdPrice() * item.cartStock(); // 장바구니 상품 * 장바구니에 담긴 상품의 수량을 곱한 값을 총합으로 합침
        }
        return totalPrice;
    }

    // 등급 할인 적용 총액 계산
    private int getDiscountedTotal(int subtotal, CustomerRank rank) {
        return rank.apply(subtotal);
    }

    // 할인 금액 계산
    private int getDiscountAmount(int subtotal, CustomerRank rank) {
        return subtotal - getDiscountedTotal(subtotal, rank);
    }


    /* 주문 기능 내 등급별 할인 적용 기능 추가 */
    private void order() {
        int subtotal = getCartTotalPrice();
        System.out.println("고객 등급을 입력해주세요.");
        while (true) {
            /* 고객 등급별 할인율 정보를 나타내는 반복문 설정 */
        for (int i = 0; i < CustomerRank.values().length; i++) {
            CustomerRank rank = CustomerRank.values()[i];
            System.out.printf("%d. %-10s : %.0f %% 할인%n",
                    (i+1),
                    rank.name(),
                    rank.getDiscountRate()*100
            );
        }
            System.out.println();
            System.out.printf("%-10s %-15s %-10s%n", "1.등급 선택", "2. 메인으로 돌아가기", "3. 주문 확정");
            int orderChoice = sc.nextInt();
            sc.nextLine();

            /* 주문확정 전 메뉴 입력값에 따라 고객등급을 적용 및 할인금액 노출을 시키는 조건문 설정 */
            if (orderChoice == 1) {
                System.out.print("등급번호를 입력하세요 (1~4) : ");
                int rankChoice = sc.nextInt();
                sc.nextLine();
                /* 고객등급의 값을 각 입력값에 맞게 저장 */
                switch (rankChoice) {
                    case 1: defaultCustomerRank = CustomerRank.BRONZE; break;
                    case 2: defaultCustomerRank = CustomerRank.SILVER; break;
                    case 3: defaultCustomerRank = CustomerRank.GOLD; break;
                    case 4: defaultCustomerRank = CustomerRank.PLATINUM; break;
                    default:
                        System.out.println("올바른 숫자를 입력하세요!");
                        continue;
                }
                /* 기존에 선언했던 할인금액과 할인 이후 총 금액의 값을 받아와 정수형 변수에 저장
                * 등급 선택에 따른 변화되는 수치를 보여줄 수 있도록 출력화면 구성 */
                int discountAmt = getDiscountAmount(subtotal, defaultCustomerRank);
                int finalPay = getDiscountedTotal(subtotal, defaultCustomerRank);
                System.out.printf("\n할인 전 금액: %,d원%n", subtotal);
                System.out.printf("%s 등급 할인(%.0f%%): -%,d원%n",
                        defaultCustomerRank.name(), defaultCustomerRank.getDiscountRate() * 100, discountAmt);
                System.out.printf("최종 결제 금액: %,d원\n%n", finalPay);
            } else if (orderChoice == 2) {
                return; // 메인으로
            } else if (orderChoice == 3) {
                // 최종 결제 확정
                int discountAmt = getDiscountAmount(subtotal, defaultCustomerRank);
                int finalPay = getDiscountedTotal(subtotal, defaultCustomerRank);

                System.out.println("주문이 완료되었습니다!!");
                System.out.printf("할인 전 금액: %,d원%n", subtotal);
                System.out.printf("%s 등급 할인(%.0f%%): -%,d원%n", defaultCustomerRank.name(), defaultCustomerRank.getDiscountRate() * 100, discountAmt);
                System.out.printf("최종 결제 금액: %,d원%n", finalPay);

                // 재고 차감
                for (int i = 0; i < cart.size(); i++) {
                    CartItem item = cart.get(i);
                    Product product = item.product();
                    int cartStock = item.cartStock();

                    int beforeStock = product.getPdStock();
                    int afterStock = beforeStock - cartStock;
                    product.setPdStock(afterStock);

                    String stockStatus = String.format("%s 재고가 %d개 → %d개로 업데이트되었습니다.",
                            product.getPdName(), beforeStock, afterStock);
                    System.out.println(stockStatus);
                }
                System.out.println();

                cart.clear(); // 장바구니 비우기
                return;       // 주문 종료 후 메인으로

            } else {
                System.out.println("올바른 숫자를 입력하세요!");
            }
        }
    }

    /* Customer 기능 관련 추가 예정 */
    private void showAccountMenu() {
        System.out.println("[ 관리자 모드 ]");
        String[] adminMenu = {"상품 추가", "상품 수정", "상품 삭제", "전체 상품 현황", };
        int accountMenuChoice;

        for (int i = 0; i < adminMenu.length; i++) {
            String printAdminMenu = String.format("%d. %s", i + 1, adminMenu[i]);
            System.out.println(printAdminMenu);
            }
        System.out.println("0. 메인으로 돌아가기");
        accountMenuChoice = sc.nextInt();
        sc.nextLine();
        switch (accountMenuChoice) {
            case 1:
                addNewProduct();
                break;
            case 2:
                fixProduct();
                break;
            case 3:
                removeProduct();
                break;
            case 4:
                allProducts();
                break;
            case 0:
                return;
            default:
                System.out.println("올바른 숫자를 입력하세요!");
                break;
        }
    }
    /*관리자 모드 진입 시 기능 구현*/
    /* 상품 등록 입력 폼 */
    private Product productForm() {
        System.out.print("상품명: ");
        String name = sc.nextLine().trim();
        while (name.isEmpty()) {
            System.out.print("입력값은 공백일 수 없습니다. 다시 입력: ");
            name = sc.nextLine().trim();
        }

        System.out.print("가격(원): ");
        int price = sc.nextInt();
        sc.nextLine(); //정수값 입력 후 개행문자 버퍼에 남아있는 이슈 해소를 위해 추가
        while (price <= 0) {
            System.out.print("단가는 0원 이하일 수 없습니다. 다시 입력: ");
            price = sc.nextInt();
            sc.nextLine();
        }

        System.out.print("설명: ");
        String desc = sc.nextLine().trim();
        while (desc.isEmpty()) {
            System.out.print("상품 설명은 공백일 수 없습니다. 다시 입력: ");
            desc = sc.nextLine().trim();
        }

        System.out.print("재고: ");
        int stock = sc.nextInt();
        sc.nextLine();
        while (stock <= 0) {
            System.out.print("재고는 1개 이상 등록되어야 합니다. 다시 입력: ");
            stock = sc.nextInt();
        }
        return new Product (name, price, desc, stock);
    }
    /*상품 추가 기능*/
    private void addNewProduct() {
        System.out.println("어느 카테고리에 상품을 추가하시겠습니까?");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).getCategoryName());
        }
        System.out.println("0. 취소");

        int categoryChoice = sc.nextInt();
        sc.nextLine();
        if (categoryChoice == 0) return;

        int idx = categoryChoice - 1;
        if (idx < 0 || idx >= categories.size()) {
            System.out.println("올바른 숫자를 입력하세요!");
            return;
        }

        Product p = productForm();

        String addList = String.format("%-13s | %,10d원 | %s | 재고: %d개",
                p.getPdName(), p.getPdPrice(), p.getPdDescription(), p.getPdStock());
        System.out.println(addList);
        System.out.println("위 정보로 상품을 추가하시겠습니까?");
        System.out.println("1. 확인   2. 취소");

        int addConfirmChoice = sc.nextInt();
        if (addConfirmChoice == 1) {
            categories.get(idx).addProduct(p);
            System.out.println("상품이 성공적으로 추가되었습니다!");
        } else if (addConfirmChoice == 2) {
            System.out.println("취소했습니다.");
        } else {
            System.out.println("올바른 숫자를 입력하세요");
        }
    }
    /*상품 수정 기능*/
    private void fixProduct() {
        System.out.print("수정할 상품명을 입력해주세요: ");
        String targetName = sc.nextLine().trim();
        while (targetName.isEmpty()) {
            System.out.print("상품명이 비었습니다. 수정할 상품명을 다시 입력하세요: ");
            targetName = sc.nextLine().trim();
        }
        Category findCat = null;
        Product findProd = null;

        for (Category cat : categories) {
            for (int j = 0; j < cat.size(); j++) {
                Product prod = cat.getProduct(j);
                if (prod.getPdName().equals(targetName)) {
                    findCat = cat;
                    findProd = prod;
                    break; // 안쪽 루프만 종료
                }
            }
            if (findProd != null) { // 바깥 루프도 끊기
                break;
            }
        }
        if (findProd == null) {
            System.out.println("일치하는 상품명을 찾지 못했습니다.");
            return;
        }


        // 하위 수정 메뉴 : 이름 고정, 가격/설명/재고 수정기능 구현
        while (true) {
            System.out.println("수정할 항목을 선택해주세요: ");
            System.out.println("1. 가격");
            System.out.println("2. 설명");
            System.out.println("3. 재고수량");
            System.out.println("0. 이전 메뉴");

            int fixMenuChoice = sc.nextInt();
            sc.nextLine();
            if (fixMenuChoice < 0 || fixMenuChoice > 3) {
                System.out.println("올바른 숫자를 입력하세요!");
            }
            switch (fixMenuChoice) {
                case 1:
                    String currentPrice = String.format("현재 가격: %,d원",
                            findProd.getPdPrice());
                    System.out.println(currentPrice);
                    System.out.print("새로운 가격을 입력해주세요: ");
                    int newPrice = sc.nextInt();
                    sc.nextLine();
                    while (newPrice <= 0) {
                        System.out.println("0원 이하로는 수정할 수 없습니다. 다시 입력: ");
                        newPrice = sc.nextInt();
                        sc.nextLine();
                    }

                    String fixPriceMsg = String.format("%s의 가격이 %,d원 → %,d원으로 수정되었습니다.",
                            findProd.getPdName(),
                            findProd.getPdPrice(),
                            newPrice
                    );
                    findProd.setPdPrice(newPrice);
                    System.out.println(fixPriceMsg);
                    break;
                case 2:
                    String currentDescription = String.format("현재 설명 : %s",
                            findProd.getPdDescription()
                            );
                    System.out.println(currentDescription);
                    System.out.print("수정할 상품설명을 입력해주세요: ");
                    String newDescription = sc.nextLine().trim();
                    while (newDescription.isEmpty()) {
                        System.out.print("공백을 입력할 수 없습니다. 다시입력: ");
                        newDescription = sc.nextLine().trim();
                    }

                    String fixDescriptionMsg = String.format("%s의 설명이 '%s' → '%s'로 수정되었습니다.",
                            findProd.getPdName(),
                            findProd.getPdDescription(),
                            newDescription);
                    System.out.println(fixDescriptionMsg);
                    findProd.setPdDescription(newDescription);
                    break;

                case 3:
                    String currentStock = String.format("현재 재고 : %d",
                            findProd.getPdStock());
                    System.out.println(currentStock);
                    System.out.print("수정할 재고수량을 입력해주세요: ");
                    int newStock = sc.nextInt();
                    sc.nextLine();
                    while (newStock < 0) {
                        System.out.println("재고는 음수로 설정할 수 없습니다. 다시입력: ");
                        newStock = sc.nextInt();
                        sc.nextLine();
                    }
                    String fixStockMsg = String.format("%s의 재고수량이 %d개 → %d개로 수정되었습니다.",
                            findProd.getPdName(),
                            findProd.getPdStock(),
                            newStock
                    );
                    System.out.println(fixStockMsg);
                    findProd.setPdStock(newStock);
                    break;
                case 0:
                    System.out.println("기존 메뉴로 돌아갑니다.");
                    return;
                default:
                    System.out.println("올바른 숫자를 입력하세요!");
                    return;
            }
        }
    }

    /*상품 삭제 기능*/
    private void removeProduct() {
        System.out.print("삭제할 상품명을 입력해주세요: ");
        String targetName = sc.nextLine().trim();
        while (targetName.isEmpty()) {
            System.out.print("상품명이 비었습니다. 삭제할 상품명을 다시 입력하세요: ");
            targetName = sc.nextLine().trim();
        }
        Category findremoveCat = null;
        Product findProd = null;
        int foundIndex = -1; // 삭제할 상품의 리스트 위치를 찾을 인덱스 값 설정

        for (Category cat : categories) {
            for (int j = 0; j < cat.size(); j++) {
                Product prod = cat.getProduct(j);
                if (prod.getPdName().equals(targetName)) {
                    findremoveCat = cat;
                    findProd = prod;
                    foundIndex = j;
                    break; // 안쪽 루프만 종료
                }
            }
            if (findProd != null) { // 바깥 루프도 끊기
                break;
            }
        }
        if (findProd == null) {
            System.out.println("일치하는 상품명을 찾지 못했습니다.");
            return;
        }
        // 하위 삭제 메뉴
        String beforeProduct = String.format("%-13s | %,10d원 | %s | 재고: %d개",
                findProd.getPdName(), findProd.getPdPrice(),
                findProd.getPdDescription(), findProd.getPdStock()
        );
        System.out.println("[ 선택된 상품 ]");
        System.out.println(beforeProduct);

        System.out.println("1. 삭제(확정)");
        System.out.println("2. 메뉴로 돌아가기");
        int delChoice = sc.nextInt();
        sc.nextLine();

        switch (delChoice) {
            case 1:
                if (foundIndex < 0) {
                    System.out.println("삭제할 상품을 찾지 못했습니다.");
                    return;
                }
                findremoveCat.removeProduct(foundIndex);

                for (int i = cart.size() -1; i>=0; i--){
                    if (cart.get(i).product().equals(findProd)) {
                        cart.remove(i);
                    }
                }
                String removeMsg = String.format("%s 상품이 삭제 되었습니다.",
                        findProd.getPdName());
                System.out.println(removeMsg);
                return;
            case 2:
                System.out.println("메뉴로 돌아갑니다.");
                return;
            default:
                System.out.println("올바른 숫자를 입력하세요!");
        }
    }

    /*전체상품 출력기능*/
    private void allProducts() {
        System.out.println("[ 전체 상품 현황 ]");
        for (Category cat : categories) { // 바깥 반복문 : 카테고리
            System.out.println();
            System.out.println("[ " + cat.getCategoryName() + " ]");
            for (int j = 0; j < cat.size(); j++) { // 안쪽 반복문 : 상품명
                Product prod = cat.getProduct(j);
                String formattedProd = String.format("%-13s | %,10d원 | %s | 재고: %d개",
                        prod.getPdName(), prod.getPdPrice(),
                        prod.getPdDescription(), prod.getPdStock()
                );
                System.out.println(formattedProd);
            }
        }
    }


    /* CartItem 클래스 생성 (병렬배열 제거 및 단일배열로 수정하기 위함)
    * record 선언을 통해 getProduct -> item.Product 형식의 리팩토링 진행*/
        record CartItem(Product product, int cartStock) {
    }
}

