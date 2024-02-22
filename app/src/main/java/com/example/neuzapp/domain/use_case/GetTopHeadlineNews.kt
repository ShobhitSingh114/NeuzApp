package com.example.neuzapp.domain.use_case

import com.example.neuzapp.data.remote.dto.toNews
import com.example.neuzapp.domain.model.News
import com.example.neuzapp.domain.repository.NewsRepository
import com.example.neuzapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetTopHeadlineNews @Inject constructor(
    private val repository: NewsRepository
) {

    operator fun invoke(category: String): Flow<Resource<News>> = flow {
        try {
            emit(Resource.Loading())
            val news = repository.getTopNewsHeadlines(category = category).toNews()
            emit(Resource.Success(data = news))
        }
        catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "An Unexpected Error Occurred"
                )
            )
        }
        catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server. Check your Internet Connection"))
        }
    }

}