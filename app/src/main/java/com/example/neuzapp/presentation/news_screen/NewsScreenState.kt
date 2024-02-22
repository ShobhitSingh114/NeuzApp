package com.example.neuzapp.presentation.news_screen

import com.example.neuzapp.domain.model.News

data class NewsScreenState(
    val isLoading: Boolean = false,
    val news: News? = null,
    val error: String = ""
)
