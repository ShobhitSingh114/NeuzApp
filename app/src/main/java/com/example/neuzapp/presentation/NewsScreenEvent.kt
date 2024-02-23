package com.example.neuzapp.presentation

import com.example.neuzapp.data.remote.dto.Article

// state = Any value that can change during the usage of app.
// event = All possible actions user can do.

// This is a Event class
sealed class NewsScreenEvent() {
    data class onNewsCardClicked(val article: Article): NewsScreenEvent()
    data class onCategoryChanged(val category: String): NewsScreenEvent()
    data class onSearchQueryChanged(val searchQuery: String): NewsScreenEvent()
    data object onSearchIconClicked: NewsScreenEvent()
    data object onClosedIconClicked: NewsScreenEvent()
}

// In a nutshell
// event + value -> data class
// event -> object

// Q. In this event class some used data class and some used object/data-object why ??
// Ans = This is bcz, when a value also come with user's action there use data class
// ex. = if user selected a news card then we should also know which card is selected.

// when we don't need any value with the user action then we use data-object/object
// ex. = when user click search icon then we don't need any value from user side.