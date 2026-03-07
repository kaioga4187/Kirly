package com.gomguk.kurly.data

data class SectionsResponse(
    val data: List<SectionInfo>,
    val paging: Paging?
)

data class Paging(
    val next_page: Int?
)

data class SectionInfo(
    val title: String,
    val id: Int,
    val type: SectionType,
    val url: String,
    var products: List<Product>? = null
)

sealed class SectionType {
    object Vertical : SectionType()
    object Horizontal : SectionType()
    object Grid : SectionType()
    data class Unknown(val value: String) : SectionType()
}
