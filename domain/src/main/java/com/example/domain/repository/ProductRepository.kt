package com.example.domain.repository

import com.example.domain.model.Product

interface ProductRepository {
    fun getAllProducts(): List<Product>
}
