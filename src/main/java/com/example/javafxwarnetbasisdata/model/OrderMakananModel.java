package com.example.javafxwarnetbasisdata.model;

public record OrderMakananModel(
        String order_id,
        String customer_id,
        String pedagang_id,
        Double total_harga,
        String status
) {
}
