package com.commerce.example;

/* 개별 상품 정보를 가지는 클래스
* 상품명, 가격, 설명, 재고수량
* 예시) new Product("Galaxy S24", 1200000, "최신 스마트폰", 50 */
public class Product {
    /* 상품명 : pdName, 가격 : pdPrice, 설명 : pdDescription, 재고수량 : pdQty 설정 */
    private String pdName;
    private int pdPrice;
    private String pdDescription;
    public int pdQty;

    // 생성자 : 메서드 선언 시 전달된 매개변수를 this.필드에 저장
    public Product(String pdName, int pdPrice, String pdDescription, int pdQty) {
        this.pdName = pdName;
        this.pdPrice = pdPrice;
        this.pdDescription = pdDescription;
        this.pdQty = pdQty;
    }

    // 기능 : 게터 기능을 사용하여 생성된 객체에 저장된 값들을 읽을 수 있도록 반환
    public String getPdName() {
        return pdName;
    }
    public int getPdPrice() {
        return pdPrice;
    }
    public String getPdDescription() {
        return pdDescription;
    }
    public int getPdQty() {
        return pdQty;
    }
}
