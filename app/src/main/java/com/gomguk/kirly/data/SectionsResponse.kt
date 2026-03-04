package com.gomguk.kirly.data

data class SectionsResponse(
    val data: List<SectionInfo>,
    val paging: Paging?
)

data class Paging(
    val next_page: Int?
)
