package com.gomguk.kirly.data

data class ProductResponse(
    val data: List<Product>
)

data class Product(
    val id: Int,
    val name: String,
    val image: String,
    val originalPrice: Int,
    val discountedPrice: Int? = null,
    val isSoldOut: Boolean = false,
    var isFavorite: Boolean = false
)
