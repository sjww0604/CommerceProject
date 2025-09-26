package com.commerce.example.domain;


import java.util.ArrayList;
import java.util.List;

/* Product 클래스를 관리하는 클래스
 * 전자제품, 의류, 식품 등 각 카테고리 내에 여러 Product를 포함*/
public class Category {
    // 속성
    private final String categoryName; // 카테고리별 이름 필드 설정
    private final List<Product> productList; // product 관리 필드 (CommerceSystem -> Category 로 목적에 맞게 이동)


    // 생성자
    public Category(String categoryName) {
        this.categoryName = categoryName;
        this.productList = new ArrayList<>();
    }
    public String getCategoryName() {
        return categoryName;
    }

    // 기능
    public void addProduct(Product product) { // 상품 추가 기능
        productList.add(product);
    }
    public Product getProduct(int index) { // 특정 번호에 맞는 상품 반환
        return productList.get(index);
    }
    public List<Product> getProducts() { // 전체 상품 읽기 접근자 설정
        return productList;
    }

    public int size() { // 사이즈 반환
        return productList.size();
    }

    public void printProducts() { // 전체 출력 기능
        /* 기존 for문 -> 스트림으로 변경 */
        java.util.stream.IntStream.range(0, size()).forEach(i -> {
            Product p = getProduct(i);
            System.out.printf("%d. %-15s | %,10d원 | %s%n",
                    i + 1, p.getPdName(), p.getPdPrice(), p.getPdDescription());
        });
    }

    public void removeProduct(int index) { //객체 삭제 기능 추가
        productList.remove(index); // Category 내부의 List<Product> 가정
    }
}
