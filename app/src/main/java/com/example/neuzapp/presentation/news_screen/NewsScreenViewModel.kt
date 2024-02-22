package com.example.neuzapp.presentation.news_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neuzapp.domain.use_case.GetTopHeadlineNews
import com.example.neuzapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NewsScreenViewModel @Inject constructor(
    private val getTopHeadlineNews: GetTopHeadlineNews
) : ViewModel() {

    private val _state = mutableStateOf(NewsScreenState())
    val state: State<NewsScreenState> = _state

    init {
        // category kaise melegi
        getNews(category = "general")
    }

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

}