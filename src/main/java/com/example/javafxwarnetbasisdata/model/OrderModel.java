package com.example.javafxwarnetbasisdata.model;

public record OrderModel(
        String order_id,
        String customer_id,
        String komputer_id,
        double harga,
        String status
) {
}
