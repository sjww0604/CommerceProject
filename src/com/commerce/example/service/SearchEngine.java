package com.commerce.example.service;

import com.commerce.example.domain.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SearchEngine {
    private final List<Product> sortedProducts;

    public SearchEngine(List<Product> sortedProducts) {
        this.sortedProducts = new ArrayList<>(sortedProducts);
        // 이름 기준 정렬하도록(대소문자 무시하여 소문자로만 읽음)
        this.sortedProducts.sort(Comparator.comparing(p -> p.getPdName().toLowerCase()));
    }

    public List<Product> getSortedProducts() {
        return new ArrayList<>(sortedProducts);
    }

    // 이진탐색 - 재귀 방식

    //외부에서 호출할 수 있는 메서드 선언 -> 이름으로만 값을 받아서 실질적인 계산은 내부 메서드가 처리하도록 함
    public Product binarySearchRecursive(String productName) {
        // 편의용 래퍼: 처음엔 left=0, right=끝
        return binarySearchRecursive(productName, 0, sortedProducts.size() - 1);
    }

    public Product binarySearchRecursive(String productName, int left, int right) {
        // 구현하세요
        if (left > right) return null; // 탐색 조건이 만족되지 않는 경우 null 값 반환

        int mid = left + (right - left) / 2;
        Product midProduct = sortedProducts.get(mid); //중간 위치의 상품을 가져옴
        int cmp = compareName(midProduct, productName); // 상품을 비교해서 같으면 0, mid의 값에 따라 양수 음수를 반환

        if (cmp == 0) return midProduct; // 값이 일치한 경우 상품명 그대로 반환
        else if (cmp > 0) return binarySearchRecursive(productName, left, mid - 1); //찾는 이름보다 mid의 값이 뒤에 있으므로 왼쪽 구간으로 탐색하도록 설정
        else return binarySearchRecursive(productName, mid + 1, right);
    }

    // 이진탐색 - 반복문 방식
    public Product binarySearchIterative(String productName) {
        // 구현하세요
        int left = 0;
        int right = sortedProducts.size() - 1; // 배열의 시작점과 끝점의 위치를 찾음
        while (left <= right) {
            int mid = left + (right - left) / 2;
            Product midProduct = sortedProducts.get(mid);
            int cmp = compareName(midProduct, productName);
            if (cmp == 0) {
                return midProduct;
            } else if (cmp > 0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return null; // 값을 찾지 못했을 때 null 값 반환
    }

    private int compareName(Product p, String targetName) {
        return p.getPdName().compareToIgnoreCase(targetName);
    }
}

