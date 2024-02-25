package com.example.neuzapp.presentation

sealed class Screen(val route: String) {
    data object NewsScreen: Screen("news_screen")
    data object ArticleScreen: Screen("article_screen")
}