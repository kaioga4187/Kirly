package com.gomguk.kurly.util

import org.junit.Assert.assertEquals
import org.junit.Test

class PriceUtilTest {

    @Test
    fun `calculatePriceInfo returns discount info when discountedPrice is lower than originalPrice`() {
        // Given
        val originalPrice = 10000
        val discountedPrice = 7000

        // When
        val result = PriceUtil.calculatePriceInfo(originalPrice, discountedPrice)

        // Then
        assertEquals("7000원", result.priceText)
        assertEquals("10000원", result.originalPriceText)
        assertEquals("30%", result.discountPercentText)
        assertEquals(true, result.showDiscount)
    }

    @Test
    fun `calculatePriceInfo returns no discount info when discountedPrice is null`() {
        // Given
        val originalPrice = 10000
        val discountedPrice = null

        // When
        val result = PriceUtil.calculatePriceInfo(originalPrice, discountedPrice)

        // Then
        assertEquals("10000원", result.priceText)
        assertEquals(null, result.originalPriceText)
        assertEquals(null, result.discountPercentText)
        assertEquals(false, result.showDiscount)
    }

    @Test
    fun `calculatePriceInfo returns no discount info when discountedPrice is equal to originalPrice`() {
        // Given
        val originalPrice = 10000
        val discountedPrice = 10000

        // When
        val result = PriceUtil.calculatePriceInfo(originalPrice, discountedPrice)

        // Then
        assertEquals("10000원", result.priceText)
        assertEquals(null, result.originalPriceText)
        assertEquals(null, result.discountPercentText)
        assertEquals(false, result.showDiscount)
    }

    @Test
    fun `calculatePriceInfo returns no discount info when discountedPrice is higher than originalPrice`() {
        // Given
        val originalPrice = 10000
        val discountedPrice = 12000

        // When
        val result = PriceUtil.calculatePriceInfo(originalPrice, discountedPrice)

        // Then
        assertEquals("10000원", result.priceText)
        assertEquals(null, result.originalPriceText)
        assertEquals(null, result.discountPercentText)
        assertEquals(false, result.showDiscount)
    }
}
