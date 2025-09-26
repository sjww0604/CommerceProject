package com.commerce.example.service;

import com.commerce.example.domain.CustomerRank;
import com.commerce.example.domain.Product;

import java.util.Scanner;

/* 주문처리 전담 서비스
 * 입력과 출력의 구현기능을 전반적으로 담당 */
public class OrderService {
    private Scanner sc =  new Scanner(System.in);
    private final CartService cartService;
    private CustomerRank currentRank;

    public OrderService(Scanner sc, CartService cartService) {
        this.sc = sc;
        this.cartService = cartService;
    }

    /* 주문 처리 프로세스 : 장바구니 확인 -> 등급 선택 -> 주문 확정 */
    public void showOrderMenu() {
        if (cartService.getCartItems().isEmpty()) {
            System.out.println("장바구니가 비어있습니다.");
            return;
        }
        int subtotal = cartService.getCartTotalPrice();

        while (true) {
            System.out.println("고객 등급을 입력해주세요.");
            for (int i = 0; i < CustomerRank.values().length; i++) {
                CustomerRank rank =  CustomerRank.values()[i];
                System.out.printf("%d. %-10s : %.0f %% 할인%n",
                        (i+1),
                        rank.name(),
                        rank.getDiscountRate()*100
                );
            }
            System.out.println();
            System.out.printf("%-10s %-15s %-10s%n", "1.등급 선택", "2. 메인으로 돌아가기", "3. 주문 확정");

            int orderChoice = sc.nextInt();
            sc.nextLine();

            if (orderChoice == 1) {
                System.out.print("등급번호를 입력하세요 (1~4) : ");
                int rankChoice = sc.nextInt();
                sc.nextLine();
                /* 고객등급의 값을 각 입력값에 맞게 저장 */
                switch (rankChoice) {
                    case 1:
                        currentRank = CustomerRank.BRONZE; break;
                    case 2:
                        currentRank = CustomerRank.SILVER; break;
                    case 3:
                        currentRank = CustomerRank.GOLD; break;
                    case 4:
                        currentRank = CustomerRank.PLATINUM; break;
                    default:
                        System.out.println("올바른 숫자를 입력하세요!");
                        continue;
                }
                /* 기존에 선언했던 할인금액과 할인 이후 총 금액의 값을 받아와 정수형 변수에 저장
                 * 등급 선택에 따른 변화되는 수치를 보여줄 수 있도록 출력화면 구성 */
                int discountAmt = cartService.getDiscountAmount(subtotal, currentRank);
                int finalPay = cartService.getDiscountedTotal(subtotal, currentRank);
                System.out.printf("\n할인 전 금액: %,d원%n", subtotal);
                System.out.printf("%s 등급 할인(%.0f%%): -%,d원%n",
                        currentRank.name(), currentRank.getDiscountRate() * 100, discountAmt);
                System.out.printf("최종 결제 금액: %,d원\n%n", finalPay);
            } else if (orderChoice == 2) {
                return; // 메인으로
            } else if (orderChoice == 3) {
                if (currentRank == null) {
                    System.out.println("고객 등급을 먼저 선택해주세요.");
                    System.out.println();
                    continue;
                }
                // 최종 결제 확정
                int discountAmt = cartService.getDiscountAmount(subtotal, currentRank);
                int finalPay = cartService.getDiscountedTotal(subtotal, currentRank);

                System.out.println("주문이 완료되었습니다!!");
                System.out.printf("할인 전 금액: %,d원%n", subtotal);
                System.out.printf("%s 등급 할인(%.0f%%): -%,d원%n", currentRank.name(), currentRank.getDiscountRate() * 100, discountAmt);
                System.out.printf("최종 결제 금액: %,d원%n", finalPay);

                // 재고 차감
                for (int i = 0; i < cartService.getCartItems().size(); i++) {
                    CommerceSystem.CartItem item = cartService.getCartItems().get(i);
                    Product product = item.product();
                    int cartStock = item.cartStock();

                    int beforeStock = product.getPdStock();
                    int afterStock = beforeStock - cartStock;
                    product.setPdStock(afterStock);

                    String stockStatus = String.format("%s 재고가 %d개 → %d개로 업데이트되었습니다.",
                            product.getPdName(), beforeStock, afterStock);
                    System.out.println(stockStatus);
                }
                System.out.println();

                cartService.clearCart(); // 장바구니 비우기
                return;       // 주문 종료 후 메인으로

            } else {
                System.out.println("올바른 숫자를 입력하세요!");
            }
        }
    }
}


