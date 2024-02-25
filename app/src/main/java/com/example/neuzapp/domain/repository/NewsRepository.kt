package com.example.neuzapp.domain.repository

import com.example.neuzapp.data.remote.dto.NewsDto

interface NewsRepository {
    suspend fun getTopNewsHeadlines(category: String): NewsDto
    suspend fun searchForNews(query: String): NewsDto
}