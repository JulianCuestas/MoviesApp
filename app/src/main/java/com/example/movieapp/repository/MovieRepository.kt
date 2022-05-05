package com.example.movieapp.repository

import com.example.movieapp.data.model.MovieList

interface MovieRepository {
    //suspend permite trabajar con funciones que toman un tiempo en traer info de un server o repo de datos (trabajo asyncrono)
    suspend fun getUpcomingMovies(): MovieList
    suspend fun getTopRatedMovies(): MovieList
    suspend fun getPopularMovies(): MovieList

}