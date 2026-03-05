package com.gomguk.kirly.mockserver.core

interface FileProvider {
    fun getJsonFromAsset(filePath: String): String?
}
