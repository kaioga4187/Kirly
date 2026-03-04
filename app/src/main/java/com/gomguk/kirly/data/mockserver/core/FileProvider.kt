package com.gomguk.kirly.data.mockserver.core

internal interface FileProvider {
    fun getJsonFromAsset(filePath: String): String?
}
