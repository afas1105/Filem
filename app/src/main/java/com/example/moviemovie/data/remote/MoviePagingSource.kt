package com.example.moviemovie.data.remote

import androidx.paging.PagingSource
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGING_INDEX = 1

class MoviePagingSource (
    private val movieApi: MovieApi,
    private val query: String?
) : PagingSource<Int,Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val position = params.key ?: STARTING_PAGING_INDEX
            val response =
                if(query!=null) movieApi.searchMovies(query,position)
                else movieApi.getNowPlayingMovies(position)
            val movies = response.results

            LoadResult.Page(
                data = movies,
                prevKey = if (position== STARTING_PAGING_INDEX) null else position-1,
                nextKey = if (movies.isEmpty()) null else position+1
            )
        } catch (e: IOException){
            LoadResult.Error(e)
        } catch (e: HttpException){
            LoadResult.Error(e)
        }
    }
}