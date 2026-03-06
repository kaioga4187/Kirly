package com.gomguk.kirly.mockserver.core

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AssetFileProviderTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var assetFileProvider: AssetFileProvider

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun getJsonFromAsset_existingFile_returnsContent() {
        // Given
        val filePath = "test.json"

        // When
        val result = assetFileProvider.getJsonFromAsset(filePath)

        // Then
        assertNotNull(result)
        // Check if it contains expected content (ignoring whitespace)
        assertEquals(true, result?.contains("\"name\": \"test\""))
        assertEquals(true, result?.contains("\"value\": 123"))
    }

    @Test
    fun getJsonFromAsset_nonExistingFile_returnsNull() {
        // Given
        val filePath = "non_existing.json"

        // When
        val result = assetFileProvider.getJsonFromAsset(filePath)

        // Then
        assertNull(result)
    }
}
