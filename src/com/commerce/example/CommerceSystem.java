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

    /* 초기화면 기능 및 검증 정적 화면에서 동적 화면으로 구성 변경 */
    public void start() {
        boolean mainStatus = true;
        while (mainStatus) {
            displayMainMenu(); // 프로그램 초기화면 구성
            int categoryChoice = sc.nextInt();

            if (categoryChoice == 0) {
                mainStatus = false;
                System.out.println("커머스 플랫폼을 종료합니다.");
                continue;
            }
            int categorySize = categories.size(); // 카테고리 개수
            int cartMenu = categorySize + 1; // 장바구니 확인
            int cancelMenu = categorySize + 2; // 주문 취소

            if (categoryChoice >= 1 && categoryChoice <= categorySize) {
                Category selectedCategory = categories.get(categoryChoice - 1);
                browseCategory(selectedCategory);
            } else if (categoryChoice == cartMenu) {
                showCart();
                order();
            } else if (categoryChoice == cancelMenu) {
                System.out.println("주문을 취소했습니다.");
                cart.clear();
                return;
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
            if (cart.get(i).getProduct().equals(product)) {
                foundIndex = i;
                break;
            }
        }

        // 2) 장바구니 수량/보유 재고 파악 (루프 밖에서 1회)
        int inCartStock = (foundIndex >= 0) ? cart.get(foundIndex).getCartStock() : 0;
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
            cart.set(foundIndex, new CartItem(item.getProduct(), item.getCartStock() + 1));
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
                Product product = item.getProduct();
                int cartStock = item.getCartStock();
                String cartList = String.format("%-13s | %,10d원 | %s | 수량: %d개",
                        product.getPdName(),
                        product.getPdPrice(),
                        product.getPdDescription(),
                        cartStock
                );
                System.out.println(cartList);
            }

        String total = String.format("%,d원", getCartTotalPrice()); // 장바구니에 담긴 상품의 총 가격을 출력
        System.out.println();
        System.out.println("[ 총 주문 금액 ]");
        System.out.println(total);
        System.out.println();
    }

    // 총 금액 계산 전용 기능 (외부 선언 및 호출 가능하도록 수정)
    private int getCartTotalPrice() {
        int totalPrice = 0;
        for (int i = 0; i < cart.size(); i++) {
            CartItem item = cart.get(i);
            totalPrice += item.getProduct().getPdPrice() * item.getCartStock(); // 장바구니 상품 * 장바구니에 담긴 상품의 수량을 곱한 값을 총합으로 합침
        }
        return totalPrice;
    }

    // 주문 기능
    private void order() {
        String orderCheck = String.format("%-10s %-15s", "1. 주문 확정", "2. 메인으로 돌아가기");
        System.out.println(orderCheck);
        int orderChoice = sc.nextInt();
        if (orderChoice == 1) {
            String orderTotal = String.format("%,d원", getCartTotalPrice());
            System.out.println("주문이 완료되었습니다! 총 금액 : " + orderTotal);

            // 장바구니의 주문수량만큼 재고 차감
            for (int i = 0; i < cart.size(); i++) {
                CartItem item = cart.get(i);
                Product product = item.getProduct();
                int cartStock = item.getCartStock();

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

        } else if (orderChoice == 2) {
            return;
        } else {
            System.out.println("올바른 숫자를 입력하세요!");
            return;
        }
    }

    // CartItem 클래스 생성 (병렬배열 제거 및 단일배열로 수정하기 위함)
    class CartItem {
        private final Product product;
        private final int cartStock;

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

