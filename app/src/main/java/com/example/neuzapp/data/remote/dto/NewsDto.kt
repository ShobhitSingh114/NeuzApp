package com.example.neuzapp.data.remote.dto

import com.example.neuzapp.domain.model.News

data class NewsDto(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)

fun NewsDto.toNews(): News {
    return News(
        articles = articles
    )
}