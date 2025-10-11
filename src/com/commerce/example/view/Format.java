package com.commerce.example.view;

import com.commerce.example.domain.CustomerRank;
import com.commerce.example.domain.Product;

public final class Format {
    private Format() {}

    // 상품 상세 양식
    public static String productLine(Product product) {
        return String.format("%-13s | %,10d원 | %s | 재고 %d개",
                product.getPdName(), product.getPdPrice(), product.getPdDescription(), product.getPdStock());
    }

    // 재고 업데이트 양식
    public static String stockUpdate(Product product, int before, int after) {
        return String.format("%s 재고가 %d개 → %d개로 업데이트되었습니다.",
                product.getPdName(), before, after);
    }

    // 등급 할인율 적용 양식
    public static String rankLine(int index, CustomerRank rank) {
        return String.format("%d. %-10s : %.0f%% 할인",
                index, rank.name(), rank.getDiscountRate()*100);
    }

    // 결제 요약 화면
    public static String paymentSummary(int subtotal, CustomerRank rank, int discountAmt, int finalPay) {
        return String.format("""
            할인 전 금액: %,d원
            %s 등급 할인(%.0f%%): -%,d원
            최종 결제 금액: %,d원
            """,
                subtotal, rank.name(), rank.getDiscountRate()*100, discountAmt, finalPay);
    }

    // Format.java (추가)
    public static String cartLine(Product p, int qty) {
        return String.format("%-13s | %,10d원 | %s | 수량 %d개",
                p.getPdName(), p.getPdPrice(), p.getPdDescription(), qty);
    }

    public static String outOfStock(Product p, int stock, int inCart) {
        return String.format("재고 부족: %s (재고 %d개, 장바구니 %d개)", p.getPdName(), stock, inCart);
    }

    public static String addedToCart(String name, int qty) {
        return String.format("'%s' %d개가 장바구니에 추가되었습니다.", name, qty);
    }

    // 섹션 헤더
    public static String header(String title) {
        return String.format(Message.MENU_HEADER, title);
    }
}
