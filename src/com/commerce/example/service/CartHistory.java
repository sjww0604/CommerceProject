package com.commerce.example.service;

import java.util.Stack;

public class CartHistory {
    private Stack<CartAction> undoStack = new Stack<>();
    private Stack<CartAction> redoStack = new Stack<>();

    public void addAction(CartAction action) {
        undoStack.push(action);
        redoStack.clear(); // 새 액션이 추가되면 redo 스택 초기화
    }

    public boolean undo(CartService cartService) {
        // 구현하세요
        if (undoStack.isEmpty()) return false;
        CartAction a = undoStack.pop();

        switch (a.getType()) {
            case ADD -> {
                for (int i = 0; i < a.getQuantity(); i++) cartService.removeOne(a.getProduct());
            }
            case REMOVE -> {
                for (int i = 0; i < a.getQuantity(); i++) cartService.addCart(a.getProduct());
            }
        }
        redoStack.push(a);
        return true;
    }

    public boolean redo(CartService cartService) {
        // 구현하세요
        if (redoStack.isEmpty()) return false;
        CartAction a = redoStack.pop();
        switch (a.getType()) {
            case ADD -> {
                for (int i = 0; i < a.getQuantity(); i++) cartService.addCart(a.getProduct());
            }
            case REMOVE -> {
                for (int i = 0; i < a.getQuantity(); i++) cartService.removeOne(a.getProduct());
            }
        }
        undoStack.push(a);
        return true;
    }

    // redo, undo 내에 쌓여있는 스택의 값을 미리보기 위해 설정
    public CartAction peekUndo() { return undoStack.isEmpty() ? null : undoStack.peek(); }
    public CartAction peekRedo() { return redoStack.isEmpty() ? null : redoStack.peek(); }
}
