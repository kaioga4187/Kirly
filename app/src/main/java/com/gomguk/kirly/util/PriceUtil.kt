package com.gomguk.kirly.util

data class PriceInfo(
    val priceText: String,
    val originalPriceText: String?,
    val discountPercentText: String?,
    val showDiscount: Boolean
)

object PriceUtil {
    fun calculatePriceInfo(
        originalPrice: Int,
        discountedPrice: Int?
    ): PriceInfo {
        return if (discountedPrice != null && discountedPrice < originalPrice) {
            val discountPercent = ((originalPrice - discountedPrice).toDouble() / originalPrice * 100).toInt()
            PriceInfo(
                priceText = "${discountedPrice}원",
                originalPriceText = "${originalPrice}원",
                discountPercentText = "$discountPercent%",
                showDiscount = true
            )
        } else {
            PriceInfo(
                priceText = "${originalPrice}원",
                originalPriceText = null,
                discountPercentText = null,
                showDiscount = false
            )
        }
    }
}
