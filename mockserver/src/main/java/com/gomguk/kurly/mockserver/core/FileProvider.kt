package com.gomguk.kurly.mockserver.core

interface FileProvider {
    fun getJsonFromAsset(filePath: String): String?
}
