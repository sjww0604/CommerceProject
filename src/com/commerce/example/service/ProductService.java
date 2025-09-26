package com.commerce.example.service;

import com.commerce.example.domain.Category;
import com.commerce.example.domain.Product;

import java.util.List;

/* CommerceSystem의 조회/검증/수정/삭제 로직을 옮겨옴 */
public class ProductService {
    private final List<Category> categories;

    public ProductService(List<Category> categories) {
        this.categories = categories;
    }

    /* 카테고리를 읽기 전용 접근자로 설정 */
    public List<Category> getCategories() {
        return categories;
    }

    public Category getCategory(int index) {
        if (index < 0 || index >= categories.size()) {
            throw new IndexOutOfBoundsException("올바른 카테고리 번호를 입력하세요.");
        }
        return categories.get(index);
    }

    /* 가격에 따른 상품 필터 */
    public List<Product> findByUnder(int maxPrice) {
        return categories.stream()
                .flatMap(cat -> cat.getProducts().stream()) // 여기서 getProducts() 사용 가능
                .filter(p -> p.getPdPrice() <= maxPrice)
                .toList();
    }

    /* 상품 참조용 레코드 (카테고리/위치/상품)
     * record에 내재된 기능으로 인해 메서드처럼 호출 가능*/
    public record ProductRef(Category category, int index, Product product) {
    }


    /* 상품명을 찾고 검증 예외처리 조건을 통해 이슈를 강하게 처리 */
    public ProductRef findByProductName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("상품명이 비어 있습니다.");
        }
        for (Category cat : categories) {
            for (int i = 0; i < cat.size(); i++) {
                Product p = cat.getProduct(i);
                if (p.getPdName().equals(name)) {
                    return new ProductRef(cat, i, p);
                }
            }
        }
        throw new IllegalStateException("일치하는 상품을 찾을 수 없습니다.");
    }

    /* 카테고리에 상품 추가 */
    public void addProduct(int categoryIdx, Product p) {
        getCategory(categoryIdx).addProduct(p);
    }

    /* 가격 수정 */
    public boolean updatePrice(String name, int newPrice) {
        ProductRef ref = findByProductName(name);
        ref.product().setPdPrice(newPrice);
        return true;
    }

    /* 설명 수정 */
    public boolean updateDescription(String name, String newDesc) {
        ProductRef ref = findByProductName(name);
        ref.product().setPdDescription(newDesc);
        return true;
    }

    /* 재고 수정 */
    public boolean updateStock(String name, int newStock) {
        ProductRef ref = findByProductName(name);
        ref.product().setPdStock(newStock);
        return true;
    }

    /* 이름으로 삭제 (장바구니 정리는 CommerceSystem에서 처리) */
    public boolean removeByName(String name) {
        ProductRef ref = findByProductName(name);
        ref.category().removeProduct(ref.index());
        return true;
    }

    /* 전체 상품 현황 출력 */
    public void printAll() {
        System.out.println("[ 전체 상품 현황 ]");
        for (Category cat : categories) {
            System.out.println();
            System.out.println("[ " + cat.getCategoryName() + " ]");
            for (int j = 0; j < cat.size(); j++) {
                Product prod = cat.getProduct(j);
                System.out.printf("%-13s | %,10d원 | %s | 재고: %d개%n",
                        prod.getPdName(), prod.getPdPrice(), prod.getPdDescription(), prod.getPdStock());
            }
        }
    }
}




