package com.commerce.example;

import java.util.ArrayList;
import java.util.List;

/* 장바구니 기능 클래스 생성 및 분리 진행 */
public class CartService {
    private final List<CommerceSystem.CartItem> cart =  new ArrayList<>();
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
            String alertStock = String.format(
                    "판매가능한 재고가 부족합니다. 현재 재고 %d개, 장바구니 수량 %d개",
                    stock, inCartStock
            );
            System.out.println(alertStock);
            return; // 중요: 추가하지 않고 종료
        }

        // 4) 추가/증량 처리 (루프 밖에서 1회)
        if (foundIndex >= 0) {
            CommerceSystem.CartItem item = cart.get(foundIndex);
            cart.set(foundIndex, new CommerceSystem.CartItem(item.product(), item.cartStock() + 1));
        } else {
            cart.add(new CommerceSystem.CartItem(product, 1));
        }

        // 5) 안내 출력
        System.out.println("=======================================");
        String addCartItem = String.format(" %s 가 장바구니에 추가되었습니다.", product.getPdName());
        System.out.println(addCartItem);
    }

    // 장바구니 출력 기능
    public void showCart() {
        System.out.println("아래와 같이 주문 하시겠습니까? ");
        for (int i = 0; i < cart.size(); i++) {
            CommerceSystem.CartItem item = cart.get(i);
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
        System.out.println();
        System.out.println("[ 총 주문 금액 ]");
        System.out.printf("%,d원%n", subtotal);
    }

    // 총 금액 계산 전용 기능 (외부 선언 및 호출 가능하도록 수정)
    public int getCartTotalPrice() {
        int totalPrice = 0;
        for (int i = 0; i < cart.size(); i++) {
            CommerceSystem.CartItem item = cart.get(i);
            totalPrice += item.product().getPdPrice() * item.cartStock(); // 장바구니 상품 * 장바구니에 담긴 상품의 수량을 곱한 값을 총합으로 합침
        }
        return totalPrice;
    }

    // 등급 할인 적용 총액 계산
    public int getDiscountedTotal(int subtotal, CustomerRank rank) {
        return rank.apply(subtotal);
    }

    // 할인 금액 계산
    public int getDiscountAmount(int subtotal, CustomerRank rank) {
        return subtotal - getDiscountedTotal(subtotal, rank);
    }

    // 외부에서 장바구니 접근이 필요할 때만 읽기 용도로 제공
    public List<CommerceSystem.CartItem> getCartItems() {
        return cart;
    }

    // 장바구니 비우기
    public void clearCart() {
        cart.clear();
    }
}
