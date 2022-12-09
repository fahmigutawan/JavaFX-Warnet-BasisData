package com.example.javafxwarnetbasisdata.model;

public record FoodModel(
        String makanan_id,
        String pedagang_id,
        Double harga,
        int stok,
        String nama,
        String kategori_id,
        String kategori_word
) {
}
