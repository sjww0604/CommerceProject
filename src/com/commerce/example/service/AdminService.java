package com.commerce.example.service;

import com.commerce.example.domain.Product;

import java.util.Scanner;

/* 관리자 모드 입력 흐름 제어
* 로직은 ProductService에서 처리하도록 진행
* 장바구니 처리는 CartService에서 진행 */
public class AdminService {
    private final Scanner sc;
    private final ProductService productService;
    private final CartService cartService;

    public AdminService(Scanner sc, ProductService productService, CartService cartService) {
        this.sc = sc;
        this.productService = productService;
        this.cartService = cartService;
    }

    public void showMenu() {
        while (true) {
            System.out.println("[ 관리자 모드 ]");
            System.out.println("1. 상품 추가");
            System.out.println("2. 상품 수정");
            System.out.println("3. 상품 삭제");
            System.out.println("4. 전체 상품 현황");
            System.out.println("0. 메인으로 돌아가기");

            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
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
                    productService.printAll();
                    break;
                case 0:
                    System.out.println("메인 메뉴로 돌아갑니다.");
                default:
                    System.out.println("올바른 숫자를 입력하세요!");
                }
            }
        }


    private void addNewProduct() {
        System.out.println("어느 카테고리에 상품을 추가하시겠습니까?");
        for (int i = 0; i < productService.getCategories().size(); i++) {
            System.out.println((i + 1) + ". " + productService.getCategories().get(i).getCategoryName());
        }
        System.out.println("0. 취소");

        int categoryChoice = sc.nextInt();
        sc.nextLine(); // 개행 제거
        while (categoryChoice < 0 || categoryChoice > productService.getCategories().size()) {
            System.out.println("숫자를 다시 입력하세요.");
            categoryChoice = sc.nextInt();
            sc.nextLine();
        }
        if (categoryChoice == 0) return;

        int idx = categoryChoice - 1;
        Product p = productForm();

        System.out.printf("%-13s | %,10d원 | %s | 재고: %d개%n",
                p.getPdName(), p.getPdPrice(), p.getPdDescription(), p.getPdStock());
        System.out.println("위 정보로 상품을 추가하시겠습니까?\n1. 확인   2. 취소");

        int addConfirmChoice = sc.nextInt();
        sc.nextLine();
        while (addConfirmChoice != 1 && addConfirmChoice != 2) {
            System.out.println("1 또는 2를 입력하세요.");
            addConfirmChoice = sc.nextInt();
            sc.nextLine();
        }

        if (addConfirmChoice == 1) {
            productService.addProduct(idx, p);
            System.out.println("상품이 성공적으로 추가되었습니다!");
        } else {
            System.out.println("취소했습니다.");
        }
    }

    private void fixProduct() {
        System.out.print("수정할 상품명을 입력해주세요: ");
        String targetName = sc.nextLine().trim();
        while (targetName.isEmpty()) {
            System.out.print("상품명이 비었습니다. 수정할 상품명을 다시 입력하세요: ");
            targetName = sc.nextLine().trim();
        }

        ProductService.ProductRef ref;
        try {
            ref = productService.findByProductName(targetName);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("일치하는 상품명을 찾지 못했습니다.");
            return;
        }
        Product findProd = ref.product();

        while (true) {
            System.out.println("수정할 항목을 선택해주세요: ");
            System.out.println("1. 가격");
            System.out.println("2. 설명");
            System.out.println("3. 재고수량");
            System.out.println("0. 이전 메뉴");

            int fixMenuChoice = sc.nextInt();
            sc.nextLine();

            switch (fixMenuChoice) {
                case 0:
                    return;
                case 1:
                    System.out.printf("현재 가격: %,d원%n", findProd.getPdPrice());
                    System.out.print("새로운 가격을 입력해주세요: ");
                    int newPrice = sc.nextInt();
                    sc.nextLine();
                    while (newPrice <= 0) {
                        System.out.print("0원 이하로는 수정할 수 없습니다. 다시 입력: ");
                        newPrice = sc.nextInt();
                        sc.nextLine();
                    }
                    productService.updatePrice(findProd.getPdName(), newPrice);
                    System.out.printf("%s의 가격이 %,d원 → %,d원으로 수정되었습니다.%n",
                            findProd.getPdName(), findProd.getPdPrice(), newPrice);
                    break;
                case 2:
                    System.out.printf("현재 설명 : %s%n", findProd.getPdDescription());
                    System.out.print("수정할 상품설명을 입력해주세요: ");
                    String newDescription = sc.nextLine().trim();
                    while (newDescription.isEmpty()) {
                        System.out.print("공백을 입력할 수 없습니다. 다시입력: ");
                        newDescription = sc.nextLine().trim();
                    }
                    productService.updateDescription(findProd.getPdName(), newDescription);
                    System.out.printf("%s의 설명이 '%s' → '%s'로 수정되었습니다.%n",
                            findProd.getPdName(), findProd.getPdDescription(), newDescription);
                    break;
                case 3:
                    System.out.printf("현재 재고 : %d%n", findProd.getPdStock());
                    System.out.print("수정할 재고수량을 입력해주세요: ");
                    int newStock = sc.nextInt();
                    sc.nextLine();
                    while (newStock < 0) {
                        System.out.print("재고는 음수로 설정할 수 없습니다. 다시입력: ");
                        newStock = sc.nextInt();
                        sc.nextLine();
                    }
                    productService.updateStock(findProd.getPdName(), newStock);
                    System.out.printf("%s의 재고수량이 %d개 → %d개로 수정되었습니다.%n",
                            findProd.getPdName(), findProd.getPdStock(), newStock);
                    break;
                default:
                    System.out.println("올바른 숫자를 입력하세요!");
            }
        }
    }

    private void removeProduct() {
        System.out.print("삭제할 상품명을 입력해주세요: ");
        String targetName = sc.nextLine().trim();
        while (targetName.isEmpty()) {
            System.out.print("상품명이 비었습니다. 삭제할 상품명을 다시 입력하세요: ");
            targetName = sc.nextLine().trim();
        }

        ProductService.ProductRef ref;
        try {
            ref = productService.findByProductName(targetName);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("일치하는 상품명을 찾지 못했습니다.");
            return;
        }
        Product findProd = ref.product();

        System.out.println("[ 선택된 상품 ]");
        System.out.printf("%-13s | %,10d원 | %s | 재고: %d개%n",
                findProd.getPdName(), findProd.getPdPrice(),
                findProd.getPdDescription(), findProd.getPdStock());

        System.out.println("1. 삭제(확정)");
        System.out.println("2. 메뉴로 돌아가기");
        int delChoice = sc.nextInt();
        sc.nextLine();
        while (delChoice != 1 && delChoice != 2) {
            System.out.println("1 또는 2를 입력하세요.");
            delChoice = sc.nextInt();
            sc.nextLine();
        }

        if (delChoice == 1) {
            productService.removeByName(findProd.getPdName());
            // 장바구니에서도 제거
            for (int i = cartService.getCartItems().size() - 1; i >= 0; i--) {
                if (cartService.getCartItems().get(i).product().equals(findProd)) {
                    cartService.getCartItems().remove(i);
                }
            }
            System.out.printf("%s 상품이 삭제 되었습니다.%n", findProd.getPdName());
        } else {
            System.out.println("메뉴로 돌아갑니다.");
        }
    }

    private Product productForm() {
        String name;
        while (true) {
            System.out.print("상품명: ");
            name = sc.nextLine().trim();
            if (!name.isEmpty()) break;
            System.out.println("공백을 입력할 수 없습니다.");
        }

        System.out.print("가격(원): ");
        int price = sc.nextInt();
        sc.nextLine();
        while (price <= 0) {
            System.out.print("0보다 큰 값을 입력하세요. 가격(원): ");
            price = sc.nextInt();
            sc.nextLine();
        }

        String desc;
        while (true) {
            System.out.print("설명: ");
            desc = sc.nextLine().trim();
            if (!desc.isEmpty()) break;
            System.out.println("공백을 입력할 수 없습니다.");
        }

        System.out.print("재고: ");
        int stock = sc.nextInt();
        sc.nextLine();
        while (stock <= 0) {
            System.out.print("0보다 큰 값을 입력하세요. 재고: ");
            stock = sc.nextInt();
            sc.nextLine();
        }
        return new Product(name, price, desc, stock);
    }
}
