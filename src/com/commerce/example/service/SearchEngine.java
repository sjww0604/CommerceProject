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
            } else if (cmp < 0) {
                left = mid + 1;
            }
        }
        return null; // 값을 찾지 못했을 때 null 값 반환
    }

    private int compareName(Product p, String targetName) {
        return p.getPdName().compareToIgnoreCase(targetName);
    }
}

