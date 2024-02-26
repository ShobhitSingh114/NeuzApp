package com.example.neuzapp.presentation.news_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neuzapp.domain.use_case.GetTopHeadlineNews
import com.example.neuzapp.domain.use_case.SearchForNewsUseCase
import com.example.neuzapp.presentation.NewsScreenEvent
import com.example.neuzapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsScreenViewModel @Inject constructor(
    private val getTopHeadlineNews: GetTopHeadlineNews,
    private val searchForNewsUseCase: SearchForNewsUseCase
) : ViewModel() {

    private val _state = mutableStateOf(NewsScreenState())
    val state: State<NewsScreenState> = _state

    // coroutine job
    private var searchJob: Job? = null


    // _state.value = NewsScreenState(selectedArticle = event.article):
    // In this line, you are creating a new NewsScreenState object with the specified
    // selectedArticle value, and then assigning it directly to _state.value.
    // This essentially replaces the existing _state.value with a completely new object.

    //_state.value = _state.value.copy(selectedArticle = event.article):
    // Here, you are using the copy method to create a shallow copy of the existing _state.value object.
    // The selectedArticle parameter is updated with the value of event.article in the process.
    // This way, you maintain the structure and any other existing values in _state.value
    // while only modifying the specified parameter.

    fun onEvent(event: NewsScreenEvent) {
        when(event) {
            is NewsScreenEvent.onCategoryChanged -> {
                _state.value = _state.value.copy(category = event.category)
//                _state.value = NewsScreenState(category = event.category)
                getNews(_state.value.category)
            }

            NewsScreenEvent.onClosedIconClicked -> {
                _state.value = _state.value.copy(isSearchBarVisible = false)
                // when user click on closeIcon that means we now show like of news again
                // soo again calling getNews fun
                getNews(category = _state.value.category)
            }

            is NewsScreenEvent.onNewsCardClicked -> {
//                _state.value = NewsScreenState(selectedArticle = event.article)
                _state.value = _state.value.copy(selectedArticle = event.article)
            }

            NewsScreenEvent.onSearchIconClicked -> {
                // when user click searchIcon then user article list also becomes empty
                _state.value = _state.value.copy(isSearchBarVisible = true, news = null)
            }

            is NewsScreenEvent.onSearchQueryChanged -> {
                // 1.Updating the searchQueryState with the user input search
                // 2.Passing that query in SearchForNews from the searchQuery State


                _state.value = _state.value.copy(searchQuery = event.searchQuery)
//                searchForNews(event.searchQuery)

                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(1000)
                    searchForNews(_state.value.searchQuery)
//                    searchForNews(event.searchQuery)
                }

            }

        }

    }

    init {
        // category kaise melegi
        getNews(category = "general")
    }

//    private fun getNews(category: String) {
    private fun getNews(category: String) {
        getTopHeadlineNews(category).onEach {
            when(it) {
                is Resource.Success -> {
                    _state.value = NewsScreenState(news = it.data)
                }
                is Resource.Error -> {
                    _state.value = NewsScreenState(error = it.message ?: "An Unexpected error occurred")
                }
                is Resource.Loading -> {
                    _state.value = NewsScreenState(true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun searchForNews(query: String) {
        if (query.isEmpty()){
            return
        }
        searchForNewsUseCase(query).onEach {
            when(it) {
                is Resource.Success -> {
//                    _state.value = NewsScreenState(news = it.data)
                    _state.value = _state.value.copy(news = it.data, isSearchBarVisible = true, searchQuery = query)
//                    _state.value = _state.value.copy(news = it.data)
                }
                is Resource.Error -> {
                    _state.value = NewsScreenState(error = it.message ?: "An Unexpected error occurred")
                }
                is Resource.Loading -> {
                    _state.value = NewsScreenState(true)
                }
            }
        }.launchIn(viewModelScope)
    }

}