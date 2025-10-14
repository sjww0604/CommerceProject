package com.commerce.example.service;

import com.commerce.example.domain.Category;
import com.commerce.example.domain.CustomerRank;
import com.commerce.example.domain.Product;
import com.commerce.example.view.Format;
import com.commerce.example.view.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/* 장바구니 기능 클래스 생성 및 분리 진행 */
public class CartService {
    public static record CartItem(Product product, int cartStock) {}

    public final List<CartItem> cart = new ArrayList<>();
    // 장바구니 추가 기능
    public void addCart(Product product) {
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
            System.out.println(Format.outOfStock(product, stock, inCartStock));
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
        System.out.println(Format.addedToCart(product.getPdName(),1));
    }

    // 장바구니에서 해당 상품 1개만 제거 (수량 1 감소, 0이면 항목 삭제)
    public void removeOne(Product product) {
        int foundIndex = -1;
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).product().equals(product)) {
                foundIndex = i;
                break;
            }
        }
        if (foundIndex < 0) {
            System.out.println("장바구니에 해당 상품이 없습니다: " + product.getPdName());
            return;
        }
        CartItem item = cart.get(foundIndex);
        int newQty = item.cartStock() - 1;
        if (newQty > 0) {
            cart.set(foundIndex, new CartItem(item.product(), newQty));
        } else {
            cart.remove(foundIndex);
        }
        System.out.println("장바구니에서 1개 제거: " + product.getPdName());
    }


    // 장바구니 출력 기능
    public void showCart() {
        if (cart.isEmpty()) {
            System.out.println(Message.CART_EMPTY);
            System.out.println();
            return;
        }

        System.out.println("아래와 같이 주문 하시겠습니까? ");
        for (CartItem item : cart) {
            System.out.println(Format.cartLine(item.product(), item.cartStock));
        }

        int subtotal = getCartTotalPrice(); // 장바구니 총액을 우선 할인 계산하기 위한 subtotal에 저장
        System.out.print(Format.header("총 주문 금액"));
        System.out.printf("%,d원%n", subtotal);
    }

    // 장바구니 2.0 출력 화면
    public void showCartInteractive(List<Category> categories, CartHistory cartHistory, Scanner sc) {
        boolean loop = true;
        while (loop) {
            System.out.println();
            System.out.println("=== 장바구니 시스템 2.0 ===");
            System.out.println();
            showCart(); // 기존 출력 활용

            System.out.println();
            System.out.println(String.format("1. %s | 2. %s | 3. %s (⌘+Z) | 4. %s (⌘+Y)",
                    "상품 추가", "상품 삭제", "Undo", "Redo"));
            System.out.println(String.format("5. %s | 6. %s | 0. %s",
                    "장바구니 히스토리", "주문하기", "메인으로"));
            System.out.print(Message.PROMPT_SELECT);

            int sel = sc.nextInt();
            sc.nextLine();

            switch (sel) {
                case 0 -> loop = false;
                case 1 -> {
                    Product p = promptProductByName(categories, sc);
                    if (p == null) break;
                    int before = qtyInCart(p);
                    addCart(p);
                    int after = qtyInCart(p);
                    if (after > before) {
                        cartHistory.addAction(new CartAction(CartAction.ActionType.ADD, p, 1));
                    }
                }
                case 2 -> {
                    Product p = promptProductByNameFromCart(sc);
                    if (p == null) break;
                    int before = qtyInCart(p);
                    removeOne(p);
                    int after = qtyInCart(p);
                    if (after < before) {
                        cartHistory.addAction(new CartAction(CartAction.ActionType.REMOVE, p, 1));
                    }
                }
                case 3 -> {
                    CartAction top = cartHistory.peekUndo();
                    if (top == null) {
                        System.out.println("되돌릴 작업이 없습니다.");
                        break;
                    }
                    cartHistory.undo(this);
                    System.out.println(String.format("⏪️ Undo 실행 : %s %d개 작업을 취소했습니다.%n",
                            top.getProduct().getPdName(), top.getQuantity()));
                }
                case 4 -> {
                    CartAction top = cartHistory.peekRedo();
                    if (top == null) {
                        System.out.println("다시할 작업이 없습니다.");
                        break;
                    }
                    cartHistory.redo(this);
                    System.out.println(String.format("⏩️ Redo 실행 : %s %d개 작업을 다시 수행했습니다.%n",
                            top.getProduct().getPdName(), top.getQuantity()));
                }
                case 5 -> {
                    System.out.println("\n[ 장바구니 히스토리 ]");
                    CartAction u = cartHistory.peekUndo();
                    CartAction r = cartHistory.peekRedo();
                    if (u == null && r == null) {
                        System.out.println("기록이 없습니다.");
                    } else {
                        System.out.println("- 다음 Undo: " + (u == null ? "없음"
                                : u.getType() + " " + u.getProduct().getPdName() + " x" + u.getQuantity()));
                        System.out.println("- 다음 Redo: " + (r == null ? "없음"
                                : r.getType() + " " + r.getProduct().getPdName() + " x" + r.getQuantity()));
                    }
                }
                case 6 ->
                    // 주문 진행: 이 화면을 빠져나가면 CommerceSystem에서 주문 메뉴로 이어집니다.
                    loop = false;

                default -> System.out.println(Message.INVALID_NUMBER);
            }
        }
    }
    // CartService 내부 화면 구성
    private Product promptProductByName(List<Category> categories, Scanner sc) {
        System.out.print("상품명을 입력하세요");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) return null;
        for (Category c : categories) {
            for (int i = 0; i < c.size(); i++) {
                Product p = c.getProduct(i);
                if (p.getPdName().equalsIgnoreCase(name)) return p;
            }
        }
        System.out.println("해당 상품을 찾을 수 없습니다.");
        return null;
    }

    private Product promptProductByNameFromCart(Scanner sc) {
        if (cart.isEmpty()) {
            System.out.println("장바구니가 비어 있습니다.");
            return null;
        }
        System.out.print("장바구니에서 제거할 상품명을 입력하세요: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) return null;
        for (CartItem item : cart) {
            if (item.product().getPdName().equalsIgnoreCase(name)) {
                return item.product();
            }
        }
        System.out.println("장바구니에 해당 상품이 없습니다.");
        return null;
    }

    private int qtyInCart(Product p) {
        for (CartItem item : cart) {
            if (item.product().equals(p)) return item.cartStock();
        }
        return 0;
    }

    // 총 금액 계산 전용 기능 (외부 선언 및 호출 가능하도록 수정)
    public int getCartTotalPrice() {
        int total = 0;
        for (CartItem item : cart) {
            total += item.product().getPdPrice() * item.cartStock();
        }
        return total;
    }

    // 등급 할인 적용 총액 계산
    public int getDiscountedTotal(int subtotal, CustomerRank rank) {
        return CustomerRank.apply(rank,subtotal);
    }

    // 할인 금액 계산
    public int getDiscountAmount(int subtotal, CustomerRank rank) {
        return subtotal - getDiscountedTotal(subtotal, rank);
    }

    // 외부에서 장바구니 접근이 필요할 때만 읽기 용도로 제공
    public List<CartItem> getCartItems() {
        return cart;
    }

    // 장바구니 비우기 (주문처리 이후)
    public void clearCart() {
        cart.clear();
    }
}
