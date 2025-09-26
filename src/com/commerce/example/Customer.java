package com.commerce.example;

/* 고객 정보를 관리하는 클래스
* 고객명, 이메일, 등급 필드를 가짐*/
public class Customer {
    //속성
    /* 고객 계정 정보 - 관리자 모드 관련하여 우선 사용 */
    public static class Account {
    private String userPassword; // 로그인용 비밀번호
    private boolean admin; // 관리자 계정 여부 체크
        public Account(String userPassword, boolean admin) {
            this.userPassword = userPassword;
            this.admin = admin;
        }

        public String getUserPassword() { return userPassword; }
        public boolean getAdmin() { return admin; }
    }

    //생성자
    public static final Account ADMIN_ACCOUNT = new Account("admin",  true);
}
