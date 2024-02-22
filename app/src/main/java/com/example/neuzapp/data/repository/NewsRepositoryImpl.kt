package com.example.neuzapp.data.repository

import com.example.neuzapp.data.NewsApi
import com.example.neuzapp.data.remote.dto.NewsDto
import com.example.neuzapp.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi
) : NewsRepository{

    override suspend fun getTopNewsHeadlines(category: String): NewsDto {
        return api.getTopNewsHeadlines(category)
    }
}