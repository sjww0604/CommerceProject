package com.commerce.example.service;

import com.commerce.example.domain.Product;

import java.util.Date;

public class CartAction {
    public enum ActionType { ADD, REMOVE }

    private final ActionType type; // ADD, REMOVE
    private final Product product;
    private final int quantity;
    private final Date timestamp;

    // 생성자, getter, setter

    public CartAction(ActionType type, Product product, int quantity) {
        this.type = type;
        this.product = product;
        this.quantity = quantity;
        this.timestamp = new Date();
    }

    public ActionType getType() { return type; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public Date getTimestamp() { return timestamp; }
}
