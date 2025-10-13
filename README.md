# CommerceProject
JAVA 문법 종합반 커머스 과제 레포지토리입니다.<br>

## 사용된 기술 및 실행환경
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white" alt="">

![Java 17](https://img.shields.io/badge/Java%2017-OpenJDK-blue?style=for-the-badge&logo=openjdk&logoColor=white)

## 과제의 목표
- 계산기 과제 개념의 확장 버전으로, 여러 객체가 협력하는 객체 지향 설계 구조를 커머스 플랫폼에 적용해보는 경험 얻기
- 상품,카테고리, 고객, 주문 등 다양한 객체를 정의하고 서로 협력하며 동작하는 구조를 만드는 것이 핵심

## 프로젝트 구조
<pre>
**commerceProject**

📂 src/com/commerce/example/
├───── 📂 domain/       
│      ├── Main.java        // 시작 지점이 되는 클래스
│      ├── Product.java     // 개별 상품 정보를 가지는 클래스
│      ├── Category.java    // 상품 카테고리 관리 클래스
│      ├── Customer.java    // 고객 정보 클래스
│      └── CustomerRank.java // 고객 등급 Enum 클래스
├───── 📂 service/      
│      ├── AdminService.java    // 관리자 모드 제어 (상품 CRUD 및 관리 기능)
│      ├── CartService.java     // 장바구니 담기/삭제/총액 계산 기능 담당
│      ├── CommerceSystem.java  // 프로그램 전체 흐름 및 주요 로직 제어
│      ├── OrderService.java    // 주문 처리 및 할인 정책 적용 담당
│      └── ProductService.java  // 상품 관련 검색, 수정, 삭제, 출력 기능 담당
│      └── SearchEngine.java    // 상품명 접두사 검색 기능 제공 (이진 탐색 기반) 
└───── PerformanceTest.java     // 대용량 데이터 생성 및 탐색 알고리즘 성능 비교, 접두사 검색 성능 검증 클래스
</pre>

#### MVC 관점
- Model → Product, Category, Customer, CustomerRank
- Service/Logic → ProductService, CartService, OrderService, AdminService
- Controller → CommerceSystem, Main

## 필수기능
- [x] 기초적인 흐름 제어 및 객체 지향 설계 개념 복습 
- [x] 커머스 플랫폼의 특성 (상품/카테고리/고객/주문/재고)을 고려하여 요구사항을 반영한 클래스 설계
- [x] ⭐️ 중요 ⭐️  단순 기능 구현에 그치지 않고, Product, Category, Customer, CommerceSystem 등 각 객체의 역할과 책임(R&R)에 대해 고민할 것 

## 도전기능
- [x] 스스로 필요한 객체를 자유롭게 정의하고, 객체 간 협력 흐름을 설계해볼 것
- [x] Enum, 람다, 스트림을 적극  활용해 고객등급별 할인, 가격대 필터링, 장바구니 항목 제거/검색 등의 기능을 설계 구현해볼 것

### 📈 확장/테스트 기능
- `SearchEngine` : 접두사 검색 로직 구현
- `PerformanceTest` : 탐색 알고리즘 성능 비교 및 검증용 클래스


