package com.commerce.example;

import com.commerce.example.domain.Category;
import com.commerce.example.domain.Product;
import com.commerce.example.service.SearchEngine;

import java.util.*;

public class PerformanceTest {

    private static final int N = 10_000; // 데이터 개수
    private static final String input = "Product_5000"; // 측정 대상 키

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

        // 랜덤 1000회 성능 비교
        Random rand = new Random();
        int trials = 1000; // 1000회 랜덤 실행
        long totalLinear = 0;
        long totalBinary = 0;

        for (int t = 0; t < trials; t++) {
            int idx = rand.nextInt(N); // 0 ~ 9999
            String randomTarget = "Product_" + pad4(idx + 1);

            totalLinear += measureLinearSearch(products, randomTarget);
            totalBinary += measureBinarySearch(products, randomTarget);
        }

        long avgLinear = totalLinear / trials;
        long avgBinary = totalBinary / trials;

        System.out.println("\n[랜덤 검색 1000회 평균 성능]");
        System.out.println("완전탐색 평균 " + fmt(avgLinear) + "ns");
        System.out.println("이진탐색 평균 " + fmt(avgBinary) + "ns");

        double speedup = (avgBinary == 0) ? 0.0 : (double) avgLinear / (double) avgBinary;
        System.out.println("🚀 평균 성능 향상: " + String.format("%.2f배", speedup));

        // 입력값에 따른 검색 대상 지정 및 성능 시간 측정
        Scanner sc = new Scanner(System.in);
        System.out.print("\n검색어를 입력하세요(예시 Product_1000): ");
        String input = sc.nextLine().trim();

        // 완전탐색 시간 측정
        long linearTime = measureLinearSearch(products, input);
        System.out.println("\n[검색 성능 비교 테스트]");
        System.out.println("검색어: \"" + input + "\"\n");
        System.out.println("완전탐색 결과:");
        System.out.println("- 실행시간: " + fmt(linearTime) + "ns");
        System.out.println("- 비교횟수: " + fmt(lastLinearComparisons) + "회");
        System.out.println("- 결과 " + (lastLinearFound == null ? "없음" : lastLinearFound.getPdName() + " 찾음"));

        // === 이진탐색(binary, iterative) 측정 ===
        long binaryTime = measureBinarySearch(products, input);
        System.out.println("\n이진탐색 결과:");
        System.out.println("- 실행시간: " + fmt(binaryTime) + "ns");
        System.out.println("- 비교횟수: " + fmt(lastBinaryComparisons) + "회");
        System.out.println("- 결과: " + (lastBinaryFound == null ? "없음" : lastBinaryFound.getPdName() + " 찾음"));

        speedup = (binaryTime == 0) ? 0.0 : (double) linearTime / (double) binaryTime;
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

    public static void runPrefixSearchFromCategories(List<Category> categories) {
        List<Product> all = new ArrayList<>();
        for (Category c : categories) {
            all.addAll(c.getProducts());
        }
        runPrefixSearch(all);
    }

        public static void runPrefixSearch(List<Product> all) {
            System.out.println("=== 상품 검색 시스템 ===");
            if ( all == null || all.isEmpty() ) {
                System.out.println("[PrefixSearch] 상품 목록이 비어 있습니다.");
                return;
            }
            Scanner sc = new Scanner(System.in);
            System.out.print("검색할 상품명을 입력하세요: ");
            String prefix = sc.nextLine().trim();

            SearchEngine engine = new SearchEngine(all);
            List<Product> hits = engine.searchByPrefix(prefix, Integer.MAX_VALUE);

            System.out.println("\n[ " + prefix + "으로 시작하는 상품 ]");
            if (hits.isEmpty()) {
                System.out.println("검색 결과가 없습니다.");
                return;
            }
            int idx = 1;
            for (Product p : hits) {
                System.out.printf("%2d. %s | %,d원 | 재고: %d%n",
                        idx++, p.getPdName(), p.getPdPrice(), p.getPdStock());
            }
            System.out.println("\n총 " + hits.size() + "개 상품");
        }
    }
