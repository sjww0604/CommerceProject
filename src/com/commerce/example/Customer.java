package com.commerce.example;

/* 고객 정보를 관리하는 클래스
* 고객명, 이메일, 등급 필드를 가짐*/
public class Customer {
    //속성
    private String customerName;
    private String customerEmail;
    private char customerRank;

    //생성자
    /* */
    public Customer(String customerName, String customerEmail, char customerRank) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerRank = customerRank;
    }

    public String getCustomerName() {
        return customerName;
    }
    public String getCustomerEmail() {
        return customerEmail;
    }
    public char getCustomerRank() {
        return customerRank;
    }
    //기능
}
