package com.example.neuzapp.presentation.news_screen

import com.example.neuzapp.data.remote.dto.Article
import com.example.neuzapp.domain.model.News


// state = Any value that can change during the usage of app.
// event = All possible actions user can do.

// This is a data class (obv.)
data class NewsScreenState(
    val isLoading: Boolean = false,
    val news: News? = null,
    val error: String = "",
    val isSearchBarVisible: Boolean = false,
    val selectedArticle: Article? = null,
    val category: String = "General",
    val searchQuery: String = ""
)
