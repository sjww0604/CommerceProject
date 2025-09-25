package com.commerce.example;

/* 고객 정보를 관리하는 클래스
* 고객명, 이메일, 등급 필드를 가짐*/
public class Customer {
    //속성
    /* 고객 기본 정보 */
    private String customerName; // 고객 이름
    private String customerEmail; // 고객 이메일 주소
    private char customerRank; // 고객 등급

    /* 고객 계정 정보 - 관리자 모드 관련하여 우선 사용 */
    public static class Account {
    private String userId; // 로그인용 아이디
    private String userPassword; // 로그인용 비밀번호
    private boolean admin; // 관리자 계정 여부 체크
        public Account(String userId, String userPassword, boolean admin) {
            this.userId = userId;
            this.userPassword = userPassword;
            this.admin = admin;
        }

        public String getUserId() { return userId; }
        public String getUserPassword() { return userPassword; }
        public boolean getAdmin() { return admin; }
    }

    //생성자
    public static final Account ADMIN_ACCOUNT = new Account("admin", "admin", true);

    /* 고객 정보와 계정 정보를 연결 - 후에 고객관련 세부정보 필요시 사용 */
    public Account account;
    public Customer(String customerName, String customerEmail, char customerRank, Account account) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerRank = customerRank;
        this.account = account;
    }

    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public char getCustomerRank() { return customerRank; }
    public Account getAccount() { return account; }
    //기능
}
