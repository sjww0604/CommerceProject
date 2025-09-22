# CommerceProject
JAVA 문법 종합반 커머스 과제 레포지토리입니다.<br>

## 사용된 기술 및 실행환경
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">

## 과제의 목표
- 계산기 과제 개념의 확장 버전으로, 여러 객체가 협력하는 객체 지향 설계 구조를 커머스 플랫폼에 적용해보는 경험 얻기
- 상품,카테고리, 고객, 주문 등 다양한 객체를 정의하고 서로 협력하며 동작하는 구조를 만드는 것이 핵심

## 프로젝트 구조
<pre>
**commerceProject**

src/
├── com/
│   └── commerce/        
│       └── example/   
│           ├── Main.java // 시작 지점이 되는 클래스
│           ├── Product.java // 개별 상품 정보를 가지는 클래스
│           ├── CommerceSystem.java // 프로그램 비즈니스 로직 클래스 
│           ├── Category.java // Product클래스를 관리하는 클래스

</pre>
- Main.java : 필요한 객체들을 생성하고 호출하는 흐름 제어 역할
- Product.java : 데이터 저장 객체 (DTO 개념)
- CommerceSystem.java : 동작을 담당하는 핵심 로직을 담음
- Category.java : Product를 묶는 관리 단위 (DTO + Collection 개념)

#### MVC 관점
- Model → Product, Category
- Service/Logic → CommerceSystem
- Controller → Main

## 필수기능
- [ ] 기초적인 흐름 제어 및 객체 지향 설계 개념 복습 
- [ ] 커머스 플랫폼의 특성 (상품/카테고리/고객/주문/재고)을 고려하여 요구사항을 반영한 클래스 설계
- [ ] ⭐️ 중요 ⭐️  단순 기능 구현에 그치지 않고, Product, Category, Customer, CommerceSystem 등 각 객체의 역할과 책임(R&R)에 대해 고민할 것 

## 도전기능
- [ ] 스스로 필요한 객체를 자유롭게 정의하고, 객체 간 협력 흐름을 설계해볼 것
- [ ] Enum, 람다, 스트림을 적극  활용해 고객등급별 할인, 가격대 필터링, 장바구니 항목 제거/검색 등의 기능을 설계 구현해볼 것


