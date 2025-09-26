package com.commerce.example.domain;

public enum CustomerRank {
    /* 등급별 할인율을 double형으로 선언 */
    BRONZE(0.0),
    SILVER(0.05),
    GOLD(0.1),
    PLATINUM(0.15);

    private final double discountRate;
    CustomerRank(double discountRate) {
        this.discountRate = discountRate;
    }
    public double getDiscountRate() {
        return discountRate;
    }

    /* 총금액 할인 적용 정수금액을 계산할 메서드 생성 */
    public int apply (int subtotal) {
        return (int) Math.round(subtotal * (1 - discountRate));
    }
}


