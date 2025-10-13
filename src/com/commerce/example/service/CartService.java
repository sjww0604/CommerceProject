package com.commerce.example.service;

import com.commerce.example.domain.CustomerRank;
import com.commerce.example.domain.Product;
import com.commerce.example.view.Format;
import com.commerce.example.view.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
