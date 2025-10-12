package com.commerce.example;

import com.commerce.example.domain.Category;
import com.commerce.example.domain.Product;
import com.commerce.example.service.SearchEngine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PerformanceTest {

    private static final int N = 10_000; // 데이터 개수
    private static final String TARGET = "Product_5000"; // 측정 대상 키

    // 최근 측정 상태(비교횟수/결과) 저장 -> compareSearchPerformance 에서 출력에 쓰기 위해 선언
    private static int lastLinearComparisons = 0;
    private static int lastBinaryComparisons = 0;
    private static Product lastLinearFound = null;
    private static Product lastBinaryFound = null;

    // 보조 포맷
    private static String pad4(int i) { return String.format("%04d", i);}
    private static String fmt(long n) { return String.format("%,d", n);}

    private static java.util.List<Product> generateProducts(int n) {
        java.util.List<Product> list = new java.util.ArrayList<>(n);
        for (int i = 1; i <= n; i++) {
            String name = "Product_" + pad4(i);
            list.add(new Product(name, 0, "auto-generated", 0));
        }
        return list;
    }

    public void compareSearchPerformance() {
        java.util.List<Product> products = generateProducts(N);
        System.out.println("=== 상품 검색 성능 테스트 ===");
        System.out.println("[대용량 데이터 생성 중...]\n✅ " + fmt(products.size()) + "개 상품 데이터 생성 완료");

        // 완전탐색 시간 측정
        long linearTime = measureLinearSearch(products, TARGET);
        System.out.println("\n[검색 성능 비교 테스트]");
        System.out.println("검색어: \"" + TARGET + "\"\n");
        System.out.println("완전탐색 결과:");
        System.out.println("- 실행시간: " + fmt(linearTime) + "ns");
        System.out.println("- 비교횟수: " + fmt(lastLinearComparisons) + "회");
        System.out.println("- 결과 " + (lastLinearFound == null ? "없음" : lastLinearFound.getPdName() + " 찾음"));

        // === 이진탐색(binary, iterative) 측정 ===
        long binaryTime = measureBinarySearch(products, TARGET);
        System.out.println("\n이진탐색 결과:");
        System.out.println("- 실행시간: " + fmt(binaryTime) + "ns");
        System.out.println("- 비교횟수: " + fmt(lastBinaryComparisons) + "회");
        System.out.println("- 결과: " + (lastBinaryFound == null ? "없음" : lastBinaryFound.getPdName() + " 찾음"));

        double speedup = (binaryTime == 0) ? 0.0 : (double) linearTime / (double) binaryTime;
        System.out.println("\n🚀 성능 향상: " + String.format("%.0f배 빨라짐!", speedup));
    }
    // 완전탐색: TARGET을 앞에서부터 순차 비교
    private long measureLinearSearch(java.util.List<Product> list, String target) {
        lastLinearComparisons = 0;
        lastLinearFound = null;
        long start = System.nanoTime();
        for (Product p : list) {
            lastLinearComparisons++;
            if (p.getPdName().equals(target)) { // 대소문자 구분 정확 일치
                lastLinearFound = p;
                break;
            }
        }
        long end = System.nanoTime();
        return end - start;
    }

    // 이진탐색(반복) : 이름 기준 정렬 후 TARGET 비교
    private long measureBinarySearch(java.util.List<Product> list, String target) {
        // 이름 기준 정렬 (오름차순)
        java.util.List<Product> sorted = new java.util.ArrayList<>(list);
        sorted.sort(Comparator.comparing(Product::getPdName));

        lastBinaryComparisons = 0;
        lastBinaryFound = null;
        long start = System.nanoTime();
        int left = 0, right = sorted.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            Product midP = sorted.get(mid);
            lastBinaryComparisons++;
            int cmp = midP.getPdName().compareTo(target);
            if (cmp == 0) { lastBinaryFound = midP; break; }
            else if (cmp > 0) right = mid - 1;
            else left = mid + 1;
        }
        long end = System.nanoTime();
        return end - start;
    }
    public static void runFromCategories(List<Category> categories) {
        List<Product> all = new ArrayList<>();
        for (Category c : categories) {
            all.addAll(c.getProducts());
        }
        if (all.isEmpty()) {
            System.out.println("[PerformanceTest] 카테고리에 상품이 없습니다.");
            return;
        }
        // 타겟이 카테고리에 없으면 가운데 상품 이름을 타겟으로 사용
        String target = TARGET;
        boolean exists = false;
        for (Product p : all) {
            if (p.getPdName().equals(target)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            target = all.get(all.size() / 2).getPdName();
        }
        PerformanceTest pt = new PerformanceTest();
        long linearTime = pt.measureLinearSearch(all, target);
        long binaryTime = pt.measureBinarySearch(all, target);
        System.out.println();
        System.out.println("=== 카테고리 기반 성능 테스트 ===");
        System.out.println("상품 수: " + fmt(all.size()) + "개, 검색어: \"" + target + "\"");
        System.out.println("완전탐색: " + fmt(linearTime) + "ns, 비교횟수 " + lastLinearComparisons + "회, 결과: " + (lastLinearFound == null ? "없음" : "있음"));
        System.out.println("이진탐색: " + fmt(binaryTime) + "ns, 비교횟수 " + lastBinaryComparisons + "회, 결과: " + (lastBinaryFound == null ? "없음" : "있음"));
        double speedup = (binaryTime == 0) ? 0.0 : (double) linearTime / (double) binaryTime;
        System.out.println("🚀 성능 향상: " + String.format("%.2f배", speedup));
    }
}
