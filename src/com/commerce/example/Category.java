package com.commerce.example;


import java.util.ArrayList;
import java.util.List;

/* Product 클래스를 관리하는 클래스
* 전자제품, 의류, 식품 등 각 카테고리 내에 여러 Product를 포함*/
public class Category {
    // 속성
    private List<Product> productList; // product 관리 필드 (CommerceSystem -> Category 로 목적에 맞게 이동)
    public Category(List<Product> productList) {
        this.productList = productList;
    }


    // 생성자



    public void printProducts() {
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            String formatted = String.format( // 출력 양식은 맞추되 저장되어있는 배열 전체 화면 출력을 위한 용도
                    "%d. %-15s | %,10d원 | %s",
                    i + 1,
                    product.getPdName(),
                    product.getPdPrice(),
                    product.getPdDescription()
            );
            System.out.println(formatted);
        }
    }


    // 기능
    public Product getProduct(int index) {
        return productList.get(index);
    }

    public int size() {
        return productList.size();
    }
}
