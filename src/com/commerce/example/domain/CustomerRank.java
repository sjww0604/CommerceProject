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

    private int applyInternal(int subtotal) {
        return (int) Math.round(subtotal * (1- discountRate));
    }

    public static int apply(CustomerRank rank, int subtotal) {
        if (rank == null) {
            throw new IllegalArgumentException("등급 정보가 없습니다.");
        }
        if (subtotal < 0) {
            throw new IllegalArgumentException("총 금액은 음수일 수 없습니다.");
        }
        return rank.applyInternal(subtotal);
    }

    public static int discountAmount(CustomerRank rank, int subtotal) {
        int discounted = apply(rank, subtotal);
        return subtotal - discounted;
    }
}


