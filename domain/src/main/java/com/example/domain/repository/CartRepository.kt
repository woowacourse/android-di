package com.example.domain.repository

import com.example.domain.model.CartProduct
import com.example.domain.model.Product

interface CartRepository {
    suspend fun addCartProduct(product: Product)

    suspend fun getAllCartProducts(): List<CartProduct>

    suspend fun deleteCartProduct(id: Long)
}